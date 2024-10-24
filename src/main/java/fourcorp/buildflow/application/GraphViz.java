package fourcorp.buildflow.application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public abstract class GraphViz {
    private static List<String> items = new ArrayList<>();
    private static List<String> subAssemblies = new ArrayList<>();
    private static List<String> components = new ArrayList<>();
    private static List<Integer> quantities = new ArrayList<>();


    //private static List<String> productIDs = new ArrayList<>();
    //private static List<String> partNumbers = new ArrayList<>();
    //private static List<String> descriptions = new ArrayList<>();
    //private static List<Integer> quantities = new ArrayList<>();

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
                continue;
            }
            items.add(columns[0]);
            subAssemblies.add(columns[1]);
            components.add(columns[2]);
            quantities.add(Integer.parseInt(columns[3]));
            //productIDs.add(columns[0]);
            //partNumbers.add(columns[1]);
            //descriptions.add(columns[2]);
            //quantities.add(Integer.parseInt(columns[3]));
        }
    }

    public static void generateGraph(String outputFilePath) throws IOException {
        Set<String> edges = new HashSet<>();
        Map<String, String> componentToSubAssemblyMap = new HashMap<>();
        Map<String, String> subAssemblyToItemMap = new HashMap<>();
        Map<String, Integer> componentQuantities = new HashMap<>();

        StringBuilder dotFileContent = new StringBuilder();
        dotFileContent.append("digraph G {\n");
        dotFileContent.append("    node [shape=box];\n");

        for (int i = 0; i < items.size(); i++) {
            String item = items.get(i);
            String subAssembly = subAssemblies.get(i);
            String component = components.get(i);
            int quantity = quantities.get(i);

            String componentNode = "\"" + component + "\"";
            dotFileContent.append("    ").append(componentNode).append(";\n");

            componentToSubAssemblyMap.put(component, subAssembly);
            subAssemblyToItemMap.put(subAssembly, item);
            componentQuantities.put(component, quantity);
        }

        for (Map.Entry<String, String> entry : componentToSubAssemblyMap.entrySet()) {
            String component = entry.getKey();
            String subAssembly = entry.getValue();
            int quantity = componentQuantities.get(component);

            String componentNode = "\"" + component + "\"";
            String subAssemblyNode = "\"" + subAssembly + "\"";

            String edge = String.format("    %s -> %s [label=\"%d\"];\n", subAssemblyNode, componentNode, quantity);
            edges.add(edge);
        }

        for (Map.Entry<String, String> entry : subAssemblyToItemMap.entrySet()) {
            String subAssembly = entry.getKey();
            String item = entry.getValue();

            String subAssemblyNode = "\"" + subAssembly + "\"";
            String itemNode = "\"" + item + "\"";

            String edge = String.format("    %s -> %s;\n", itemNode, subAssemblyNode);
            edges.add(edge);
        }

        for (String edge : edges) {
            dotFileContent.append(edge);
        }
        dotFileContent.append("}\n");

        FileWriter fileWriter = new FileWriter(outputFilePath);
        fileWriter.write(dotFileContent.toString());
        fileWriter.close();
    }



    public static void generateProductComponentGraph() {
        try {
            String dotFilePath = "outFiles/Graph.dot";
            String outputImagePath = "outFiles/Graph.png";
            GraphViz.generateGraph(dotFilePath);
            System.out.println("Graph .dot file generated successfully: " + dotFilePath);
            String command = "dot -Tpng " + dotFilePath + " -o " + outputImagePath;
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Graph image generated successfully: " + outputImagePath);
            } else {
                System.out.println("Error: Failed to generate the graph image. Exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error generating graph: " + e.getMessage());
        }
    }


   /*public static void generateGraph(String outputFilePath) throws IOException {
        Set<String> edges = new HashSet<>();
        StringBuilder dotFileContent = new StringBuilder();
        dotFileContent.append("digraph G {\n");
        dotFileContent.append("    node [shape=box];\n");

        for (int i = 0; i < productIDs.size(); i++) {
            String productID = productIDs.get(i);
            String partNumber = partNumbers.get(i);
            String description = descriptions.get(i);
            int quantity = quantities.get(i);

            String partNode = String.format("\"%s\\n%s\\n(%d)\"", partNumber, description, quantity);
            dotFileContent.append("    ").append(partNode).append(";\n");

            String edge = String.format("    \"%s\" -> %s;\n", productID, partNode);
            edges.add(edge);
        }
        for (String edge : edges) {
            dotFileContent.append(edge);
        }
        dotFileContent.append("}\n");
        FileWriter fileWriter = new FileWriter(outputFilePath);
        fileWriter.write(dotFileContent.toString());
        fileWriter.close();
    }

    public static void generateProductComponentGraph() {
        try {
            String dotFilePath = "outFiles/BOMGraph.dot";
            String outputImagePath = "outFiles/BOMGraph.png";
            GraphViz.generateGraph(dotFilePath);
            System.out.println("Graph .dot file generated successfully: " + dotFilePath);
            String command = "dot -Tpng " + dotFilePath + " -o " + outputImagePath;
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Graph image generated successfully: " + outputImagePath);
            } else {
                System.out.println("Error: Failed to generate the graph image. Exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error generating graph: " + e.getMessage());
        }
    }*/
}

