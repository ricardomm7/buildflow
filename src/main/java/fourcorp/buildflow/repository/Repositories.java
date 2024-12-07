package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.Activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton class that provides centralized access to various repositories used in the application.
 * It manages instances of repositories for product priority lines and workstations per operation.
 */
public class Repositories {
    private static Repositories instance;
    private final ProductPriorityLine productPriorityLine;
    private final WorkstationsPerOperation workstationsPerOperation;
    private static ProductionTree productionTree;
    private static MaterialQuantityBST materialBST;
    private static ActivitiesGraph activitiesGraph;


    /**
     * Private constructor to initialize the repositories.
     * This constructor is called only once to create the singleton instance.
     */
    public Repositories() {
        productPriorityLine = new ProductPriorityLine();
        workstationsPerOperation = new WorkstationsPerOperation();
        productionTree = new ProductionTree();
        materialBST = new MaterialQuantityBST();
        activitiesGraph = new ActivitiesGraph();
    }

    /**
     * Returns the singleton instance of the Repositories class.
     *
     * @return the single instance of the Repositories
     */
    public static Repositories getInstance() {
        if (instance == null) {
            synchronized (Repositories.class) {
                instance = new Repositories();
            }
        }
        return instance;
    }

    /**
     * Sets a new instance of the Repositories class.
     * This can be used for testing or resetting the singleton.
     *
     * @param newInstance the new instance to set
     */
    public static void setInstance(Repositories newInstance) {
        instance = newInstance;
    }

    /**
     * Retrieves the product priority line repository.
     *
     * @return the product priority line repository
     */
    public ProductPriorityLine getProductPriorityRepository() {
        return productPriorityLine;
    }

    /**
     * Retrieves the workstations per operation repository.
     *
     * @return the workstations per operation repository
     */
    public WorkstationsPerOperation getWorkstationsPerOperation() {
        return workstationsPerOperation;
    }

    /**
     * Sets the production tree repository.
     *
     * @param productionTree the production tree to set
     */
    public static void setProductionTree(ProductionTree productionTree) {
        Repositories.productionTree = productionTree;
    }

    /**
     * Retrieves the production tree repository.
     *
     * @return the production tree
     */
    public ProductionTree getProductionTree() {
        return productionTree;
    }

    /**
     * Gets material bst.
     *
     * @return the material bst
     */
    public MaterialQuantityBST getMaterialBST() {
        return materialBST;
    }

    public ActivitiesGraph getActivitiesGraph(){
        return activitiesGraph;
    }

    /**
     * Clear the production tree.
     */
    public static void clear() {
        productionTree.clear();
        materialBST.clear();
    }

}