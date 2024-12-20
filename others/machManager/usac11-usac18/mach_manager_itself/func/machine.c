#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../include/machine.h"
#include "../include/operation.h"
#include "../include/func.h"


int saveMachinesToFile(const char *filename, Machine *head) {
    FILE *file = fopen(filename, "w");
    if (!file) {
        perror("Error opening file to save machines");
        return 0;
    }

    Machine *current = head;
    while (current) {
        // First, write machine details
        fprintf(file, "MACHINE:%d,%s,%d,%d,%d,%d,%d,%d\n",
                 current->id, current->name,
                 current->minTemp, current->maxTemp,
                 current->minHumidity, current->maxHumidity,
                 current->bufferLength, current->medianWindow);

        // Then, write operations for this machine
        for (int i = 0; i < current->operationCount; i++) {
            fprintf(file, "OPERATION:%d,%s,%s,%s,%lf,%lf,%d\n", 
                    current->id,  // Machine ID to link operation to machine
                    current->operations[i].operationName,
                    current->operations[i].state,
                    current->operations[i].timestamp,
                    current->operations[i].temperature,
                    current->operations[i].humidity,
                    current->operations[i].operationNumber);
        }

        current = current->next;
    }

    fclose(file);
    return 1;
}
Machine* loadMachinesFromFile(const char *filename) {
    FILE *file = fopen(filename, "r");
    if (!file) {
        perror("Error opening machine file");
        return NULL;
    }

    Machine *head = NULL;
    Machine *currentMachine = NULL;
    char line[200];

    while (fgets(line, sizeof(line), file)) {
        // Remove newline character
        line[strcspn(line, "\n")] = 0;

        if (strncmp(line, "MACHINE:", 8) == 0) {
            // Create a new machine
            Machine *machine = malloc(sizeof(Machine));
            if (!machine) {
                perror("Memory reallocation error");
                break;
            }

            // Parse machine details
            sscanf(line + 8, "%d,%49[^,],%d,%d,%d,%d,%d,%d",
                   &machine->id, machine->name,
                   &machine->minTemp, &machine->maxTemp,
                   &machine->minHumidity, &machine->maxHumidity,
                   &machine->bufferLength, &machine->medianWindow);

            // Inicializar buffers dinâmicos
            initializeMachineBuffers(machine, machine->bufferLength, machine->medianWindow);

            // Initialize operations
            machine->operations = NULL;
            machine->operationCount = 0;

            // Chamada para inicializar os buffers da máquina
            if (!initializeMachineBuffers(machine, machine->bufferLength, machine->medianWindow)) {
                printf("[Error] Failed to initialize buffers for Machine %d\n", machine->id);
                free(machine); // Libera a memória em caso de falha
                continue;
            }

            // Link to the machine list
            machine->next = head;
            head = machine;
            currentMachine = machine;
        }
        else if (strncmp(line, "OPERATION:", 10) == 0 && currentMachine) {
            // Allocate or reallocate operations array
            Operation *newOperations = realloc(currentMachine->operations, 
                (currentMachine->operationCount + 1) * sizeof(Operation));
            
            if (!newOperations) {
                perror("Memory reallocation error");
                break;
            }

            currentMachine->operations = newOperations;
            Operation *operation = &currentMachine->operations[currentMachine->operationCount];

            // Parse operation details
            int machineId;  // To verify machine ID matches
            sscanf(line + 10, "%d,%49[^,],%9[^,],%19[^,],%le,%le,%d",
                   &machineId,
                   operation->operationName,
                   operation->state,
                   operation->timestamp,
                   &operation->temperature,
                   &operation->humidity,
                   &operation->operationNumber);

            // Increment operation count
            currentMachine->operationCount++;
        }
    }

    fclose(file);
    return head;
}
void printMachineDetails(Machine *machine) {
    printf("\n=== Machine Details ===\n");
    printf("%-15s: %d\n", "Machine ID", machine->id);
    printf("%-15s: %s\n", "Name", machine->name);
    printf("%-15s: %d°C - %d°C\n", "Temperature", machine->minTemp, machine->maxTemp);
    printf("%-15s: %d%% - %d%%\n", "Humidity", machine->minHumidity, machine->maxHumidity);
    printf("%-15s: %d\n", "Buffer Length", machine->bufferLength);
    printf("%-15s: %d\n", "Median Window", machine->medianWindow);
    
    printf("\n--- Associated Operations ---\n");
    if (machine->operationCount > 0) {
        printf("%-6s | %-20s | %-15s\n", "Number", "Name", "State");
        printf("----------------------------------------\n");
        for (int i = 0; i < machine->operationCount; i++) {
            Operation *op = &machine->operations[i];
            printf("%-6d | %-20s | %-15s\n", 
                op->operationNumber,
                op->operationName,
                op->state);
        }
    } else {
        printf("No operations found\n");
    }
    printf("\n");
}

Machine* findMachineById(Machine *head, int id) {
    Machine *current = head;
    while (current) {
        if (current->id == id) return current;
        current = current->next;
    }
    return NULL;
}

int validateMachineData(Machine *machine) {
    return (machine->minTemp < machine->maxTemp &&
            machine->minHumidity < machine->maxHumidity &&
            machine->bufferLength > 0 &&
            machine->medianWindow > 0);
}

Machine* findMachineForOperation(Machine* machineList, int operationNumber) {
    Machine* current = machineList;
    while (current != NULL) {
        for (int i = 0; i < current->operationCount; i++) {
            if (current->operations[i].operationNumber == operationNumber) {
                return current;
            }
        }
        current = current->next;
    }
    return NULL;
}

void executeMachineOP(Machine *machine) {
    if (machine == NULL) {
        fprintf(stderr, "Error: Invalid machine pointer.\n");
        return;
    }

    if (machine->operationCount <= 0 || machine->operations == NULL) {
        fprintf(stderr, "Error: The machine has no valid operations.\n");
        return;
    }

    char cmd[256];
    char response[256];
    
    for (int i = 0; i < machine->operationCount; i++) {
        Operation op = machine->operations[i];
        char *constant_name = op.state;
        
        if (format_command(constant_name, op.operationNumber, cmd) == 1) {
            printf("Formatted command: %s\n", cmd);
            // Passando machine em vez de &a já que agora machine já é um ponteiro
            if (send_and_read_from_machine(cmd, response, machine) == 0) {
                printf("Machine response: %s\n", response);
            } else {
                fprintf(stderr, "Error sending command for operation %d.\n", op.operationNumber);
            }
        } else {
            fprintf(stderr, "Error formatting command for operation %d.\n", op.operationNumber);
        }
    }
}
