package fourcorp.buildflow.repository;

public class Repositories {
    private static Repositories instance;
    private final ProductPriorityLine productPriorityLine;
    private final WorkstationsPerOperation workstationsPerOperation;

    public Repositories() {
        productPriorityLine = new ProductPriorityLine();
        workstationsPerOperation = new WorkstationsPerOperation();
    }

    public static Repositories getInstance() {
        if (instance == null) {
            synchronized (Repositories.class) {
                instance = new Repositories();
            }
        }
        return instance;
    }

    public static void setInstance(Repositories newInstance) {
        instance = newInstance;
    }

    public ProductPriorityLine getProductPriorityRepository() {
        return productPriorityLine;
    }

    public WorkstationsPerOperation getWorkstationsPerOperation() {
        return workstationsPerOperation;
    }
}