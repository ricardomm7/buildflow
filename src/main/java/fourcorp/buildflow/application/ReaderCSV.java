package fourcorp.buildflow.application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class ReaderCSV {
    private static List<String> productIDs = new ArrayList<>();
    private static List<String> partNumbers = new ArrayList<>();
    private static List<String> descriptions = new ArrayList<>();
    private static List<Integer> quantities = new ArrayList<>();

    public static void saveInformation(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        br.readLine();
        String line;
        boolean isFirstLine = true;
        while ((line = br.readLine()) != null) {
            if (isFirstLine) {
                isFirstLine = false;
                continue;
            }
            String[] columns = line.split(";");
            if (columns.length < 4) {
                continue; // Skip any incomplete lines
            }
            productIDs.add(columns[0]);
            partNumbers.add(columns[1]);
            descriptions.add(columns[2]);
            quantities.add(Integer.parseInt(columns[3]));
        }
    }
}
