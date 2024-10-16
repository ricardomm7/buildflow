package fourcorp.buildflow.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class WorkstationFlowAnalyzerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        MachineFlowAnalyzer.machineDependencies.clear();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void test_add_dependency_updates_map() {
        MachineFlowAnalyzer.addDependency("MachineA", "MachineB");
        assertEquals(1, MachineFlowAnalyzer.machineDependencies.get("MachineA").get("MachineB"));
    }

    @Test
    public void test_print_machine_dependencies_format_and_order() {
        MachineFlowAnalyzer.addDependency("MachineA", "MachineB");
        MachineFlowAnalyzer.addDependency("MachineA", "MachineC");
        MachineFlowAnalyzer.addDependency("MachineA", "MachineB");
        MachineFlowAnalyzer.printMachineDependencies();

        String expectedOutput = "MachineA : [(MachineB,2),(MachineC,1)]" + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void test_add_dependency_initializes_map() {
        MachineFlowAnalyzer.addDependency("NewMachine", "MachineB");
        assertNotNull(MachineFlowAnalyzer.machineDependencies.get("NewMachine"));
        assertEquals(1, MachineFlowAnalyzer.machineDependencies.get("NewMachine").get("MachineB"));
    }

    @Test
    public void test_print_no_dependencies_outputs_nothing() {
        MachineFlowAnalyzer.printMachineDependencies();
        assertEquals("", outContent.toString());
    }

    @Test
    public void test_add_dependency_empty_string_handling() {
        MachineFlowAnalyzer.addDependency("", "MachineB");
        assertNotNull(MachineFlowAnalyzer.machineDependencies.get(""));
        assertEquals(1, MachineFlowAnalyzer.machineDependencies.get("").get("MachineB"));
    }

    @Test
    void testAddDependency() {
        MachineFlowAnalyzer.addDependency("Machine1", "Machine2");
        MachineFlowAnalyzer.addDependency("Machine1", "Machine2");
        MachineFlowAnalyzer.addDependency("Machine1", "Machine3");

        Map<String, Map<String, Integer>> dependencies = MachineFlowAnalyzer.machineDependencies;

        assertTrue(dependencies.containsKey("Machine1"));
        assertEquals(2, dependencies.get("Machine1").get("Machine2"));
        assertEquals(1, dependencies.get("Machine1").get("Machine3"));
    }

    @Test
    void testAddDependencyWithNewMachine() {
        MachineFlowAnalyzer.addDependency("Machine4", "Machine5");

        Map<String, Map<String, Integer>> dependencies = MachineFlowAnalyzer.machineDependencies;

        assertTrue(dependencies.containsKey("Machine4"));
        assertEquals(1, dependencies.get("Machine4").get("Machine5"));
    }

    @Test
    void testPrintMachineDependencies() {
        MachineFlowAnalyzer.addDependency("MachineA", "MachineB");
        MachineFlowAnalyzer.addDependency("MachineA", "MachineC");
        MachineFlowAnalyzer.addDependency("MachineA", "MachineB");

        MachineFlowAnalyzer.printMachineDependencies();

        String expectedOutput = "MachineA : [(MachineB,2),(MachineC,1)]" + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void testPrintMachineDependenciesWithMultipleMachines() {
        MachineFlowAnalyzer.addDependency("Machine1", "Machine2");
        MachineFlowAnalyzer.addDependency("Machine1", "Machine3");
        MachineFlowAnalyzer.addDependency("Machine2", "Machine3");

        MachineFlowAnalyzer.printMachineDependencies();

        String expectedOutput = "Machine2 : [(Machine3,1)]" + System.lineSeparator() +
                "Machine1 : [(Machine2,1),(Machine3,1)]" + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void testPrintMachineDependenciesWithNoDependendencies() {
        MachineFlowAnalyzer.printMachineDependencies();

        assertEquals("", outContent.toString());
    }

    @BeforeEach
    public void tearDown() {
        System.setOut(originalOut);
    }
}
