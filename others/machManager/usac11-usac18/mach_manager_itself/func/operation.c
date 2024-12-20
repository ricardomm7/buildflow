#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../include/operation.h"

int loadOperationsFromFile(const char *filename, Operation **operations, int *operationCount) {
    FILE *file = fopen(filename, "r");
    if (!file) {
        perror("Error opening operations file");
        return 0;
    }

    *operations = NULL;
    *operationCount = 0;
    char line[200];
    
    // Pular a primeira linha se for cabeçalho
    fgets(line, sizeof(line), file);
    
    while (fgets(line, sizeof(line), file)) {
        // Ignorar linhas de comentário ou vazias
        if (line[0] == '#' || line[0] == '\n') continue;
        
        // Remover possível \n do final da linha
        line[strcspn(line, "\n")] = 0;
        
        Operation *op = realloc(*operations, (*operationCount + 1) * sizeof(Operation));
        if (!op) {
            perror("Memory reallocation error");
            break;
        }
        *operations = op;
        
        // Criar variáveis temporárias para armazenar os valores
        char tempName[50];
        char tempState[10];
        char tempTimestamp[20];
        char tempTemperature[20];
        char tempHumidity[20];
        int tempOpNumber;
        
        // Dividir a linha em tokens
        char *token = strtok(line, ",");
        if (token) strncpy(tempName, token, sizeof(tempName)-1);
        
        token = strtok(NULL, ",");
        if (token) strncpy(tempState, token, sizeof(tempState)-1);
        
        token = strtok(NULL, ",");
        if (token) strncpy(tempTimestamp, token, sizeof(tempTimestamp)-1);
        
        token = strtok(NULL, ",");
        if (token) strncpy(tempTemperature, token, sizeof(tempTemperature)-1);
        
        token = strtok(NULL, ",");
        if (token) strncpy(tempHumidity, token, sizeof(tempHumidity)-1);
        
        token = strtok(NULL, ",");
        if (token) tempOpNumber = atoi(token);
        
        // Limpar espaços em branco
        for (int i = 0; tempName[i]; i++) {
            if (tempName[i] == ' ') {
                int j;
                for (j = i; tempName[j]; j++) {
                    tempName[j] = tempName[j+1];
                }
                i--;
            }
        }
        
        // Copiar valores para a estrutura
        strncpy((*operations)[*operationCount].operationName, tempName, sizeof((*operations)[*operationCount].operationName)-1);
        strncpy((*operations)[*operationCount].state, tempState, sizeof((*operations)[*operationCount].state)-1);
        strncpy((*operations)[*operationCount].timestamp, tempTimestamp, sizeof((*operations)[*operationCount].timestamp)-1);
        
        // Converter temperatura e umidade de string para double
        (*operations)[*operationCount].temperature = atof(tempTemperature);
        (*operations)[*operationCount].humidity = atof(tempHumidity);
        (*operations)[*operationCount].operationNumber = tempOpNumber;
        
        // Garantir terminação null para as strings
        (*operations)[*operationCount].operationName[sizeof((*operations)[*operationCount].operationName)-1] = '\0';
        (*operations)[*operationCount].state[sizeof((*operations)[*operationCount].state)-1] = '\0';
        (*operations)[*operationCount].timestamp[sizeof((*operations)[*operationCount].timestamp)-1] = '\0';
            
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
        perror("Memory reallocation error");
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
        perror("Memory reallocation error");
        return 0;
    }

    machine->operations = newOps;
    machine->operations[machine->operationCount] = *operation;
    machine->operationCount++;
    return 1;
}

int saveOperationsToFile(const char *filename, Operation *operations, int operationCount) {
    FILE *file = fopen(filename, "w");
    if (!file) {
        perror("Error opening file to save operations");
        return 0;
    }

    // Escrever cabeçalho
    fprintf(file, "# operationName, state, timestamp, temperature, humidity, operationNumber\n");

    // Salvar todas as operações do array global
    for (int i = 0; i < operationCount; i++) {
        fprintf(file, "%s,%s,%s,%.2f,%.2f,%d\n",
                operations[i].operationName,
                operations[i].state,
                operations[i].timestamp,
                operations[i].temperature,
                operations[i].humidity,
                operations[i].operationNumber);
    }

    fclose(file);
    return 1;
}
