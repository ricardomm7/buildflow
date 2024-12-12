package fourcorp.buildflow.ui;

import fourcorp.buildflow.application.*;
import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.MaterialQuantityBST;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
    private final PertCpmVisualizer pert;
    private final ActivityTopologicalSort topologicalSort;
    private final BottleneckIdentifier bottleneckIdentifier;
    private final ActivityTimeCalculator calculatorGraph;
    private final ProjectDelaySimulator delaySimulator;


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
        pert = new PertCpmVisualizer();
        topologicalSort = new ActivityTopologicalSort();
        bottleneckIdentifier = new BottleneckIdentifier();
        calculatorGraph = new ActivityTimeCalculator();
        delaySimulator = new ProjectDelaySimulator();
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
            System.out.printf("%-5s%-75s%n", "[17]", "Prioritize quality checks.");
            System.out.printf("%-5s%-75s%n", "[18]", "Prioritize critical path by number of dependencies.");
            System.out.printf("%-5s%-75s%n", "[19]", "Prioritize critical path by depth Level.");
            System.out.printf("%-5s%-75s%n", "[20]", "View material quantities in the production tree.");
            System.out.printf("%-5s%-75s%n", "[21]", "Database connected features (submenu).");
            System.out.printf("%-5s%-75s%n", "[22]", "See the total quantity of materials.");
            System.out.printf("%-5s%-75s%n", "[23]", "Put the components into production.");
            System.out.printf("%-5s%-75s%n", "[24]", "See a specific product production tree (graphical).");
            System.out.printf("%-5s%-75s%n", "[25]", "See the PERT-CPM graph (console).");
            System.out.printf("%-5s%-75s%n", "[26]", "Topological sort of project activities.");
            System.out.printf("%-5s%-75s%n", "[27]", "Identify bottleneck activities.");
            System.out.printf("%-5s%-75s%n", "[28]", "Calculate Earliest and Latest Time.");
            System.out.printf("%-5s%-75s%n", "[29]", "Simulate project delay.");
            System.out.printf("%-5s%-75s%n", "[30]", "See the critical path.");
            System.out.printf("%-5s%-75s%n", "[31]", "Export Schedule.");
            System.out.printf("%-5s%-75s%n", "[32]", "Display graph (graphical).");
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
                if (choice >= 0 && choice <= 32) {
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
                ProductionTree tree = Repositories.getInstance().getProductionTree();
                System.out.println();
                System.out.print("Enter Product Name or ID: ");
                String rootNode = scanner.nextLine();

                ProductionNode rootNodeId = tree.getNodeByNameOrId(rootNode);
                while (rootNodeId == null) {
                    System.out.println("Product ID not found. Please enter a valid Product ID: ");
                    rootNode = scanner.nextLine();
                    rootNodeId = tree.getNodeByNameOrId(rootNode);
                }
                ProductionTree bomTree = BomTreeBuilder.createBOMTree("textFiles/boo_v2.csv", "textFiles/items.csv", rootNodeId);
                ptVisualizer.setProductionTree(bomTree);
                ptVisualizer.generateGraph();
                break;
            case 11:
                bstVisualizer.displayMaterialsByQuantity(true);
                break;
            case 12:
                bstVisualizer.displayMaterialsByQuantity(false);
                break;
            case 13:
                ptService.handleNodeSearch();
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
            case 21:
                DatabaseMenu m = new DatabaseMenu();
                m.displayMenu();
                break;
            case 22:
                System.out.println();
                ptService.calculateQuantityOfMaterials();
                break;
            case 23:
                System.out.println();
                ptService.simulateProductionExecution();
                break;
            case 24:
                System.out.println();
                System.out.print("Enter Product ID: ");
                String id = scanner.nextLine();
                ptVisualizer.loadSubTreeFromNode(id);
                break;
            case 25:
                pert.printGraph();
                break;
            case 26:
                System.out.println();
                topologicalSort.handleTopologicalSort();
                break;
            case 27:
                bottleneckIdentifier.identifyBottleneckActivities();
                break;
            case 28:
                calculatorGraph.calculateTimes();
                calculatorGraph.displayTimes();
                break;
            case 29:

                Map<String, Integer> delays = new HashMap<>();

                while (true) {
                    // Display all activities
                    System.out.println("\nAvailable Activities:");
                    ActivityTopologicalSort sort = new ActivityTopologicalSort();
                    for (Activity activity : sort.performTopologicalSort()) {
                        System.out.printf("• ID: %s | Name: %s | Duration: %d%n",
                                activity.getId(), activity.getName(), activity.getDuration());
                    }

                    System.out.println("\nEnter the ID of the activity to delay (or type '-1'/'exit' to finish): ");
                    String input = scanner.nextLine();

                    if (input.equals("-1") || input.equalsIgnoreCase("exit")) {
                        break;
                    }

                    Activity activity = delaySimulator.findActivityById(input);
                    if (activity == null) {
                        System.out.println("Invalid Activity ID. Please try again.");
                        continue;
                    }

                    System.out.printf("Enter delay time (in time units) for activity %s: ", activity.getId());
                    int delay = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character
                    delays.put(activity.getId(), delay);
                }

                delaySimulator.simulateProjectDelays(delays);
                break;

            case 30:
                CriticalPathIdentifierGraph criticalPathIdentifier = new CriticalPathIdentifierGraph();
                criticalPathIdentifier.identifyCriticalPath();
                break;
            case 31:
                ExportSchedule exporter = new ExportSchedule();
                exporter.exportToCsv("outFiles/schedule.csv");
                break;
            case 32:

                DisplayGraph displayGraph = new DisplayGraph();
                String dotFilePath = "outFiles/Graph3.dot";
                String svgFilePath = "outFiles/Graph3.svg";

                displayGraph.generateDotFile(dotFilePath);
                displayGraph.generateSVG(dotFilePath, svgFilePath);
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