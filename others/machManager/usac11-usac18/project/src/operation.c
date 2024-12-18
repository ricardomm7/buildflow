#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../include/operation.h"

int loadOperationsFromFile(const char *filename, Operation **operations, int *operationCount) {
    FILE *file = fopen(filename, "r");
    if (!file) {
        perror("Erro ao abrir arquivo de operações");
        return 0;
    }

    *operations = NULL;
    *operationCount = 0;
    char line[200];
    
    while (fgets(line, sizeof(line), file)) {
        if (line[0] == '#' || line[0] == '\n') continue;

        Operation *op = realloc(*operations, (*operationCount + 1) * sizeof(Operation));
        if (!op) {
            perror("Erro de alocação de memória");
            break;
        }
        *operations = op;

        sscanf(line, "%49[^,],%9[^,],%19[^,],%lf,%lf,%d", 
               (*operations)[*operationCount].operationName,
               (*operations)[*operationCount].state,
               (*operations)[*operationCount].timestamp,
               &(*operations)[*operationCount].temperature,
               &(*operations)[*operationCount].humidity,
               &(*operations)[*operationCount].operationNumber);

        (*operationCount)++;
    }

    fclose(file);
    return 1;
}

void printOperationDetails(Operation *operation) {
    printf("\n\nOperation: %s\n", operation->operationName);
    printf("State: %s\n", operation->state);
    printf("Timestamp: %s\n", operation->timestamp);
    printf("Temperature: %.2lf\n", operation->temperature);
    printf("Humidity: %.2lf\n", operation->humidity);
    printf("Operation Number: %d\n\n", operation->operationNumber);
}

Operation* createOperation() {
    Operation* newOperation = malloc(sizeof(Operation));
    if (!newOperation) {
        perror("Erro de alocação de memória");
        return NULL;
    }

    printf("\nEnter Operation Details:\n");
    printf("Operation Name: ");
    scanf("%49s", newOperation->operationName);
    printf("State (ON/OFF/OP): ");
    scanf("%9s", newOperation->state);
    printf("Timestamp : ");
    scanf("%19s", newOperation->timestamp);
    printf("Temperature: ");
    scanf("%lf", &newOperation->temperature);
    printf("Humidity: ");
    scanf("%lf", &newOperation->humidity);
    printf("Operation Number: ");
    scanf("%d", &newOperation->operationNumber);

    return newOperation;
}

int saveOperationsToCsvByMachine(Machine *machine, const char *filename) {
    if (!machine || machine->operationCount == 0) {
        printf("No operations to save or machine not found.\n");
        return 0;
    }

    FILE *file = fopen(filename, "w");
    if (!file) {
        perror("Erro ao abrir arquivo para salvar operações");
        return 0;
    }

    // Cabeçalho CSV
    fprintf(file, "OperationName,State,Timestamp,Temperature,Humidity,OperationNumber\n");

    for (int i = 0; i < machine->operationCount; i++) {
        Operation *op = &machine->operations[i];
        fprintf(file, "%s,%s,%s,%.2lf,%.2lf,%d\n",
                op->operationName, op->state, op->timestamp,
                op->temperature, op->humidity, op->operationNumber);
    }

    fclose(file);
    printf("Operations saved to %s successfully.\n", filename);
    return 1;
}

int associateOperationToMachine(Machine *machine, Operation *operation) {
    Operation *newOps = realloc(machine->operations, 
                                (machine->operationCount + 1) * sizeof(Operation));
    if (!newOps) {
        perror("Erro de alocação de memória");
        return 0;
    }

    machine->operations = newOps;
    machine->operations[machine->operationCount] = *operation;
    machine->operationCount++;
    return 1;
}

int saveOperationsToFile(const char *filename, Machine *head) {
    FILE *file = fopen(filename, "w");
    if (!file) {
        perror("Erro ao abrir arquivo para salvar operações");
        return 0;
    }

    Machine *machine = head;
    while (machine) {
        for (int i = 0; i < machine->operationCount; i++) {
            Operation *op = &machine->operations[i];
            fprintf(file, "%s,%s,%s,%.2f,%.2f,%d\n",
                    op->operationName, op->state, op->timestamp,
                    op->temperature, op->humidity, op->operationNumber);
        }
        machine = machine->next;
    }

    fclose(file);
    return 1;
}
