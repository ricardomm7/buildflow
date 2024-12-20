#include <stdio.h>
#include <stdlib.h>
#include "../include/plant_management.h"
#include "../include/machine.h"
#include "../include/operation.h"
#include "../include/func.h"

Machine *machineList;



int main() {
    machineList = loadMachinesFromFile("data/machines.txt");
    Operation *operations = NULL;
    int operationCount = 0;
    loadOperationsFromFile("data/operations.txt", &operations, &operationCount);
    
    int choice;
    do {
        printf("\nBUILDFLOW MACHMANAGER MAIN MENU\n");
        printf("1. Machine options\n");
        printf("2. Send command\n");
        printf("3. See machine state\n");
        printf("4. Read instructions and execute them in the machine\n");
        printf("0. Exit\n");
        printf("Choose an option: ");
        scanf("%d", &choice);
        
        switch(choice) {
            case 1: 
                machManager(&machineList, &operations, &operationCount);
                break;
            case 2: {
                listMachines(machineList);
                
                int machineId;
                printf("\nEnter Machine ID to send command: ");
                scanf("%d", &machineId);
                
                Machine *selectedMachine = findMachineById(machineList, machineId);
                
                if (selectedMachine) {
                    char command[256];
                    printf("\nEnter command (format: OP,0,0,0,0,1): ");
                    scanf("%255s", command);
                    
                    char response[256];
                    if (send_and_read_from_machine(command, response, selectedMachine) == 0) {
                        printf("\nCommand executed successfully for Machine %d. Response: %s\n", 
                               selectedMachine->id, response);
                    } else {
                        fprintf(stderr, "\nFailed to execute command for Machine %d.\n", 
                                selectedMachine->id);
                    }
                } else {
                    printf("\nMachine with ID %d not found.\n", machineId);
                }
                break;
            }
            case 3: 
				listMachines(machineList);
    
				int machineId2;
				printf("\nEnter Machine ID to see state: ");
				scanf("%d", &machineId2);
    
				Machine *selectedMachine2 = findMachineById(machineList, machineId2);
    
				if (selectedMachine2) {
					// Passar o ponteiro diretamente, sem dereferenciação
					executeMachineOP(selectedMachine2);
				} else {
					printf("\nMachine with ID %d not found.\n", machineId2);
				}               
				break;
            case 4: {
                printf("Processing instructions from file...\n");
                processInstructions(machineList, "data/instructions.txt");
                break;
            }
            case 0:
                break;
            default:
                printf("Invalid option!\n");
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
				saveOperationsToFile("data/operations.txt", *operations, *operationCount);
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
            case 0:
                break;
            default:
                printf("Invalid option!\n");
                break;
        }
    } while (choice != 0);
    
    return 0;
}
