#include <stdio.h>
#include <stdlib.h>
#include "../include/plant_management.h"
#include "../include/machine.h"
#include "../include/operation.h"
#include "../include/func.h"

Machine *machineList;

void printMachineBuffers(Machine *machine) {
    // Imprimir os elementos do buffer de temperatura
    printf("[DEBUG] tempBuffer for Machine %d: ", machine->id);
    for (int i = machine->tempTail; i != machine->tempHead; i = (i + 1) % machine->bufferLength) {
        printf("%d ", machine->tempBuffer[i]);
    }
    printf("[DEBUG] temptail %d temphead %d", machine->tempTail, machine->tempHead);
    printf("\n");

    // Imprimir os elementos do buffer de umidade
    printf("[DEBUG] humidityBuffer for Machine %d: ", machine->id);
    for (int i = machine->humidityTail; i != machine->humidityHead; i = (i + 1) % machine->bufferLength) {
        printf("%d ", machine->humidityBuffer[i]);
    }
    printf("[DEBUG] temphum %d humhead %d", machine->humidityTail, machine->humidityHead);
    printf("\n");
}

int main() {
    machineList = loadMachinesFromFile("data/machines.txt");
    Operation *operations = NULL;
    int operationCount = 0;
    loadOperationsFromFile("data/operations.txt", &operations, &operationCount);
    
    int choice;
    do {
        printf("\nBUILDFLOW MACHMANAGER MAIN MENU\n");
        printf("1. Mach Manager Menu\n");
        printf("2. Send Command\n");
        printf("0. Exit\n");
        printf("Choose an option: ");
        scanf("%d", &choice);
        
        switch(choice) {
            case 1: 
                machManager(&machineList, &operations, &operationCount);
                break;
            case 2:
                listMachines(machineList);
                
                int machineId;
                printf("Enter Machine ID to send command: ");
                scanf("%d", &machineId);
                
                Machine *selectedMachine = findMachineById(machineList, machineId);
                
                if (selectedMachine) {
                    char command[256];
                    printf("Enter command (format: OP,0,0,0,0,1): ");
                    scanf("%255s", command);
                    
                    char response[256];
                    if (send_and_read_from_machine(command, response, selectedMachine) == 0) {
                        printf("Command executed successfully for Machine %d. Response: %s\n", 
                               selectedMachine->id, response);
                    } else {
                        fprintf(stderr, "Failed to execute command for Machine %d.\n", 
                                selectedMachine->id);
                    }
                } else {
                    printf("Machine with ID %d not found.\n", machineId);
                }
                printf("\n[DEBUG] Printing buffers for all machines:\n");
                for (Machine *machine = machineList; machine != NULL; machine = machine->next) {
                    printMachineBuffers(machine);
                }
  				break;
        }
    } while (choice != 0);
    
    // Liberar memória
    while (machineList) {
        Machine *temp = machineList;
        machineList = machineList->next;
        free(temp->operations);
        free(temp);
    }
    free(operations);
    
    return 0;
}

int machManager(Machine **machines, Operation **operations, int *operationCount) {
    int choice;
    do {
        printf("\nMACHINE OPTIONS - SUBMENU\n");
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
            case 1: 
                listMachines(*machines); 
                break;
            case 2: 
                addMachine(machines); 
                break;
            case 3: {
                int id;
                printf("Enter Machine ID to remove: ");
                scanf("%d", &id);
                removeMachine(machines, id);
                break;
            }
            case 4: 
                assignOperationToMachine(*machines, *operations, *operationCount); 
                break;
            case 5: 
                saveMachinesToFile("data/machines.txt", *machines); 
                break;
            case 6: 
                saveOperationsToFile("data/operations.txt", *machines); 
                break;
            case 7: 
                createAndStoreOperation(operations, operationCount); 
                break;
            case 8: {
                int machineId;
                char filename[100];
                printf("Enter Machine ID: ");
                scanf("%d", &machineId);
                printf("Enter output CSV filename: ");
                scanf("%99s", filename);
                
                Machine *machine = findMachineById(*machines, machineId);
                if (machine) {
                    saveOperationsToCsvByMachine(machine, filename);
                } else {
                    printf("Machine not found.\n");
                }
                break;
            }
        }
    } while (choice != 0);
    
    return 0;
}
