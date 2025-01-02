package fourcorp.buildflow.ui;

import fourcorp.buildflow.application.*;
import fourcorp.buildflow.repository.MaterialQuantityBST;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class DatabaseMenu {
    private final Scanner scanner;
    private final DatabaseFunctionsController db;
    private final DisplayProductionTree ptVisualizer;
    private final ProductionTreeSearcher ptService;
    private final DisplayBST bstVisualizer;
    private final QualityCheckManager manager;
    private final CriticalPathCalculator calculator;
    private final CriticalPathPrioritizer prioritize;
    private final MaterialQuantityUpdater materialUpdater;

    public DatabaseMenu() {
        scanner = new Scanner(System.in);
        db = new DatabaseFunctionsController();
        ptVisualizer = new DisplayProductionTree();
        ptService = new ProductionTreeSearcher();
        prioritize = new CriticalPathPrioritizer();
        manager = new QualityCheckManager();
        bstVisualizer = new DisplayBST();
        calculator = new CriticalPathCalculator();
        ProductionTree productionTree = Repositories.getInstance().getProductionTree();
        MaterialQuantityBST materialQuantityBST = Repositories.getInstance().getMaterialBST();
        materialUpdater = new MaterialQuantityUpdater(productionTree, materialQuantityBST);
    }

    public void displayMenu() throws IOException {
        while (true) {
            System.out.println("\n================================================================================");
            System.out.println("                      BUILDFLOW CONNECTED FEATURES MENU                              ");
            System.out.println("================================================================================");
            System.out.printf("%-5s%-75s%n", "[1]", "See the parts used in a product.");
            System.out.printf("%-5s%-75s%n", "[2]", "See the list of operations involved in the production of a product.");
            System.out.printf("%-5s%-75s%n", "[3]", "Know which product uses all types of machines available.");
            System.out.printf("%-5s%-75s%n", "[4]", "Deactivate a costumer.");
            System.out.printf("%-5s%-75s%n", "[5]", "Import from Database BOM and BOO and generate production tree.");
            System.out.printf("%-5s%-75s%n", "[6]", "Create a new order.");
            System.out.printf("%-5s%-75s%n", "[7]", "Register new Product.");
            System.out.printf("%-5s%-75s%n", "[8]", "Product with the most operation in its BOO.");
            System.out.printf("%-5s%-75s%n", "[9]", "Register a Workstation.");
            System.out.printf("%-5s%-75s%n", "[10]", "Search for a node by name or ID.");
            System.out.printf("%-5s%-75s%n", "[11]", "Display materials in increasing order (by quantity).");
            System.out.printf("%-5s%-75s%n", "[12]", "Display materials in decreasing order (by quantity).");
            System.out.printf("%-5s%-75s%n", "[13]", "Prioritize quality checks.");
            System.out.printf("%-5s%-75s%n", "[14]", "Update material quantity.");
            System.out.printf("%-5s%-75s%n", "[15]", "View material quantities in the production tree.");
            System.out.printf("%-5s%-75s%n", "[16]", "Prioritize critical path by number of dependencies.");
            System.out.printf("%-5s%-75s%n", "[17]", "Prioritize critical path by depth Level.");
            System.out.printf("%-5s%-75s%n", "[18]", "Put the components into production.");
            System.out.printf("%-5s%-75s%n", "[19]", "Get a list of product operations.");
            System.out.printf("%-5s%-75s%n", "[20]", "Necessary stock for a given order.");
            System.out.printf("%-5s%-75s%n", "[21]", "Reserve materials/components to fulfill a given order.");
            System.out.printf("%-5s%-75s%n", "[22]", "List all the reserved materials/components.");
            System.out.printf("%-5s%-75s%n", "[23]", "Workstation types not used.");
            System.out.printf("%-5s%-75s%n", "[24]", "Consume a material/component, and deduct from the stock.");
            System.out.printf("%-5s%-75s%n", "[25]", "Simulate the production of the items required to accomplish the orders.");
            System.out.printf("%-5s%-75s%n", "[0]", "Escape to main menu.");
            System.out.println("================================================================================");

            System.out.print("Choose an option: ");
            int choice = getUserChoice();
            handleChoice(choice);
            if (choice == 0) {
                Repositories.clear();
                Reader.loadOperations("textFiles/articles.csv");
                Reader.loadMachines("textFiles/workstations.csv");
                Reader.loadItems("textFiles/items.csv");
                Reader.loadSimpleOperations("textFiles/operations.csv");
                Reader.loadBOO("textFiles/boo_v2.csv");
                return;
            }
        }
    }

    private int getUserChoice() {
        while (true) {
            try {
                String input = scanner.nextLine();
                int choice = Integer.parseInt(input);
                if (choice >= 0 && choice <= 25) {
                    return choice;
                } else {
                    System.out.print("Invalid option. Please try again: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    private void handleChoice(int choice) throws IOException {
        switch (choice) {
            case 1:
                System.out.println();
                System.out.print("Enter Product ID: ");
                String productId = scanner.nextLine();
                db.callSeeProductParts(productId);
                break;
            case 2:
                System.out.println();
                System.out.print("Enter Product ID: ");
                String productId2 = scanner.nextLine();
                db.callGetProductOperationsAndWorkstations(productId2);
                break;
            case 3:
                System.out.println();
                db.callPrintProductsUsingAllWorkstationTypes();
                break;
            case 4:
                System.out.println();
                System.out.print("Enter Costumer VAT: ");
                String nif = scanner.nextLine();
                db.callDeactivateCustomer(nif);
                break;
            case 5:
                System.out.println();
                System.out.print("Enter Product ID: ");
                String pid = scanner.nextLine();
                db.graphicVisualization(pid);
                Repositories.clear();
                Reader.loadSimpleOperations("textFiles/operationsLapr.csv");
                Reader.loadItems("textFiles/itemsLapr.csv");
                Reader.loadBOO("textFiles/boo_v2Lapr.csv");
                ptVisualizer.generateGraph();
                break;
            case 6:
                System.out.println();
                System.out.println("Create a New Order");
                System.out.print("Enter Order Date (YYYY-MM-DD): ");
                String orderDateInput = scanner.nextLine();
                System.out.print("Enter Delivery Date (YYYY-MM-DD): ");
                String deliveryDateInput = scanner.nextLine();
                System.out.print("Enter Customer VAT: ");
                String customerVat = scanner.nextLine();
                System.out.print("Enter Product ID: ");
                String productId3 = scanner.nextLine();

                try {
                    java.time.LocalDate orderDate = java.time.LocalDate.parse(orderDateInput);
                    java.time.LocalDate deliveryDate = java.time.LocalDate.parse(deliveryDateInput);

                    String result = db.registerOrder(orderDate, deliveryDate, customerVat, productId3);

                    System.out.println(result);
                } catch (Exception e) {
                    System.out.println("Invalid input. Please ensure the dates are in the correct format and try again.");
                }
                break;
            case 7:
                System.out.println();
                String result;
                do {
                    System.out.print("Enter Product ID: ");
                    String pid2 = scanner.nextLine();

                    System.out.print("Enter Product Name: ");
                    String pname = scanner.nextLine();

                    System.out.print("Enter Family ID: ");
                    String fid = scanner.nextLine();

                    result = db.registerNewProduct(pid2, pname, fid);

                    System.out.println(result);
                    if (result.contains("registered successfully")) {
                        break;
                    }
                    System.out.println("Please, try again.");
                } while (true);
                break;
            case 8:
                System.out.println();
                db.callProductWithMostOperations();
                break;
            case 9:
                System.out.println();

                System.out.print("Enter Workstation ID (4 characters, numeric): ");
                String workstationId = scanner.nextLine().trim();
                while (!workstationId.matches("^[0-9]{4}$")) {
                    System.out.print("Invalid Workstation ID. Must be 4 numbers. Try again: ");
                    workstationId = scanner.nextLine().trim();
                }

                System.out.print("Enter Workstation Name (max 60 characters): ");
                String name = scanner.nextLine().trim();
                while (name.isEmpty() || name.length() > 60) {
                    System.out.print("Invalid Workstation Name. Must be 1 to 60 characters. Try again: ");
                    name = scanner.nextLine().trim();
                }

                System.out.print("Enter Workstation Description (max 100 characters): ");
                String description = scanner.nextLine().trim();
                while (description.isEmpty() || description.length() > 100) {
                    System.out.print("Invalid Workstation Description. Must be 1 to 100 characters. Try again: ");
                    description = scanner.nextLine().trim();
                }

                db.showWorkstationTypes();
                System.out.print("Enter Workstation Type ID (5 characters, alphanumeric): ");
                String workstationType = scanner.nextLine().trim();
                while (!workstationType.matches("^[A-Za-z0-9]{5}$")) {
                    System.out.print("Invalid Workstation Type ID. Must be 5 alphanumeric characters. Try again: ");
                    workstationType = scanner.nextLine().trim();
                }

                db.registerWorkstation(workstationId, name, description, workstationType);
                break;
            case 10:
                ptService.handleNodeSearch();
                break;
            case 11:
                bstVisualizer.displayMaterialsByQuantity(true);
                break;
            case 12:
                bstVisualizer.displayMaterialsByQuantity(false);
                break;
            case 13:
                manager.prioritizeAndExecuteQualityChecks();
                break;
            case 14:
                materialUpdater.updateMaterialQuantity();
                break;
            case 15:
                ptVisualizer.displayMaterialQuantitiesInProductionTree();
                break;
            case 16:
                calculator.displayOperationsWithDependencies();
                break;
            case 17:
                prioritize.displayCriticalPathByDepth();
                break;
            case 18:
                ptService.simulateProductionExecution();
                break;
            case 19:
                System.out.println();
                System.out.print("Enter Product ID: ");
                productId = scanner.nextLine();
                db.callGetProductOperations(productId);  // Call the method to get product operations
                break;
            case 20:
                System.out.println();
                String order = db.displayOrders();
                if (order != null) {
                    db.checkStock(order);
                }
                break;
            case 21:
                System.out.println();
                String orderID = db.displayOrders();
                if (orderID != null) {
                    db.reserveOrderComponents(orderID);
                }
                break;
            case 22:
                System.out.println();
                db.showReservedParts();
                break;
            case 23:
                System.out.println();
                db.showUnusedWorkstations();
                break;
            case 24:
                System.out.println();
                System.out.print("Enter the part ID: ");
                String part = scanner.next();

                System.out.print("Enter the quantity to consume: ");
                double quantity;

                try {
                    quantity = Double.parseDouble(scanner.next());
                    System.out.println("Part ID: " + part);
                    System.out.println("Quantity: " + quantity);
                    db.consumeMaterial(part, quantity);
                    scanner.nextLine();
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Quantity must be a number.");
                    scanner.nextLine();
                    break;
                } catch (Exception e) {
                    System.out.println("Error during consume material operation: " + e.getMessage());
                    scanner.nextLine();
                    e.printStackTrace();
                }
                break;
            case 25:
                try {
                    WorkstationCompleter workstationCompleter = new WorkstationCompleter();
                    workstationCompleter.ensureCompleteWorkstationsFile();
                    OrderProductionManager manager = new OrderProductionManager();
                    manager.processOrdersFromFile("textFiles/orders.csv");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid option.");
        }
    }
}