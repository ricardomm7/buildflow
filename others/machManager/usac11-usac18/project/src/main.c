#include <stdio.h>
#include <stdlib.h>
#include "../include/plant_management.h"
#include "../include/machine.h"
#include "../include/operation.h"


int main() {
    Machine *machines = loadMachinesFromFile("data/machines.txt");
    Operation *operations = NULL;
    int operationCount = 0;

    loadOperationsFromFile("data/operations.txt", &operations, &operationCount);

    int choice;
    do {
        printf("\nBUILDFLOW Mach Manager Menu\n");
        printf("1. List Machines\n");
        printf("2. Add Machine\n");
        printf("3. Remove Machine\n");
        printf("4. Assign Operation to Machine\n");
        printf("5. Save Machines\n");
        printf("6. Save Operations\n");
        printf("7. Create New Operation\n");
        printf("8. Export Machine Operations to CSV\n");
        printf("0. Exit\n");
        printf("Choose an option: ");
        scanf("%d", &choice);

        switch(choice) {
            case 1: listMachines(machines); break;
            case 2: addMachine(&machines); break;
            case 3: {
                int id;
                printf("Enter Machine ID to remove: ");
                scanf("%d", &id);
                removeMachine(&machines, id);
                break;
            }
            case 4: assignOperationToMachine(machines, operations, operationCount); break;
            case 5: saveMachinesToFile("data/machines.txt", machines); break;
            case 6: saveOperationsToFile("data/operations.txt", machines); break;
            case 7: createAndStoreOperation(&operations, &operationCount); break;
            case 8: {
                int machineId;
                char filename[100];
                printf("Enter Machine ID: ");
                scanf("%d", &machineId);
                printf("Enter output CSV filename: ");
                scanf("%99s", filename);
                
                Machine *machine = findMachineById(machines, machineId);
                if (machine) {
                    saveOperationsToCsvByMachine(machine, filename);
                } else {
                    printf("Machine not found.\n");
                }
                break;
            }
        }
    } while (choice != 0);

    // Liberar memória
    while (machines) {
        Machine *temp = machines;
        machines = machines->next;
        free(temp->operations);
        free(temp);
    }
    free(operations);

    return 0;
}
