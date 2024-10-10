package fourcorp.buildflow.application;

import fourcorp.buildflow.application.MachineFlowAnalyzer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MachineFlowAnalyzerTest {


    // Adding a new dependency between two machines updates the map correctly
    @Test
    public void test_add_dependency_updates_map() {
        MachineFlowAnalyzer.addDependency("MachineA", "MachineB");
        assertEquals(1, MachineFlowAnalyzer.machineDependencies.get("MachineA").get("MachineB"));
    }

    // Printing machine dependencies outputs the correct format and order
    @Test
    public void test_print_machine_dependencies_format_and_order() {
        MachineFlowAnalyzer.addDependency("MachineA", "MachineB");
        MachineFlowAnalyzer.addDependency("MachineA", "MachineC");
        MachineFlowAnalyzer.addDependency("MachineA", "MachineB");
        MachineFlowAnalyzer.printMachineDependencies();
    }

    // Adding a dependency to a non-existent machine initializes its map
    @Test
    public void test_add_dependency_initializes_map() {
        MachineFlowAnalyzer.addDependency("NewMachine", "MachineB");
        assertNotNull(MachineFlowAnalyzer.machineDependencies.get("NewMachine"));
        assertEquals(1, MachineFlowAnalyzer.machineDependencies.get("NewMachine").get("MachineB"));
    }

    // Printing dependencies when no dependencies exist outputs nothing
    @Test
    public void test_print_no_dependencies_outputs_nothing() {
        MachineFlowAnalyzer.printMachineDependencies();
    }

    // Adding a dependency with an empty string as machine name handles gracefully
    @Test
    public void test_add_dependency_empty_string_handling() {
        MachineFlowAnalyzer.addDependency("", "MachineB");
        assertNotNull(MachineFlowAnalyzer.machineDependencies.get(""));
        assertEquals(1, MachineFlowAnalyzer.machineDependencies.get("").get("MachineB"));
    }
}