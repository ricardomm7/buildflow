package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.ProductPriorityLine;
import fourcorp.buildflow.repository.Repositories;
import fourcorp.buildflow.repository.WorkstationsPerOperation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public abstract class Reader {
    public static ProductPriorityLine p = Repositories.getInstance().getProductPriorityRepository();
    public static WorkstationsPerOperation w = Repositories.getInstance().getWorkstationsPerOperation();

    //public static List<Product> products = new ArrayList<>();
    //public static List<Workstation> machines = new ArrayList<>();

    //private static MachinesPerOperation machinesPerOperation = Repositories.getInstance().getMachinesPerOperation();

    public static void loadOperations(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        br.readLine();
        String line;
        while ((line = br.readLine()) != null) {
            String[] fields = line.split(";");
            String idItem = fields[0];
            PriorityOrder priorityOrder = getPriorityOrderFromValue(fields[1]);
            LinkedList<Operation> operations = new LinkedList<>();
            for (int i = 2; i < fields.length; i++) {
                operations.add(new Operation(fields[i]));
            }
            p.create(new Product(idItem, operations), priorityOrder);
        }
        br.close();
    }


    public static void loadMachines(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        br.readLine();
        String linha;
        while ((linha = br.readLine()) != null) {
            String[] campos = linha.split(";");
            Workstation maquina = new Workstation(campos[0], Double.parseDouble(campos[2]));
            Operation operation = new Operation(campos[1]);
            //machinesPerOperation.create(maquina, operation);
            w.create(maquina, operation);
        }
        br.close();
    }

    private static PriorityOrder getPriorityOrderFromValue(String value) {
        switch (value.toUpperCase()) {
            case "HIGH":
                return PriorityOrder.HIGH;
            case "NORMAL":
                return PriorityOrder.NORMAL;
            case "LOW":
                return PriorityOrder.LOW;
            default:
                throw new IllegalArgumentException("Invalid priority value: " + value);
        }
    }
}
