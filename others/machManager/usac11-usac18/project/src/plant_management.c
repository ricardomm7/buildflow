#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../include/plant_management.h"
#include "../include/machine.h"
#include "../include/operation.h"


void addMachine(Machine **head) {
    Machine *newMachine = malloc(sizeof(Machine));
    printf("\n\nEnter Machine Details:\n");
    printf("ID: ");
    scanf("%d", &newMachine->id);
    printf("Name: ");
    scanf("%49s", newMachine->name);
    printf("Min Temperature: ");
    scanf("%lf", &newMachine->minTemp);
    printf("Max Temperature: ");
    scanf("%lf", &newMachine->maxTemp);
    printf("Min Humidity: ");
    scanf("%lf", &newMachine->minHumidity);
    printf("Max Humidity: ");
    scanf("%lf", &newMachine->maxHumidity);
    printf("Buffer Length: ");
    scanf("%d", &newMachine->bufferLength);
    printf("Median Window:");
    scanf("%d\n", &newMachine->medianWindow);

    newMachine->operations = NULL;
    newMachine->operationCount = 0;
    newMachine->next = *head;
    *head = newMachine;
}

int removeMachine(Machine **head, int id) {
    Machine *current = *head, *prev = NULL;

    while (current) {
        if (current->id == id) {
            if (current->operationCount > 0) {
                printf("Cannot remove machine with active operations.\n");
                return 0;
            }
            
            if (prev) prev->next = current->next;
            else *head = current->next;

            free(current->operations);
            free(current);
            return 1;
        }
        prev = current;
        current = current->next;
    }
    return 0;
}

void listMachines(Machine *head) {
    Machine *current = head;
    while (current) {
        printMachineDetails(current);
        current = current->next;
    }
}

void createAndStoreOperation(Operation **operations, int *operationCount) {
    Operation *newOperation = createOperation();
    if (newOperation) {
        Operation *temp = realloc(*operations, (*operationCount + 1) * sizeof(Operation));
        if (temp) {
            *operations = temp;
            (*operations)[*operationCount] = *newOperation;
            (*operationCount)++;
            free(newOperation);
            printf("Operation added successfully.\n");
        } else {
            perror("Erro de alocação de memória");
            free(newOperation);
        }
    }
}

void assignOperationToMachine(Machine *head, Operation *operations, int operationCount) {
    int machineId, opIndex;

    printf("\nAvailable Machines:\n");
    listMachines(head);

    printf("Enter Machine ID: \n");
    scanf("%d", &machineId);

    Machine *machine = findMachineById(head, machineId);
    if (!machine) {
        printf("Machine not found.\n");
        return;
    }

    printf("\nAvailable Operations:\n");
    for (int i = 0; i < operationCount; i++) {
        printf("[%d] ", i);
        printOperationDetails(&operations[i]);
    }

    printf("Select Operation Index: \n");
    scanf("%d", &opIndex);

    if (opIndex < 0 || opIndex >= operationCount) {
        printf("Invalid operation index.\n");
        return;
    }

    associateOperationToMachine(machine, &operations[opIndex]);
}

void loadInstructionsFromFile(const char *filename, Machine *head) {
    FILE *file = fopen(filename, "r");
    if (!file) {
        perror("Erro ao abrir arquivo de instruções");
        return;
    }

    char line[100];
    while (fgets(line, sizeof(line), file)) {
        // Processa cada instrução (exemplo simplificado)
        char cmd[10];
        int machineId, operationNumber;
        
        if (sscanf(line, "%9[^,],%d,%d", cmd, &machineId, &operationNumber) == 3) {
            Machine *machine = findMachineById(head, machineId);
            if (machine) {
                // Lógica para processar a instrução
                printf("Processing: %s for Machine %d, Operation %d\n", cmd, machineId, operationNumber);
            }
        }
    }

    fclose(file);
}
