#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../include/machine.h"
#include "../include/operation.h"

int saveMachinesToFile(const char *filename, Machine *head) {
    FILE *file = fopen(filename, "w");
    if (!file) {
        perror("Erro ao abrir arquivo para salvar máquinas");
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
        perror("Erro ao abrir arquivo de máquinas");
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
                perror("Erro de alocação de memória");
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
                perror("Erro de realocação de memória");
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
    printf("\nMachine ID: %d\n", machine->id);
    printf("Name: %s\n", machine->name);
    printf("Temp Range: %d - %d\n", machine->minTemp, machine->maxTemp);
    printf("Humidity Range: %d - %d\n", machine->minHumidity, machine->maxHumidity);
    printf("Buffer Length: %d\n", machine->bufferLength);
    printf("Median Window: %d\n", machine->medianWindow);
    
    if (machine->operationCount > 0) {
        printf("Associated Operations:\n");
        for (int i = 0; i < machine->operationCount; i++) {
            printOperationDetails(&machine->operations[i]);
        }
    } else {
        printf("Associated Operations: None\n");
    }
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
