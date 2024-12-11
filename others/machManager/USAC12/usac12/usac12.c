#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_NAME_LENGTH 50
#define MAX_STATE_LENGTH 10
#define MAX_OPERATION_NAME_LENGTH 50
#define MAX_TIMESTAMP_LENGTH 20

// Estrutura para representar uma operação
typedef struct {
    char operationName[MAX_OPERATION_NAME_LENGTH]; // Nome da operação
    char state[MAX_STATE_LENGTH];                  // Estado ("OP", "ON", "OFF")
    char timestamp[MAX_TIMESTAMP_LENGTH];          // Timestamp
    float temperature;                             // Temperatura registrada
    float humidity;                                // Umidade registrada
    int operationNumber;                           // Número da operação (0-31)
} Operation;

// Estrutura para representar uma máquina
typedef struct {
    int id;                     // Identificador
    int minTemp;                // Temperatura mínima
    int maxTemp;                // Temperatura máxima
    int minHumidity;            // Umidade mínima
    int maxHumidity;            // Umidade máxima
    int bufferLength;           // Tamanho do buffer circular
    int medianWindow;           // Tamanho da janela da mediana
    char name[MAX_NAME_LENGTH]; // Nome da máquina
    Operation *operations;      // Memória dinâmica para operações
} Machine;

// Função para carregar operações de um arquivo de texto
int loadOperations(const char *filePath, Operation **operations, int *operationCount) {
    FILE *file = fopen(filePath, "r");
    if (!file) {
        perror("Erro ao abrir o arquivo");
        return 0;
    }

    char line[200];
    int count = 0;
    Operation *operationArray = NULL;

    while (fgets(line, sizeof(line), file)) {
        if (line[0] == '\n' || line[0] == '#') // Ignorar linhas vazias ou comentários
            continue;

        Operation operation;
        if (sscanf(line, "%49[^,], %9[^,], %19[^,], %f, %f, %d",
                   operation.operationName, operation.state,
                   operation.timestamp, &operation.temperature,
                   &operation.humidity, &operation.operationNumber) != 6) {
            fprintf(stderr, "Formato de linha inválido: %s", line);
            continue;
        }

        operationArray = realloc(operationArray, (count + 1) * sizeof(Operation));
        if (!operationArray) {
            perror("Erro ao alocar memória para operações");
            fclose(file);
            return 0;
        }
        operationArray[count++] = operation;
    }

    fclose(file);

    *operations = operationArray;
    *operationCount = count;
    return 1;
}

// Função para carregar máquinas a partir de um arquivo de texto
int loadMachines(const char *filePath, Machine **machines, int *machineCount) {
    FILE *file = fopen(filePath, "r");
    if (!file) {
        perror("Erro ao abrir o arquivo");
        return 0;
    }

    char line[200];
    int count = 0;
    Machine *machineArray = NULL;

    while (fgets(line, sizeof(line), file)) {
        if (line[0] == '\n' || line[0] == '#') // Ignorar linhas vazias ou comentários
            continue;

        Machine machine;
        if (sscanf(line, "%d, %49[^,], %d, %d, %d, %d, %d, %d",
                   &machine.id, machine.name,
                   &machine.minTemp, &machine.maxTemp,
                   &machine.minHumidity, &machine.maxHumidity,
                   &machine.bufferLength, &machine.medianWindow) != 8) {
            fprintf(stderr, "Formato de linha inválido: %s", line);
            continue;
        }

        // Inicializar operações como NULL (sem operações inicialmente)
        machine.operations = NULL;

        machineArray = realloc(machineArray, (count + 1) * sizeof(Machine));
        if (!machineArray) {
            perror("Erro ao alocar memória para máquinas");
            fclose(file);
            return 0;
        }
        machineArray[count++] = machine;
    }

    fclose(file);

    *machines = machineArray;
    *machineCount = count;
    return 1;
}

