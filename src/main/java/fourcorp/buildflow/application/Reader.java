package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Machine;
import fourcorp.buildflow.domain.Product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Reader {
    static Map<String, Product> products = new HashMap<>();

    static Map<String, LinkedList<Machine>> machinesPerOperation = new HashMap<>();

    public static void loadOperations(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = br.readLine()) != null) {
            String[] fields = line.split(",");
            String idItem = fields[1];
            int priority = Integer.parseInt(fields[2]);
            LinkedList<String> operations = new LinkedList<>(Arrays.asList(Arrays.copyOfRange(fields, 3, fields.length)));
            products.put(idItem, new Product(idItem, priority, operations));
        }
        br.close();
    }


    public static void loadMachines(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String linha;
        while ((linha = br.readLine()) != null) {
            String[] campos = linha.split(",");
            String idMaquina = campos[1];
            String operacao = campos[2];
            int tempo = Integer.parseInt(campos[3]);
            Machine maquina = new Machine(idMaquina, operacao, tempo);

            machinesPerOperation.computeIfAbsent(operacao, k -> new LinkedList<>()).add(maquina);
        }
        br.close();
    }
}
