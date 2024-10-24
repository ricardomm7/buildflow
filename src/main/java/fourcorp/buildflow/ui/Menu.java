package fourcorp.buildflow.ui;

import fourcorp.buildflow.application.GraphViz;
import fourcorp.buildflow.application.MachineFlowAnalyzer;
import fourcorp.buildflow.application.Simulator;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.repository.Repositories;

import java.io.IOException;
import java.util.Scanner;

public class Menu {
    private final Scanner scanner;
    //private final Simulator s;

    public Menu() {
        scanner = new Scanner(System.in);
        //s = new Simulator();
    }

    public void displayMenu() throws IOException {
        while (true) {
            System.out.println("\n--- BUILDFLOW MAIN MENU ---");
            System.out.println("1. Simulate production without priority order and with line workstation selection.");
            System.out.println("2. Simulate production without priority order and with time workstation selection.");
            System.out.println("3. Simulate production with priority order and with line workstation selection.");
            System.out.println("4. Simulate production with priority order and with time workstation selection.");
            System.out.println("5. See the uploaded products by priority.");
            System.out.println("6. Generate product-component graph.");
            System.out.println("7. See machines dependencies.");
            System.out.println("8. Workstation Analysis.");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice = getUserChoice();
            handleChoice(choice);
        }
    }

    private int getUserChoice() {
        while (true) {
            try {
                String input = scanner.nextLine();
                int choice = Integer.parseInt(input);
                if (choice >= 0 && choice <= 9) {
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
                Simulator s = new Simulator();
                break;
            case 2:
                //s.runWithoutPriority(true);
                break;
            case 3:
               // s.runWithPriority(false);
                break;
            case 4:
               // s.runWithPriority(true);
            case 5:
                for (PriorityOrder priority : PriorityOrder.values()) {
                    System.out.println("\n----- For " + priority.toString());
                    for (Product c : Repositories.getInstance().getProductPriorityRepository().getProductsByPriority(priority)) {
                        System.out.println("Product ID: " + c.getId());
                    }
                }
                break;
            case 6:
                displayAvailableItems();
                int itemChoice = getUserChoice();
                String selectedItem = getItemFilePath(itemChoice);
                if (selectedItem != null) {
                    GraphViz.saveInformation(selectedItem);
                    String itemName = selectedItem.substring(selectedItem.lastIndexOf("/") + 1, selectedItem.indexOf(".csv"));
                    GraphViz.generateProductComponentGraph(itemName);
                } else {
                    System.out.println("Invalid choice. Please select a valid item.");
                }
                break;
            case 7:
                if (MachineFlowAnalyzer.machineDependencies.isEmpty()) {
                    System.out.println("Please run the simulation first (Option 1 or 2).");
                } else {
                    //s.printMachineDependencies();
                }
                break;
            case 8:
                //s.printProductionStatistics();
                break;

            case 0:
                System.out.println("Exiting...");
                scanner.close();
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private void displayAvailableItems() {
        System.out.println("\nAvailable items:");
        System.out.println("1. Table");
        System.out.println("2. Chair");
        System.out.println("3. Bicycle");
        System.out.println("4. Bookshelf");
        System.out.println("5. Lamp");
        System.out.print("Choose an item: ");
    }

    private String getItemFilePath(int choice) {
        switch (choice) {
            case 1:
                return "textFiles/table.csv";
            case 2:
                return "textFiles/chair.csv";
            case 3:
                return "textFiles/bicycle.csv";
            case 4:
                return "textFiles/bookshelf.csv";
            case 5:
                return "textFiles/lamp.csv";
            default:
                return null;
        }
    }
}
