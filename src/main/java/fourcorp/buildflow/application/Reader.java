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

/**
 * Reader is an abstract utility class responsible for loading data from external files
 * to populate the application's repositories for product priorities and workstation operations.
 * It provides methods for reading and parsing data from CSV files to create products and workstations.
 */
public abstract class Reader {
    public static ProductPriorityLine p = Repositories.getInstance().getProductPriorityRepository();
    public static WorkstationsPerOperation w = Repositories.getInstance().getWorkstationsPerOperation();

    /**
     * Loads product data from a specified file and populates the product priority repository.
     * Each line in the file should represent a product, including its ID, priority, and a list of operations.
     *
     * @param filePath the path to the file containing product data
     * @throws IOException if an I/O error occurs while reading the file
     */
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

    /**
     * Loads workstation data from a specified file and populates the workstations per operation repository.
     * Each line in the file should represent a workstation, including its ID, associated operation, and capacity.
     *
     * @param filePath the path to the file containing workstation data
     * @throws IOException if an I/O error occurs while reading the file
     */
    public static void loadMachines(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        br.readLine();
        String linha;
        while ((linha = br.readLine()) != null) {
            String[] campos = linha.split(";");
            Workstation maquina = new Workstation(campos[0], Integer.parseInt(campos[2]));
            Operation operation = new Operation(campos[1]);
            w.create(maquina, operation);
        }
        br.close();
    }

    /**
     * Maps a string value to the corresponding PriorityOrder enumeration value.
     *
     * @param value the string representation of the priority ("HIGH", "NORMAL", or "LOW")
     * @return the corresponding PriorityOrder enum value
     * @throws IllegalArgumentException if the input string does not match any valid priority
     */
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
