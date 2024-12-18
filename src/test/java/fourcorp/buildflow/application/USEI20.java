package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class USEI20 {

    @Test
    public void test_calculate_times_single_activity() {
        ActivitiesGraph graph = new ActivitiesGraph();

        Activity activity = new Activity("A1", "Single Task", 4, "days", 100.0, "USD", Collections.emptyList());
        graph.addActivity(activity);

        ActivityTimeCalculator calculator = new ActivityTimeCalculator();
        calculator.setGraph(graph);
        calculator.calculateTimes();

        assertEquals(0, activity.getEarlyStart());
        assertEquals(4, activity.getEarlyFinish());
        assertEquals(0, activity.getLateStart());
        assertEquals(4, activity.getLateFinish());
        assertEquals(4, calculator.getProjectDuration());
    }

    @Test
    public void test_calculate_times_with_multiple_activities() {
        ActivitiesGraph graph = new ActivitiesGraph();

        Activity a1 = new Activity("A1", "Task 1", 3, "days", 100.0, "USD", Collections.emptyList());
        Activity a2 = new Activity("A2", "Task 2", 4, "days", 200.0, "USD", List.of("A1"));
        Activity a3 = new Activity("A3", "Task 3", 2, "days", 150.0, "USD", Arrays.asList("A1", "A2"));

        graph.addActivity(a1);
        graph.addActivity(a2);
        graph.addActivity(a3);

        graph.addDependency(a1, a2);
        graph.addDependency(a1, a3);
        graph.addDependency(a2, a3);

        ActivityTimeCalculator calculator = new ActivityTimeCalculator();
        calculator.setGraph(graph);
        calculator.calculateTimes();

        assertEquals(0, a1.getEarlyStart());
        assertEquals(3, a1.getEarlyFinish());
        assertEquals(3, a2.getEarlyStart());
        assertEquals(7, a2.getEarlyFinish());
        assertEquals(7, a3.getEarlyStart());
        assertEquals(9, a3.getEarlyFinish());
    }

    @Test
    public void test_project_duration_calculation() {
        ActivitiesGraph graph = new ActivitiesGraph();

        Activity a1 = new Activity("A1", "Task 1", 5, "days", 100.0, "USD", Collections.emptyList());
        Activity a2 = new Activity("A2", "Task 2", 3, "days", 200.0, "USD", Collections.emptyList());

        graph.addActivity(a1);
        graph.addActivity(a2);

        ActivityTimeCalculator calculator = new ActivityTimeCalculator();
        calculator.setGraph(graph);
        calculator.calculateTimes();

        assertEquals(5, calculator.getProjectDuration());
    }

    @Test
    public void test_calculate_times_with_empty_graph() {
        ActivitiesGraph graph = new ActivitiesGraph();

        ActivityTimeCalculator calculator = new ActivityTimeCalculator();
        calculator.setGraph(graph);
        calculator.calculateTimes();

        assertEquals(0, calculator.getProjectDuration());
    }

    @Test
    public void test_calculate_times_no_dependencies() {
        ActivitiesGraph graph = new ActivitiesGraph();

        Activity a1 = new Activity("A1", "Task 1", 3, "days", 100.0, "USD", Collections.emptyList());
        Activity a2 = new Activity("A2", "Task 2", 4, "days", 200.0, "USD", Collections.emptyList());

        graph.addActivity(a1);
        graph.addActivity(a2);

        ActivityTimeCalculator calculator = new ActivityTimeCalculator();
        calculator.setGraph(graph);
        calculator.calculateTimes();

        assertEquals(0, a1.getEarlyStart());
        assertEquals(3, a1.getEarlyFinish());
        assertEquals(0, a2.getEarlyStart());
        assertEquals(4, a2.getEarlyFinish());
        assertEquals(4, calculator.getProjectDuration());
    }

    @Test
    public void test_slack_with_positive_value() {
        ActivitiesGraph graph = new ActivitiesGraph();

        // Criando atividades
        Activity a1 = new Activity("A1", "Start Task", 3, "days", 100.0, "USD", Collections.emptyList());
        Activity a2 = new Activity("A2", "Critical Task", 5, "days", 200.0, "USD", Arrays.asList("A1"));
        Activity a3 = new Activity("A3", "Non-Critical Task", 2, "days", 150.0, "USD", Arrays.asList("A1"));
        Activity a4 = new Activity("A4", "Final Task", 4, "days", 300.0, "USD", Arrays.asList("A2", "A3"));

        // Adicionando atividades ao grafo
        graph.addActivity(a1);
        graph.addActivity(a2);
        graph.addActivity(a3);
        graph.addActivity(a4);

        // Criando dependências
        graph.addDependency(a1, a2);
        graph.addDependency(a1, a3);
        graph.addDependency(a2, a4);
        graph.addDependency(a3, a4);

        // Calculando tempos
        ActivityTimeCalculator calculator = new ActivityTimeCalculator();
        calculator.setGraph(graph);
        calculator.calculateTimes();

        // Verificando slacks
        int slackA3 = a3.getLateStart() - a3.getEarlyStart();
        assertTrue(slackA3 > 0, "A3 deve ter slack positivo.");
        assertEquals(3, slackA3, "Slack esperado para A3 é 3.");

        // Verificando que A2 é crítico (slack = 0)
        int slackA2 = a2.getLateStart() - a2.getEarlyStart();
        assertEquals(0, slackA2, "A2 deve ser uma atividade crítica com slack 0.");
    }

    @Test
    public void test_slack_with_zero_for_critical_path() {
        ActivitiesGraph graph = new ActivitiesGraph();

        // Criando atividades
        Activity a1 = new Activity("A1", "Start Task", 3, "days", 100.0, "USD", Collections.emptyList());
        Activity a2 = new Activity("A2", "Critical Task A", 4, "days", 200.0, "USD", Arrays.asList("A1"));
        Activity a3 = new Activity("A3", "Critical Task B", 5, "days", 300.0, "USD", Arrays.asList("A2"));

        // Adicionando atividades ao grafo
        graph.addActivity(a1);
        graph.addActivity(a2);
        graph.addActivity(a3);

        // Criando dependências
        graph.addDependency(a1, a2);
        graph.addDependency(a2, a3);

        // Calculando tempos
        ActivityTimeCalculator calculator = new ActivityTimeCalculator();
        calculator.setGraph(graph);
        calculator.calculateTimes();

        // Verificando que todas as atividades no caminho crítico têm slack 0
        assertEquals(0, a1.getLateStart() - a1.getEarlyStart(), "A1 deve ter slack 0.");
        assertEquals(0, a2.getLateStart() - a2.getEarlyStart(), "A2 deve ter slack 0.");
        assertEquals(0, a3.getLateStart() - a3.getEarlyStart(), "A3 deve ter slack 0.");
    }


    @Test
    void testCalculateTimesWithFileInput() throws IOException {
        // Carrega atividades a partir de arquivo
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activities.csv");

        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();
        ActivityTimeCalculator calculator = new ActivityTimeCalculator();

        // Calcula os tempos
        calculator.calculateTimes();

        // Verifica os tempos para uma atividade específica
        Activity a4 = graph.findActivityById("A4");
        assertNotNull(a4);
        assertEquals(11, a4.getEarlyStart()); // ES esperado para A4
        assertEquals(18, a4.getEarlyFinish()); // EF esperado para A4

        // Verifica a duração total do projeto
        assertEquals(68, calculator.getProjectDuration());
    }

    @Test
    void testCalculateTimesForCriticalPathActivities() throws IOException {
        // Carrega atividades do arquivo
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activities.csv");

        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();
        ActivityTimeCalculator calculator = new ActivityTimeCalculator();

        // Calcula os tempos
        calculator.calculateTimes();

        // Verifica os tempos para atividades críticas
        Activity a1 = graph.findActivityById("A1");
        Activity a16 = graph.findActivityById("A16");

        assertNotNull(a1);
        assertEquals(0, a1.getEarlyStart()); // ES esperado
        assertEquals(5, a1.getEarlyFinish()); // EF esperado
        assertEquals(0, a1.getLateStart()); // LS esperado
        assertEquals(5, a1.getLateFinish()); // LF esperado
        assertEquals(0, a1.getLateStart() - a1.getEarlyStart()); // Slack esperado

        assertNotNull(a16);
        assertEquals(64, a16.getEarlyStart());
        assertEquals(68, a16.getEarlyFinish());
        assertEquals(64, a16.getLateStart());
        assertEquals(68, a16.getLateFinish());
        assertEquals(0, a16.getLateStart() - a16.getEarlyStart());
    }

    @Test
    void testCalculateTimesForNonCriticalActivities() throws IOException {
        // Carrega atividades do arquivo
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activities.csv");

        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();
        ActivityTimeCalculator calculator = new ActivityTimeCalculator();

        // Calcula os tempos
        calculator.calculateTimes();

        // Verifica os tempos para atividades não críticas
        Activity a6 = graph.findActivityById("A6");
        Activity a11 = graph.findActivityById("A11");

        assertNotNull(a6);
        assertEquals(18, a6.getEarlyStart()); // ES esperado
        assertEquals(23, a6.getEarlyFinish()); // EF esperado
        assertEquals(25, a6.getLateStart()); // LS esperado
        assertEquals(30, a6.getLateFinish()); // LF esperado
        assertEquals(7, a6.getLateStart() - a6.getEarlyStart()); // Slack esperado

        assertNotNull(a11);
        assertEquals(33, a11.getEarlyStart());
        assertEquals(37, a11.getEarlyFinish());
        assertEquals(37, a11.getLateStart());
        assertEquals(41, a11.getLateFinish());
        assertEquals(4, a11.getLateStart() - a11.getEarlyStart());
    }

    @Test
    void testCalculateTotalProjectDuration() throws IOException {
        // Carrega atividades do arquivo
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activities.csv");

        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();
        ActivityTimeCalculator calculator = new ActivityTimeCalculator();

        // Calcula os tempos
        calculator.calculateTimes();

        // Verifica a duração total do projeto
        assertEquals(68, calculator.getProjectDuration(), "Total project duration should be 68 time units.");
    }


    @Test
    void testCalculateTimesForActivitiesWithMultipleDependencies() throws IOException {
        // Carrega atividades do arquivo
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activities.csv");

        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();
        ActivityTimeCalculator calculator = new ActivityTimeCalculator();

        // Calcula os tempos
        calculator.calculateTimes();

        // Verifica atividades com múltiplas dependências
        Activity a4 = graph.findActivityById("A4");

        assertNotNull(a4);
        assertEquals(11, a4.getEarlyStart()); // ES esperado
        assertEquals(18, a4.getEarlyFinish()); // EF esperado
        assertEquals(11, a4.getLateStart()); // LS esperado
        assertEquals(18, a4.getLateFinish()); // LF esperado
        assertEquals(0, a4.getLateStart() - a4.getEarlyStart()); // Slack esperado
    }



    @Test
    void testCalculateTimesWithNoDependencies() throws IOException {
        // Carrega atividades do arquivo
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activities.csv");

        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();
        ActivityTimeCalculator calculator = new ActivityTimeCalculator();

        // Calcula os tempos
        calculator.calculateTimes();

        // Verifica atividade sem dependências
        Activity a1 = graph.findActivityById("A1");

        assertNotNull(a1);
        assertEquals(0, a1.getEarlyStart());
        assertEquals(5, a1.getEarlyFinish());
        assertEquals(0, a1.getLateStart());
        assertEquals(5, a1.getLateFinish());
        assertEquals(0, a1.getLateStart() - a1.getEarlyStart()); // Slack esperado
    }

    @Test
    void testSlackWithDelays() throws IOException {
        // Carrega atividades do arquivo
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activities.csv");
        DisplayGraph d = new DisplayGraph();
        String dotFilePath = "outFiles/GraphTest.dot";
        String svgFilePath = "outFiles/GraphTest.svg";

        d.generateDotFile(dotFilePath);
        d.generateSVG(dotFilePath, svgFilePath);
        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();
        ActivityTimeCalculator calculator = new ActivityTimeCalculator();

        // Calcula os tempos originais
        calculator.calculateTimes();

        // Aplica um delay em uma atividade para criar slack
        Activity delayedActivity = graph.findActivityById("A6");
        assertNotNull(delayedActivity);
        delayedActivity.setDuration(delayedActivity.getDuration() + 5); // Adiciona 5 unidades de tempo à duração

        // Recalcula os tempos após o delay
        calculator.calculateTimes();

        // Verifica o Slack para a atividade "A8", que depende de "A6"
        Activity a8 = graph.findActivityById("A8");
        assertNotNull(a8);
        assertEquals(2, a8.getLateStart() - a8.getEarlyStart(), "Slack esperado para A8 deve ser 2.");

        // Verifica os tempos para garantir consistência
        assertEquals(28, a8.getEarlyStart()); // ES esperado
        assertEquals(35, a8.getEarlyFinish()); // EF esperado
        assertEquals(30, a8.getLateStart()); // LS esperado
        assertEquals(37, a8.getLateFinish()); // LF esperado
        assertEquals(2, a8.getLateStart() - a8.getEarlyStart(), "Slack esperado para A8 deve ser 2.");
    }

    @Test
    void testDelayInCriticalPathActivity() throws IOException {
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activities.csv");
        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();
        ActivityTimeCalculator calculator = new ActivityTimeCalculator();

        // Calcula os tempos originais
        calculator.calculateTimes();
        int originalProjectDuration = calculator.getProjectDuration();

        // Aplica um atraso em A5
        Activity a5 = graph.findActivityById("A5");
        assertNotNull(a5);
        a5.setDuration(a5.getDuration() + 3); // Adiciona 3 dias ao Design Arquitetural

        // Recalcula os tempos após o atraso
        calculator.calculateTimes();

        // Verifica o novo tempo do projeto
        assertEquals(originalProjectDuration + 3, calculator.getProjectDuration(),
                "O projeto deve ser estendido pelo atraso no caminho crítico.");

        // Verifica os tempos para atividades dependentes de A5 (A7 e A8)
        Activity a7 = graph.findActivityById("A7");
        Activity a8 = graph.findActivityById("A8");
        assertNotNull(a7);
        assertNotNull(a8);

        assertEquals(29, a7.getEarlyStart());
        assertEquals(39, a7.getEarlyFinish());
        assertEquals(29, a8.getEarlyStart());
        assertEquals(36, a8.getEarlyFinish());
    }
}
