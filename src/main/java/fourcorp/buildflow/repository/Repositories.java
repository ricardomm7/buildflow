package fourcorp.buildflow.repository;

public class Repositories {
    private static Repositories instance;
    private final MachinesPerOperation machinesPerOperation;

    public Repositories() {
        machinesPerOperation = new MachinesPerOperation();
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

    public MachinesPerOperation getMachinesPerOperation() {
        return machinesPerOperation;
    }

}