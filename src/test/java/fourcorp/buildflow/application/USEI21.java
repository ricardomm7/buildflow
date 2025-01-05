package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

public class USEI21 {

    private ExportSchedule exportSchedule;

    @BeforeEach
    void setUp() {
        exportSchedule = new ExportSchedule();
    }

    // Testa a exportação com gráfico vazio
    @Test
    void testExportToCsv_emptyGraph() throws IOException {
        exportSchedule.setGraph(new ActivitiesGraph());
        String outputPath = "empty_test_schedule.csv";
        exportSchedule.exportToCsv(outputPath);

        File file = new File(outputPath);
        assertTrue(file.exists(), "CSV file should be created");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();
            assertEquals("act_id,cost,duration,es,ls,ef,lf,prev_act_id1,...,prev_act_idN", header);
            String nextLine = reader.readLine();
            assertNull(nextLine, "There should be no activities in the CSV");
        }

        file.delete();
    }

    // Testa a exportação quando há falha no cálculo de tempo
    @Test
    void testExportToCsv_whenTimeCalculationFails() {
        ActivityTimeCalculator faultyCalculator = new ActivityTimeCalculator() {
            @Override
            public void calculateTimes() throws RuntimeException {
                throw new RuntimeException("Time calculation failed");
            }
        };

        exportSchedule = new ExportSchedule() {
            @Override
            public void exportToCsv(String outputPath) throws IOException {
                timeCalculator = faultyCalculator;
                super.exportToCsv(outputPath);
            }
        };

        String outputPath = "faulty_schedule.csv";
        assertThrows(RuntimeException.class, () -> exportSchedule.exportToCsv(outputPath),
                "Expected RuntimeException due to faulty time calculation");
    }

    void testExportToCsv_withActivities() throws IOException {
        // Criando o gráfico de atividades
        ActivitiesGraph graph = new ActivitiesGraph();

        // Atividades no formato: act_id, act_descr, duration, duration_unit, cost, cost_unit, prev_act_id1, prev_act_id2
        // Corrigido a dependência para que B2 e B3 dependam de B1, mas B1 não dependa de nenhuma outra atividade
        Activity activity1 = new Activity("B1", "Inicio do Projeto", 3, "dias", 1500, "R$", asList());
        Activity activity2 = new Activity("B2", "Planejamento", 4, "dias", 2000, "R$", asList("B1"));
        Activity activity3 = new Activity("B3", "Análise de Riscos", 5, "dias", 1800, "R$", asList("B1"));

        // Adiciona as atividades no gráfico
        graph.addActivity(activity1);
        graph.addActivity(activity2);
        graph.addActivity(activity3);

        // Define o gráfico no exportSchedule
        exportSchedule.setGraph(graph);

        String outputPath = "activities_schedule.csv";
        exportSchedule.exportToCsv(outputPath);

        File file = new File(outputPath);
        assertTrue(file.exists(), "CSV file should be created");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();
            assertEquals("act_id,cost,duration,es,ls,ef,lf,prev_act_id1,...,prev_act_idN", header);

            // Verifica se as atividades B1, B2 e B3 estão presentes
            String line1 = reader.readLine();
            assertNotNull(line1, "There should be activity B1 in the CSV");
            assertTrue(line1.contains("B1"), "CSV should contain activity B1");

            String line2 = reader.readLine();
            assertNotNull(line2, "There should be activity B2 in the CSV");
            assertTrue(line2.contains("B2"), "CSV should contain activity B2");

            String line3 = reader.readLine();
            assertNotNull(line3, "There should be activity B3 in the CSV");
            assertTrue(line3.contains("B3"), "CSV should contain activity B3");
        }

        file.delete();
    }

}
