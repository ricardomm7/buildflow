package fourcorp.buildflow.ui;

import fourcorp.buildflow.application.*;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.repository.MaterialQuantityBST;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;

import java.io.IOException;
import java.util.Scanner;

public class Menu {
    private final Scanner scanner;
    private final Simulator s;
    private final DisplayProductionTree ptVisualizer;
    private final DisplayBST bstVisualizer;
    private final ProductionTreeSearcher ptService;
    private final CriticalPathPrioritizer prioritize;
    private final QualityCheckManager manager;
    private final CriticalPathCalculator calculator;
    private final MaterialQuantityUpdater materialUpdater;

    public Menu() {
        scanner = new Scanner(System.in);
        s = new Simulator();
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
            System.out.println("                             BUILDFLOW MAIN MENU                              ");
            System.out.println("================================================================================");
            System.out.printf("%-5s%-75s%n", "[1]", "See the uploaded products by priority.");
            System.out.printf("%-5s%-75s%n", "[2]", "Simulate production without priority, using line workstation selection.");
            System.out.printf("%-5s%-75s%n", "[3]", "Simulate production without priority, using time workstation selection.");
            System.out.printf("%-5s%-75s%n", "[4]", "Simulate production with priority, using line workstation selection.");
            System.out.printf("%-5s%-75s%n", "[5]", "Simulate production with priority, using time workstation selection.");
            System.out.printf("%-5s%-75s%n", "[6]", "View production times.");
            System.out.printf("%-5s%-75s%n", "[7]", "View machine dependencies.");
            System.out.printf("%-5s%-75s%n", "[8]", "Report average times per operation.");
            System.out.printf("%-5s%-75s%n", "[9]", "Workstation analysis.");
            System.out.printf("%-5s%-75s%n", "[10]", "Generate product-component graph.");
            System.out.printf("%-5s%-75s%n", "[11]", "Display materials in increasing order (by quantity).");
            System.out.printf("%-5s%-75s%n", "[12]", "Display materials in decreasing order (by quantity)");
            System.out.printf("%-5s%-75s%n", "[13]", "Search for a node by name or ID.");
            System.out.printf("%-5s%-75s%n", "[14]", "Update material quantity.");
            System.out.printf("%-5s%-75s%n", "[15]", "See the production tree (console).");
            System.out.printf("%-5s%-75s%n", "[16]", "See the production tree (graphical).");
            System.out.printf("%-5s%-75s%n", "[17]", "Prioritize Quality Checks.");
            System.out.printf("%-5s%-75s%n", "[18]", "Prioritize Critical path by Number of Dependencies.");
            System.out.printf("%-5s%-75s%n", "[19]", "Prioritize Critical path by Depth Level.");
            System.out.printf("%-5s%-75s%n", "[20]", "View material quantities in the Production Tree.");
            System.out.printf("%-5s%-75s%n", "[0]", "Exit");
            System.out.println("================================================================================");

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
                if (choice >= 0 && choice <= 20) {
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
                for (PriorityOrder priority : PriorityOrder.values()) {
                    System.out.println("\n>>> FOR " + priority.toString().toUpperCase() + " PRIORITY");
                    for (Product c : Repositories.getInstance().getProductPriorityRepository().getProductsByPriority(priority)) {
                        System.out.println("Product ID: " + c.getId());
                    }
                }
                break;
            case 2:
                s.runWithoutPriority(false);
                break;
            case 3:
                s.runWithoutPriority(true);
                break;
            case 4:
                s.runWithPriority(false);
                break;
            case 5:
                s.runWithPriority(true);
                break;
            case 6:
                if (s.getTotalProductionTime() == 0) {
                    System.out.println("Please run the simulation first (Option 2 to 5).");
                } else {
                    s.printProductionStatistics();
                }
                break;
            case 7:
                if (s.getTotalProductionTime() == 0) {
                    System.out.println("Please run the simulation first (Option 2 to 5).");
                } else {
                    MachineFlowAnalyzer.printDependencies();
                }
                break;
            case 8:
                if (s.getTotalProductionTime() == 0) {
                    System.out.println("Please run the simulation first (Option 2 to 5).");
                } else {
                    s.printAverageTimesReport();
                }
                break;
            case 9:
                if (s.getTotalProductionTime() == 0) {
                    System.out.println("Please run the simulation first (Option 2 to 5).");
                } else {
                    s.printAnalysis();
                }
                break;
            case 10:
                System.out.println("Not implemented yet");
                break;
            case 11:
                bstVisualizer.displayMaterialsByQuantity(true);
                break;
            case 12:
                bstVisualizer.displayMaterialsByQuantity(false);
                break;
            case 13:
                System.out.print("Enter the ID or name of the node to search: ");
                String identifier = scanner.nextLine();
                String result = ptService.searchNodeByNameOrId(identifier);
                System.out.println(result);
                break;
            case 14:
                materialUpdater.updateMaterialQuantity();
                break;
            case 15:
                ptVisualizer.displayTree();
                break;
            case 16:
                ptVisualizer.generateGraph();
                break;
            case 17:
                manager.prioritizeAndExecuteQualityChecks();
                break;
            case 18:
                calculator.displayOperationsWithDependencies();
                break;
            case 19:
                prioritize.displayCriticalPathByDepth();
                break;
            case 20:
                ptVisualizer.displayMaterialQuantitiesInProductionTree();
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
}