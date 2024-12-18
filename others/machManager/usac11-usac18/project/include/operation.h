#ifndef OPERATION_H
#define OPERATION_H

#include "machine.h"

// Protótipos de funções para operações
int loadOperationsFromFile(const char *filename, Operation **operations, int *operationCount);
void printOperationDetails(Operation *operation);
int associateOperationToMachine(Machine *machine, Operation *operation);
int saveOperationsToFile(const char *filename, Machine *head);
Operation* createOperation();
int saveOperationsToCsvByMachine(Machine *machine, const char *filename);

#endif // OPERATION_H
