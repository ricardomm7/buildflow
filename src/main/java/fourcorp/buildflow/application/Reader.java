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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Reader {
    public static Map<String, Product> products = new HashMap<>();

    private static MachinesPerOperation machinesPerOperation = Repositories.getInstance().getMachinesPerOperation();

    public static void loadOperations(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = br.readLine()) != null) {
            String[] fields = line.split(",");
            String idItem = fields[0];
            PriorityOrder priorityOrder = getPriorityOrderFromValue(Integer.parseInt(fields[1]));
            LinkedList<String> operations = new LinkedList<>(Arrays.asList(Arrays.copyOfRange(fields, 2, fields.length)));
            products.put(idItem, new Product(idItem, priorityOrder, operations));
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
            machinesPerOperation.create(maquina, operation);
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
