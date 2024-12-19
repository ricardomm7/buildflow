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
    int minTemp;
    int maxTemp;
    int minHumidity;
    int maxHumidity;
    int bufferLength;
    int medianWindow;
    Operation *operations;
    int operationCount;
    struct Machine *next;
    
    
    // Novos campos
    int *tempBuffer;
    int *humidityBuffer;
    int tempTail;
    int tempHead;
    int humidityTail;
    int humidityHead;
} Machine;

// Protótipos de funções para máquinas
int saveMachinesToFile(const char *filename, Machine *head);
Machine* loadMachinesFromFile(const char *filename);
void printMachineDetails(Machine *machine);
Machine* findMachineById(Machine *head, int id);
int validateMachineData(Machine *machine);

// Novas
int initializeMachineBuffers(Machine *machine, int bufferLength, int medianWindow);
void freeMachineBuffers(Machine *machine);
int enqueueMachineTemperature(Machine *machine, int temperature);
int enqueueMachineHumidity(Machine *machine, int humidity);
int calculateMachineTemperatureMedian(Machine *machine, int *median);
int calculateMachineHumidityMedian(Machine *machine, int *median);
#endif
