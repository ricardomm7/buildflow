package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.MapLinked;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Reader {
    public static Map<String, Product> products = new HashMap<>();

    public static MapLinked<Workstation, String, String> machinesPerOperation = new MapLinked<>();

    public static void loadOperations(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = br.readLine()) != null) {
            String[] fields = line.split(",");
            String idItem = fields[0];
            int priority = Integer.parseInt(fields[1]);
            PriorityOrder priorityOrder = getPriorityOrderFromValue(priority);

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
            String idMaquina = campos[0];
            String operacao = campos[1];
            int tempo = Integer.parseInt(campos[2]);
            Workstation maquina = new Workstation(idMaquina, operacao, tempo);

            machinesPerOperation.newItem(maquina, operacao);
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
