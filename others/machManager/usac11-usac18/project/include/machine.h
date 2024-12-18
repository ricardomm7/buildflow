#ifndef MACHINE_H
#define MACHINE_H
#define MAX_NAME_LENGTH 50
#define MAX_OPERATION_NAME_LENGTH 50
#define MAX_STATE_LENGTH 10
#define MAX_TIMESTAMP_LENGTH 20

typedef struct Operation {
    char operationName[MAX_OPERATION_NAME_LENGTH];
    char state[MAX_STATE_LENGTH];
    char timestamp[MAX_TIMESTAMP_LENGTH];
    double temperature;
    double humidity;
    int operationNumber;
} Operation;

typedef struct Machine {
    int id;
    char name[MAX_NAME_LENGTH];
    double minTemp;
    double maxTemp;
    double minHumidity;
    double maxHumidity;
    int bufferLength;
    int medianWindow;
    Operation *operations;
    int operationCount;
    struct Machine *next;
} Machine;

// Protótipos de funções para máquinas
int saveMachinesToFile(const char *filename, Machine *head);
Machine* loadMachinesFromFile(const char *filename);
void printMachineDetails(Machine *machine);
Machine* findMachineById(Machine *head, int id);
int validateMachineData(Machine *machine);

#endif // MACHINE_H