// Função para exibir informações das máquinas
void printMachines(const Machine *machines, const int *operationCounts, int machineCount) {
    for (int i = 0; i < machineCount; i++) {
        printf("Máquina %d: %s\n", machines[i].id, machines[i].name);
        printf("  Temp Min-Max: %d-%d\n", machines[i].minTemp, machines[i].maxTemp);
        printf("  Hum Min-Max: %d-%d\n", machines[i].minHumidity, machines[i].maxHumidity);
        printf("  Operações:\n");

        if (machines[i].operations != NULL) {
            for (int j = 0; j < operationCounts[i]; j++) {
                Operation *op = &machines[i].operations[j];
                printf("    [%d] %s - %s | Temp: %.2f, Hum: %.2f, Time: %s\n",
                       op->operationNumber, op->operationName, op->state,
                       op->temperature, op->humidity, op->timestamp);
            }
        } else {
            printf("    Nenhuma operação registrada.\n");
        }
        printf("\n");
    }
}

// Função para exibir operações com índices
void printOperations(const Operation *operations, int operationCount) {
    printf("Operações Disponíveis:\n");
    for (int i = 0; i < operationCount; i++) {
        printf("[%d] %s - %s | Temp: %.2f, Hum: %.2f, Time: %s\n",
               i, operations[i].operationName, operations[i].state,
               operations[i].temperature, operations[i].humidity,
               operations[i].timestamp);
    }
    printf("\n");
}

// Função para exibir máquinas com índices
void printMachinesIndexed(const Machine *machines, int machineCount) {
    printf("Máquinas Disponíveis:\n");
    for (int i = 0; i < machineCount; i++) {
        printf("[%d] Máquina %d: %s | Temp Min-Max: %d-%d, Hum Min-Max: %d-%d\n",
               i, machines[i].id, machines[i].name,
               machines[i].minTemp, machines[i].maxTemp,
               machines[i].minHumidity, machines[i].maxHumidity);
    }
    printf("\n");
}

// Função para associar uma operação a uma máquina
int associateOperation(Machine *machine, Operation operation, int *operationCount) {
    machine->operations = realloc(machine->operations, (*operationCount + 1) * sizeof(Operation));
    if (!machine->operations) {
        perror("Erro ao alocar memória para operações");
        return 0;
    }

    machine->operations[*operationCount] = operation;
    (*operationCount)++;
    return 1;
}

// Função para liberar memória alocada
void freeMachines(Machine *machines, int machineCount) {
    for (int i = 0; i < machineCount; i++) {
        free(machines[i].operations);
    }
    free(machines);
}

void freeOperations(Operation *operations) {
    free(operations);
}

// Função principal para teste
int main() {
    Machine *machines = NULL;
    int machineCount = 0;

    Operation *operations = NULL;
    int operationCount = 0;

    if (!loadMachines("/media/sf_partilha/arqcp2425/SEM3/Sprint3/machines.txt", &machines, &machineCount)) {
        fprintf(stderr, "Erro ao carregar máquinas.\n");
        return EXIT_FAILURE;
    }

    if (!loadOperations("/media/sf_partilha/arqcp2425/SEM3/Sprint3/Operations.txt", &operations, &operationCount)) {
        fprintf(stderr, "Erro ao carregar operações.\n");
        freeMachines(machines, machineCount);
        return EXIT_FAILURE;
    }

    printf("Máquinas carregadas com sucesso! Total: %d\n\n", machineCount);
    printf("Operações carregadas com sucesso! Total: %d\n\n", operationCount);

    // Exibir lista de operações e permitir associação
    int opIndex, machineIndex;
    int machineOperationCounts[machineCount];
    memset(machineOperationCounts, 0, sizeof(machineOperationCounts));

    while (1) {
        printOperations(operations, operationCount);
        printf("Digite o índice da operação a associar (-1 para sair): ");
        scanf("%d", &opIndex);

        if (opIndex == -1)
            break;

        if (opIndex < 0 || opIndex >= operationCount) {
            printf("Índice inválido. Tente novamente.\n");
            continue;
        }

        printMachinesIndexed(machines, machineCount);
        printf("Digite o índice da máquina para associar a operação: ");
        scanf("%d", &machineIndex);

        if (machineIndex < 0 || machineIndex >= machineCount) {
            printf("Índice inválido. Tente novamente.\n");
            continue;
        }

        if (associateOperation(&machines[machineIndex], operations[opIndex], &machineOperationCounts[machineIndex])) {
            printf("Operação '%s' associada com sucesso à máquina '%s'.\n\n",
                   operations[opIndex].operationName, machines[machineIndex].name);
        }
    }

    printMachines(machines, machineOperationCounts, machineCount);

    freeMachines(machines, machineCount);
    freeOperations(operations);
    return 0;
}
