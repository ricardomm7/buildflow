package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.MachinesPerOperation;
import fourcorp.buildflow.repository.Repositories;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Reader {
    public static List<Product> products = new ArrayList<>();
    public static List<Workstation> machines = new ArrayList<>();

    //private static MachinesPerOperation machinesPerOperation = Repositories.getInstance().getMachinesPerOperation();

    public static void loadOperations(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = br.readLine()) != null) {
            String[] fields = line.split(",");
            String idItem = fields[0];
            PriorityOrder priorityOrder = getPriorityOrderFromValue(Integer.parseInt(fields[1]));
            LinkedList<Operation> operations = new LinkedList<>();
            for (int i = 2; i < fields.length; i++) {
                operations.add(new Operation(fields[i]));
            }
            products.add(new Product(idItem, priorityOrder, operations));
        }
        br.close();
    }


    public static void loadMachines(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String linha;
        while ((linha = br.readLine()) != null) {
            String[] campos = linha.split(",");
            Workstation maquina = new Workstation(campos[0], campos[1], Double.parseDouble(campos[2]));
            Operation operation = new Operation(campos[1]);
            //machinesPerOperation.create(maquina, operation);
            machines.add(maquina);
        }
        br.close();
    }

    private static PriorityOrder getPriorityOrderFromValue(int value) {
        switch (value) {
            case 1:
                return PriorityOrder.HIGH;
            case 2:
                return PriorityOrder.MEDIUM;
            case 3:
                return PriorityOrder.LOW;
            default:
                throw new IllegalArgumentException("Invalid priority value: " + value);
        }
    }
}
