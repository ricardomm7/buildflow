package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.*;
import fourcorp.buildflow.repository.ProductPriorityLine;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;
import fourcorp.buildflow.repository.WorkstationsPerOperation;

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
    public static ProductPriorityLine p = Repositories.getInstance().getProductPriorityRepository();
    public static WorkstationsPerOperation w = Repositories.getInstance().getWorkstationsPerOperation();
    public static ProductionTree pt = Repositories.getInstance().getProductionTree();

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
            String[] campos = linha.split(",");
            String id = campos[0];
            String name = campos[1];
            pt.insertProductionNode(id, name, true);
        }
        br.close();
    }

    public static void loadSimpleOperations(String s) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(s));
        br.readLine();
        String linha;
        while ((linha = br.readLine()) != null) {
            String[] campos = linha.split(",");
            String id = campos[0];
            String name = campos[1];
            pt.insertProductionNode(id, name, false);
        }
        br.close();
    }

    public static void loadBOO(String filepath) throws IOException {
        ProductionTree pt = Repositories.getInstance().getProductionTree();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                String itemId = parts[0];
                String opId = parts[1];

                ProductionNode itemNode = pt.getNodeById(itemId);
                if (itemNode == null) {
                    itemNode = new ProductionNode(itemId, itemId, true);
                    pt.insertProductionNode(itemId, itemId, true);
                }

                ProductionNode opNode = pt.getNodeById(opId);
                if (opNode == null) {
                    opNode = new ProductionNode(opId, opId, false);
                    pt.insertProductionNode(opId, opId, false);
                }

                pt.insertNewConnection(itemId, opId, 1); // Default quantity (or adjustable)
                itemNode.setParent(opNode);  // Set the parent operation

                for (int i = 2; i < parts.length; i += 2) {
                    String subitemId = parts[i];
                    int quantity = Integer.parseInt(parts[i + 1]);

                    ProductionNode subitemNode = pt.getNodeById(subitemId);
                    if (subitemNode == null) {
                        subitemNode = new ProductionNode(subitemId, subitemId, true);
                        pt.insertProductionNode(subitemId, subitemId, true);
                    }

                    pt.insertNewConnection(opId, subitemId, quantity);
                    subitemNode.setParent(opNode);  // Set the parent operation
                    subitemNode.setQuantity(quantity);  // Set the material quantity
                }
            }
        }
    }
}
