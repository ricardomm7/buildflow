package fourcorp.buildflow.ui;

import fourcorp.buildflow.application.DatabaseFunctionsController;

import java.io.IOException;
import java.util.Scanner;

public class DatabaseMenu {
    private final Scanner scanner;
    private final DatabaseFunctionsController db;

    public DatabaseMenu() {
        scanner = new Scanner(System.in);
        db = new DatabaseFunctionsController();
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
            System.out.printf("%-5s%-75s%n", "[0]", "Escape to main menu.");
            System.out.println("================================================================================");

            System.out.print("Choose an option: ");
            int choice = getUserChoice();
            handleChoice(choice);
            if (choice == 0) {
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
            case 0:
                return;
            default:
                System.out.println("Invalid option.");
        }
    }
}