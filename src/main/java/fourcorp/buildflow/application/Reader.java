package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.*;
import fourcorp.buildflow.repository.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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
    private static ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();

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

    /**
     * Load items.
     * The complexity of this method is: O(n^2).
     *
     * @param filePath the file path
     * @throws IOException the io exception
     */
    public static void loadItems(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        br.readLine();
        String linha;
        while ((linha = br.readLine()) != null) { // O(n)
            String[] campos = linha.split(";");
            String id = campos[0];
            String name = campos[1];
            pt.insertProductionNode(id, name, true); // O(1)
            bst.insert(new ProductionNode(id, name, true), 0); // O(n^2log(n)) // Quantidade inicial definida como 0
        }
        br.close();
    }

    /**
     * Load simple operations.
     * The complexity of this method is: O(n).
     *
     * @param s the s
     * @throws IOException the io exception
     */
    public static void loadSimpleOperations(String s) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(s));
        br.readLine();
        String linha;
        while ((linha = br.readLine()) != null) { // O(n)
            String[] campos = linha.split(";");
            String id = campos[0];
            String name = campos[1];
            pt.insertProductionNode(id, name, false);  // O(1)
        }
        br.close();
    }

    /**
     * Load boo.
     * The complexity of this method is: O(n^2log(n)).
     *
     * @param filepath the filepath
     * @throws IOException the io exception
     */
    public static void loadBOO(String filepath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            reader.readLine(); // Ignorar o cabeçalho

            while ((line = reader.readLine()) != null) { // O(n)
                line = line.trim();

                String[] parts = line.split(";");
                String opId = parts[0];
                String itemId = parts[1];
                double itemQnt = Double.parseDouble(parts[2].replace(",", "."));

                ProductionNode itemNode = pt.getNodeById(itemId); // O(1)
                if (itemNode == null) {
                    itemNode = new ProductionNode(itemId, itemId, true);
                    pt.insertProductionNode(itemId, itemId, true); // O(1)
                }
                itemNode.setQuantity(itemQnt);
                bst.insert(itemNode, itemQnt); // O(nlog(n))

                ProductionNode opNode = pt.getNodeById(opId); // O(1)
                if (opNode == null) {
                    opNode = new ProductionNode(opId, opId, false);
                    pt.insertProductionNode(opId, opId, false); // O(1)
                }

                pt.insertNewConnection(itemId, opId, itemQnt); // O(1)
                itemNode.setParent(opNode);

                int i = 3; // Início após op_id, item_id e item_qtd
                boolean isOperationsBlock = false;
                boolean isMaterialsBlock = false;

                while (i < parts.length) { // O(1)
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

                        ProductionNode subOpNode = pt.getNodeById(subOpId); // O(1)
                        if (subOpNode == null) {
                            subOpNode = new ProductionNode(subOpId, subOpId, false);
                            pt.insertProductionNode(subOpId, subOpId, false); // O(1)
                        }

                        pt.insertNewConnection(opId, subOpId, subOpQnt);  // O(1)
                        subOpNode.setQuantity(subOpQnt);
                        bst.insert(subOpNode, subOpQnt); // O(nlog(n))

                        i += 2;
                    } else if (isMaterialsBlock) {
                        String materialId = parts[i];
                        double materialQnt = Double.parseDouble(parts[i + 1].replace(",", "."));

                        ProductionNode materialNode = pt.getNodeById(materialId);  // O(1)
                        if (materialNode == null) {
                            materialNode = new ProductionNode(materialId, materialId, true);
                            pt.insertProductionNode(materialId, materialId, true);  // O(1)
                        }

                        pt.insertNewConnection(opId, materialId, materialQnt);  // O(1)
                        materialNode.setParent(opNode);
                        materialNode.setQuantity(materialQnt);
                        bst.insert(materialNode, materialQnt);  // O(nlog(n))

                        i += 2;
                    } else {
                        i++;
                    }
                }
            }
        }
    }

    /**
     * Load production tree production tree.
     * The complexity of this method is: O(n^3).
     *
     * @param operationsFile the operations file
     * @param itemsFile      the items file
     * @param booFile        the boo file
     * @return the production tree
     * @throws IOException the io exception
     */
    public static ProductionTree loadProductionTree(String operationsFile, String itemsFile, String booFile) throws IOException {
        ProductionTree productionTree = new ProductionTree(); // Cria uma nova árvore de produção

        // Ler operações
        try (BufferedReader br = new BufferedReader(new FileReader(operationsFile))) {
            br.readLine(); // Ignorar cabeçalho
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(";");
                String opId = campos[0];
                String opName = campos[1];
                productionTree.insertProductionNode(opId, opName, false); // O(1)
            }
        }

        // Ler itens
        try (BufferedReader br = new BufferedReader(new FileReader(itemsFile))) {
            br.readLine(); // Ignorar cabeçalho
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(";");
                String itemId = campos[0];
                String itemName = campos[1];
                productionTree.insertProductionNode(itemId, itemName, true); // O(1)
            }
        }

        // Ler BOO e construir dependências
        try (BufferedReader reader = new BufferedReader(new FileReader(booFile))) {
            String line;
            reader.readLine(); // Ignorar o cabeçalho

            while ((line = reader.readLine()) != null) { // O(n)
                line = line.trim();

                String[] parts = line.split(";");
                String opId = parts[0];
                String itemId = parts[1];
                double itemQnt = Double.parseDouble(parts[2].replace(",", "."));

                ProductionNode opNode = productionTree.getNodeById(opId);
                if (opNode == null) {
                    opNode = new ProductionNode(opId, opId, false);
                    productionTree.insertProductionNode(opId, opId, false); // O(1)
                }

                ProductionNode itemNode = productionTree.getNodeById(itemId);
                if (itemNode == null) {
                    itemNode = new ProductionNode(itemId, itemId, true);
                    productionTree.insertProductionNode(itemId, itemId, true); // O(1)
                }

                // Atualizar quantidade e conexões
                itemNode.setQuantity(itemQnt);
                productionTree.insertNewConnection(itemId, opId, itemQnt); // O(1)
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

                        ProductionNode subOpNode = productionTree.getNodeById(subOpId);
                        if (subOpNode == null) {
                            subOpNode = new ProductionNode(subOpId, subOpId, false);
                            productionTree.insertProductionNode(subOpId, subOpId, false); // O(1)
                        }

                        subOpNode.setQuantity(subOpQnt);
                        productionTree.insertNewConnection(opId, subOpId, subOpQnt); // O(1)

                        i += 2;
                    } else if (isMaterialsBlock) {
                        String materialId = parts[i];
                        double materialQnt = Double.parseDouble(parts[i + 1].replace(",", "."));

                        ProductionNode materialNode = productionTree.getNodeById(materialId);
                        if (materialNode == null) {
                            materialNode = new ProductionNode(materialId, materialId, true);
                            productionTree.insertProductionNode(materialId, materialId, true); // O(1)
                        }

                        materialNode.setQuantity(materialQnt);
                        productionTree.insertNewConnection(opId, materialId, materialQnt); // O(1)
                        materialNode.setParent(opNode);

                        i += 2;
                    } else {
                        i++;
                    }
                }
            }
        }

        return productionTree; // Retorna a árvore construída
    }


    public static void setProdTree(ProductionTree p) {
        pt = p;
    }

    public static void setBST(MaterialQuantityBST p) {
        bst = p;
    }

    /**
     * Load activities.
     * The complexity of this method is: O(n).
     *
     * @param filePath the file path
     * @throws IOException the io exception
     */
    public static void loadActivities(String filePath) throws IOException {
        List<Activity> allActivities = new ArrayList<>();
        Map<String, Activity> activityMap = new HashMap<>();

        // Primeiro, ler e criar todas as atividades
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                Activity activity = getActivity(line);
                allActivities.add(activity);
                activityMap.put(activity.getId(), activity);
                graph.addActivity(activity);  // Adicionar vértice ao grafo
            }
        }

        // Depois, adicionar dependências
        for (Activity activity : allActivities) { // O(n)
            for (String depId : activity.getDependencies()) { // O(n^2)
                Activity dependencyActivity = activityMap.get(depId);
                if (dependencyActivity != null) {
                    graph.addDependency(dependencyActivity, activity);
                }
            }
        }

        // Verificar dependências circulares
        boolean cycleActivityId = graph.detectCircularDependencies(); // O(n)
        if (cycleActivityId) {
            //System.err.println("Graph creation aborted due to circular dependency detected.");
            throw new RuntimeException("Program aborted due to the circular dependency(ies) found.");
        }
    }

    private static Activity getActivity(String line) {
        String[] parts = line.split(",");
        if (parts.length < 6) {
            throw new IllegalArgumentException("Invalid activity format in CSV");
        }

        String id = parts[0].trim();
        String name = parts[1].trim();
        int duration = Integer.parseInt(parts[2].trim());
        String durationUnit = parts[3].trim();
        double cost = Double.parseDouble(parts[4].trim());
        String costUnit = parts[5].trim();

        List<String> dependencies = new ArrayList<>();
        if (parts.length > 6) {
            for (int i = 6; i < parts.length; i++) {
                String dep = parts[i].trim();
                if (!dep.isEmpty()) {
                    dependencies.add(dep);
                }
            }
        }

        return new Activity(id, name, duration, durationUnit, cost, costUnit, dependencies);
    }
}
