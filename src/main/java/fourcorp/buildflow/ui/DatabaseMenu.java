package fourcorp.buildflow.ui;

import fourcorp.buildflow.application.DatabaseFunctionsController;
import fourcorp.buildflow.application.DisplayProductionTree;
import fourcorp.buildflow.application.Reader;
import fourcorp.buildflow.repository.Repositories;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

public class DatabaseMenu {
    private final Scanner scanner;
    private final DatabaseFunctionsController db;
    private final DisplayProductionTree ptVisualizer;

    public DatabaseMenu() {
        scanner = new Scanner(System.in);
        db = new DatabaseFunctionsController();
        ptVisualizer = new DisplayProductionTree();
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
                if (choice >= 0 && choice <= 10) {
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
                System.out.println("Creating a new order:");

                // Input Order ID
                String orderId;
                while (true) {
                    System.out.print("Enter Order ID: ");
                    orderId = scanner.nextLine();
                    if (orderId.isEmpty()) {
                        System.out.println("Error: Order ID cannot be empty. Please enter a valid Order ID.");
                    } else {
                        break;
                    }
                }

                // Input Order Date
                LocalDate orderDate = null;
                while (true) {
                    System.out.print("Enter Order Date (yyyy-mm-dd): ");
                    String orderDateInput = scanner.nextLine();
                    try {
                        orderDate = LocalDate.parse(orderDateInput);
                        break;  // Break if the date is valid
                    } catch (Exception e) {
                        System.out.println("Error: Invalid date format. Please enter the Order Date in yyyy-mm-dd format.");
                    }
                }

                // Input Delivery Date and verify it is not before the Order Date
                LocalDate deliveryDate = null;
                while (true) {
                    System.out.print("Enter Delivery Date (yyyy-mm-dd): ");
                    String deliveryDateInput = scanner.nextLine();
                    try {
                        deliveryDate = LocalDate.parse(deliveryDateInput);
                        // Check if the delivery date is before the order date
                        if (deliveryDate.isBefore(orderDate)) {
                            System.out.println("Error: Delivery date cannot be before the order date. Please enter a valid Delivery Date.");
                        } else {
                            break;  // Break if the date is valid
                        }
                    } catch (Exception e) {
                        System.out.println("Error: Invalid date format. Please enter the Delivery Date in yyyy-mm-dd format.");
                    }
                }

                // Input VAT (Simple validation: check if it's not empty)
                String vat;
                while (true) {
                    System.out.print("Enter Customer VAT: ");
                    vat = scanner.nextLine();
                    if (vat.isEmpty()) {
                        System.out.println("Error: VAT cannot be empty. Please enter a valid VAT.");
                    } else {
                        break;
                    }
                }

                // Input Product ID (Simple validation: check if it's not empty)
                String productId6;
                while (true) {
                    System.out.print("Enter Product ID: ");
                    productId6 = scanner.nextLine();
                    if (productId6.isEmpty()) {
                        System.out.println("Error: Product ID cannot be empty. Please enter a valid Product ID.");
                    } else {
                        break;
                    }
                }

                try {
                    // Call the registerOrder function if all details are valid
                    String result = db.registerOrder(orderId, orderDate, deliveryDate, vat, productId6);
                    System.out.println("\nResult: " + result);
                } catch (Exception e) {
                    System.out.println("Error: System failure while processing the order. Please try again.");
                    e.printStackTrace();
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

                    // Chamar o método para registrar o produto
                    result = db.RegisterNewProduct(pid2, pname, fid);

                    // Exibir o feedback para o usuário
                    System.out.println(result);

                    // Se o resultado indicar sucesso, sair do loop
                    if (result.contains("registered successfully")) {
                        break;
                    }

                    // Caso contrário, permitir nova tentativa
                    System.out.println("Please, try again.");
                } while (true);
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid option.");
        }
    }
}