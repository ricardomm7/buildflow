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
                    // Parse dates
                    java.time.LocalDate orderDate = java.time.LocalDate.parse(orderDateInput);
                    java.time.LocalDate deliveryDate = java.time.LocalDate.parse(deliveryDateInput);

                    // Call the `registerOrder` method
                    String result = db.registerOrder(orderDate, deliveryDate, customerVat, productId3);

                    // Display result to the user
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