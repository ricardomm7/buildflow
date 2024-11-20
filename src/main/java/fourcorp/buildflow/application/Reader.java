package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.*;
import fourcorp.buildflow.repository.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Reader is an abstract utility class responsible for loading data from external files
 * to populate the application's repositories for product priorities and workstation operations.
 * It provides methods for reading and parsing data from CSV files to create products and workstations.
 */
public abstract class Reader {
    private static ProductPriorityLine p = Repositories.getInstance().getProductPriorityRepository();
    private static WorkstationsPerOperation w = Repositories.getInstance().getWorkstationsPerOperation();
    private static ProductionTree pt = Repositories.getInstance().getProductionTree();
    private static MaterialQuantityBST bst = Repositories.getInstance().getMaterialBST();

    /**
     * Loads product data from a specified file and populates the product priority repository.
     * Each line in the file should represent a product, including its ID, priority, and a list of operations.
     *
     * @param filePath the path to the file containing product data
     * @throws IOException if an I/O error occurs while reading the file
     */
    public static void loadOperations(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        br.readLine();
        String line;
        while ((line = br.readLine()) != null) {
            String[] fields = line.split(";");
            String idItem = fields[0];
            PriorityOrder priorityOrder = getPriorityOrderFromValue(fields[1]);
            LinkedList<Operation> operations = new LinkedList<>();
            for (int i = 2; i < fields.length; i++) {
                operations.add(new Operation(fields[i]));
            }
            p.create(new Product(idItem, operations), priorityOrder);
        }
        br.close();
    }

    /**
     * Loads workstation data from a specified file and populates the workstations per operation repository.
     * Each line in the file should represent a workstation, including its ID, associated operation, and capacity.
     *
     * @param filePath the path to the file containing workstation data
     * @throws IOException if an I/O error occurs while reading the file
     */
    public static void loadMachines(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        br.readLine();
        String linha;
        while ((linha = br.readLine()) != null) {
            String[] campos = linha.split(";");
            Workstation maquina = new Workstation(campos[0], Integer.parseInt(campos[2]));
            Operation operation = new Operation(campos[1]);
            w.create(maquina, operation);
        }
        br.close();
    }

    /**
     * Maps a string value to the corresponding PriorityOrder enumeration value.
     *
     * @param value the string representation of the priority ("HIGH", "NORMAL", or "LOW")
     * @return the corresponding PriorityOrder enum value
     * @throws IllegalArgumentException if the input string does not match any valid priority
     */
    private static PriorityOrder getPriorityOrderFromValue(String value) {
        switch (value.toUpperCase()) {
            case "HIGH":
                return PriorityOrder.HIGH;
            case "NORMAL":
                return PriorityOrder.NORMAL;
            case "LOW":
                return PriorityOrder.LOW;
            default:
                throw new IllegalArgumentException("Invalid priority value: " + value);
        }
    }

    public static void loadItems(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        br.readLine();
        String linha;
        while ((linha = br.readLine()) != null) {
            String[] campos = linha.split(";");
            String id = campos[0];
            String name = campos[1];
            pt.insertProductionNode(id, name, true);
            bst.insert(new ProductionNode(id, name, true), 0); // Quantidade inicial definida como 0
        }
        br.close();
    }

    public static void loadSimpleOperations(String s) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(s));
        br.readLine();
        String linha;
        while ((linha = br.readLine()) != null) {
            String[] campos = linha.split(";");
            String id = campos[0];
            String name = campos[1];
            pt.insertProductionNode(id, name, false);
        }
        br.close();
    }

    public static void loadBOO(String filepath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            reader.readLine(); // Ignorar o cabeçalho

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                String[] parts = line.split(";");
                String opId = parts[0];
                String itemId = parts[1];
                double itemQnt = Double.parseDouble(parts[2].replace(",", "."));

                ProductionNode itemNode = pt.getNodeById(itemId);
                if (itemNode == null) {
                    itemNode = new ProductionNode(itemId, itemId, true);
                    pt.insertProductionNode(itemId, itemId, true);
                }
                itemNode.setQuantity(itemQnt);
                bst.insert(itemNode, itemQnt);

                ProductionNode opNode = pt.getNodeById(opId);
                if (opNode == null) {
                    opNode = new ProductionNode(opId, opId, false);
                    pt.insertProductionNode(opId, opId, false);
                }

                pt.insertNewConnection(itemId, opId, itemQnt);
                itemNode.setParent(opNode);

                int i = 3; // Início após op_id, item_id e item_qtd
                boolean isOperationsBlock = false;
                boolean isMaterialsBlock = false;

                while (i < parts.length) {
                    String part = parts[i].trim();
                    if (part.isEmpty()) {
                        i++;
                        continue;
                    }

                    if (part.equals("(")) {
                        if (!isOperationsBlock) {
                            isOperationsBlock = true;
                        } else if (isOperationsBlock) {
                            isMaterialsBlock = true;
                        }
                        i++;
                        continue;
                    } else if (part.equals(")")) {
                        if (isMaterialsBlock) {
                            isMaterialsBlock = false;
                        } else if (isOperationsBlock) {
                            isOperationsBlock = false;
                        }
                        i++;
                        continue;
                    }

                    if (isOperationsBlock && !isMaterialsBlock) {
                        String subOpId = parts[i];
                        double subOpQnt = Double.parseDouble(parts[i + 1].replace(",", "."));

                        ProductionNode subOpNode = pt.getNodeById(subOpId);
                        if (subOpNode == null) {
                            subOpNode = new ProductionNode(subOpId, subOpId, false);
                            pt.insertProductionNode(subOpId, subOpId, false);
                        }

                        pt.insertNewConnection(opId, subOpId, subOpQnt);
                        subOpNode.setQuantity(subOpQnt);
                        bst.insert(subOpNode, subOpQnt);

                        i += 2;
                    } else if (isMaterialsBlock) {
                        String materialId = parts[i];
                        double materialQnt = Double.parseDouble(parts[i + 1].replace(",", "."));

                        ProductionNode materialNode = pt.getNodeById(materialId);
                        if (materialNode == null) {
                            materialNode = new ProductionNode(materialId, materialId, true);
                            pt.insertProductionNode(materialId, materialId, true);
                        }

                        pt.insertNewConnection(opId, materialId, materialQnt);
                        materialNode.setParent(opNode);
                        materialNode.setQuantity(materialQnt);
                        bst.insert(materialNode, materialQnt);

                        i += 2;
                    } else {
                        i++;
                    }
                }
            }
        }
    }

    public static void setAtt(ProductionTree p) {
        pt = p;
    }
}
