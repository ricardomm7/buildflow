package fourcorp.buildflow.application;

import fourcorp.buildflow.repository.Repositories;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class OracleDataExporter {
    public static final DisplayProductionTree ptVisualizer = new DisplayProductionTree();
    private static final String DB_URL = "jdbc:oracle:thin:@//localhost:1521/XEPDB1"; // Atualize conforme necessário
    private static final String DB_USER = "fourcorp";
    private static final String DB_PASSWORD = "1234";

    public static void start(Scanner scanner) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Conectado ao banco de dados Oracle.");

            while (true) {
                System.out.println("Digite o ID ou Nome do produto (ou 'sair' para voltar ao menu): ");
                String userInput = scanner.nextLine().trim();

                if (userInput.equalsIgnoreCase("sair")) {
                    System.out.println("Voltando ao menu principal...");
                    return;
                }

                // Buscar o ID do produto com base no nome ou ID
                String productId = fetchProductId(connection, userInput);
                if (productId == null) {
                    System.err.println("Produto não encontrado! Tente novamente.");
                    continue; // Permite nova entrada
                }

                System.out.println("Produto encontrado: " + productId);

                // Gerar arquivos CSV apenas para o produto especificado
                exportItems(connection, productId);
                exportBOO(connection, productId);
                exportOperations(connection, productId);
                return;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro ao escrever arquivos CSV: " + e.getMessage());
        }
    }

    private static String fetchProductId(Connection connection, String userInput) throws SQLException {
        String sql = "SELECT Part_ID FROM Product WHERE Part_ID = ? OR Name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userInput);
            stmt.setString(2, userInput);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Part_ID");
                }
            }
        }
        return null;
    }

    private static void exportItems(Connection connection, String productId) throws SQLException, IOException {
        String sql = """
                SELECT DISTINCT Part_ID, Description 
                FROM Part 
                WHERE Part_ID = ? OR Part_ID IN (
                    SELECT Part_ID 
                    FROM Operation_Input 
                    WHERE Operation_ID IN (
                        SELECT Operation_ID FROM Operation WHERE Product_ID = ?
                    )
                )
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, productId);
            stmt.setString(2, productId);

            try (ResultSet rs = stmt.executeQuery();
                 FileWriter writer = new FileWriter("textFiles/itemsLapr.csv")) {

                writer.write("id_item;item_name\n");
                while (rs.next()) {
                    String idItem = rs.getString("Part_ID");
                    String itemName = rs.getString("Description");
                    writer.write(idItem + ";" + itemName + "\n");
                }
                System.out.println("Arquivo items.csv gerado com sucesso.");
            }
        }
    }

    private static void exportBOO(Connection connection, String productId) throws SQLException, IOException {
        String sql = """
                SELECT 
                    o.Operation_ID AS op_id,
                    o.Product_ID AS item_id,
                    oo.Quantity AS item_qtd,
                    o.NextOperation_ID AS predecessor_id,
                    oi.Part_ID AS input_item_id,
                    oi.Quantity AS input_quantity
                FROM 
                    Operation o
                LEFT JOIN 
                    Operation_Output oo ON o.Operation_ID = oo.Operation_ID
                LEFT JOIN 
                    Operation_Input oi ON o.Operation_ID = oi.Operation_ID
                WHERE 
                    o.Product_ID = ?
                ORDER BY 
                    o.Operation_ID, oi.Part_ID
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, productId);

            try (ResultSet rs = stmt.executeQuery();
                 FileWriter writer = new FileWriter("textFiles/boo_v2Lapr.csv")) {

                writer.write("op_id;item_id;item_qtd;(;op1;op_qtd1;op2;op_qtd2;opN;op_qtdN;);(;item_id1;item_qtd1;item_id2;item_qtd2;item_idN;item_qtdN;)\n");

                String lastOpId = "";
                String currentItemId = "";
                double currentItemQtd = 0.0;
                StringBuilder predecessors = new StringBuilder();
                StringBuilder inputs = new StringBuilder();

                while (rs.next()) {
                    String opId = rs.getString("op_id");
                    String itemId = rs.getString("item_id");
                    double itemQtd = rs.getDouble("item_qtd");
                    String predecessorId = rs.getString("predecessor_id");
                    String inputItemId = rs.getString("input_item_id");
                    Double inputQuantity = rs.getDouble("input_quantity");

                    // Se mudarmos de operação, escreve a linha acumulada e reinicia os buffers
                    if (!opId.equals(lastOpId) && !lastOpId.isEmpty()) {
                        writer.write(String.format("%s;%s;%.3f;(;%s);(;%s)\n",
                                lastOpId,
                                currentItemId,
                                currentItemQtd,
                                padSection(predecessors.toString()),
                                padSection(inputs.toString())));

                        // Reinicia para a nova operação
                        predecessors = new StringBuilder();
                        inputs = new StringBuilder();
                    }

                    // Atualiza os dados da operação atual
                    lastOpId = opId;
                    currentItemId = itemId;
                    currentItemQtd = itemQtd;

                    // Adiciona predecessor se existir
                    if (predecessorId != null) {
                        predecessors.append(predecessorId).append(";1;");
                    }

                    // Adiciona entrada de material se existir
                    if (inputItemId != null) {
                        inputs.append(inputItemId).append(";").append(String.format("%.3f", inputQuantity)).append(";");
                    }
                }

                // Escreve a última operação, se necessário
                if (!lastOpId.isEmpty()) {
                    writer.write(String.format("%s;%s;%.3f;(;%s);(;%s)\n",
                            lastOpId,
                            currentItemId,
                            currentItemQtd,
                            padSection(predecessors.toString()),
                            padSection(inputs.toString())));
                }

                System.out.println("Arquivo boo.csv gerado com sucesso no formato corrigido.");
            }
        }
    }

    /**
     * Garante que uma seção contenha exatamente 7 delimitadores ";".
     * Se o número de entradas for menor, preenche com espaços extras.
     */
    private static String padSection(String section) {
        int currentDelimiterCount = (int) section.chars().filter(ch -> ch == ';').count();
        int missingDelimiters = 7 - currentDelimiterCount;
        if (missingDelimiters > 0) {
            section += ";".repeat(missingDelimiters);
        }
        return section;
    }


    private static void exportOperations(Connection connection, String productId) throws SQLException, IOException {
        String sql = """
                SELECT DISTINCT Operation_ID AS op_id, Designation 
                FROM Operation 
                WHERE Product_ID = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, productId);

            try (ResultSet rs = stmt.executeQuery();
                 FileWriter writer = new FileWriter("textFiles/operationsLapr.csv")) {

                writer.write("op_id;op_name\n");
                while (rs.next()) {
                    String opId = rs.getString("op_id");
                    String opName = rs.getString("Designation");
                    writer.write(opId + ";" + opName + "\n");
                }
                System.out.println("Arquivo operations.csv gerado com sucesso.");
            }
        }
    }
}
