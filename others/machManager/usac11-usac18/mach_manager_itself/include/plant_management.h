#ifndef PLANT_MANAGEMENT_H
#define PLANT_MANAGEMENT_H

#include "../include/machine.h"

void addMachine(Machine **head);
int removeMachine(Machine **head, int id);
void listMachines(Machine *head);
void assignOperationToMachine(Machine *head, Operation *operations, int operationCount);
void loadInstructionsFromFile(const char *filename, Machine *head);
void createAndStoreOperation(Operation **operations, int *operationCount);
int machManager(Machine **machines, Operation **operations, int *operationCount);


#endif // PLANT_MANAGEMENT_H
