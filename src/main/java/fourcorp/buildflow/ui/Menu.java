package fourcorp.buildflow.ui;

import fourcorp.buildflow.application.MachineFlowAnalyzer;
import fourcorp.buildflow.application.Simulator;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.repository.Repositories;

import java.util.Scanner;

public class Menu {
    private final Scanner scanner;
    private final Simulator s;

    public Menu() {
        scanner = new Scanner(System.in);
        s = new Simulator();
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n--- BUILDFLOW MAIN MENU ---");
            System.out.println("1. Simulate production without priority order.");
            System.out.println("2. Simulate production with priority order.");
            System.out.println("3. See the uploaded products by priority.");
            System.out.println("4. See Production time.");
            System.out.println("5. See machines dependencies.");
            System.out.println("6. Workstation Analysis.");
            System.out.println("9. Exit");
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
                if (choice >= 1 && choice <= 9) {
                    return choice;
                } else {
                    System.out.print("Invalid option. Please try again: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 1:
                s.runWithoutPriority();
                break;
            case 2:
                s.runWithPriority();
                break;
            case 3:
                for (PriorityOrder priority : PriorityOrder.values()) {
                    System.out.println("\n----- For " + priority.toString());
                    for (Product c : Repositories.getInstance().getProductPriorityRepository().getProductsByPriority(priority)) {
                        System.out.println("Product ID: " + c.getId());
                    }
                }
                break;
            case 4:
                if (s.getProducts().isEmpty()) {
                    System.out.println("No production time data available. Please run the simulation first (Option 1 or 2).");
                } else {
                    s.printProductionTimePerProduct();
                    s.printTotalProductionTime();
                }
                break;

            case 5:
                if (MachineFlowAnalyzer.machineDependencies.isEmpty()) {
                    System.out.println("No dependencies found. Please run the simulation first (Option 1 or 2).");
                } else {
                    s.printMachineDependencies();
                }
                break;
            case 6:
                if (MachineFlowAnalyzer.machineDependencies.isEmpty()) {
                    System.out.println("Please run the simulation first (Option 1 or 2).");
                } else {
                    s.printAnalysis();
                }
                break;
            case 9:
                System.out.println("Exiting...");
                scanner.close();
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option.");
        }
    }
}
