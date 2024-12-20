#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../include/machine.h"
#include "../include/operation.h"
#include "../include/func.h"

// Função para ler o arquivo de instruções e converter os IDs para binário
int loadOpInstructFromFile(const char *filename, int **operationIds, int *operationCount) {
	FILE *file = fopen(filename, "r");
	if (!file) {
		perror("Error opening the instruction file.");
		return 0;
	}

	int *ids = NULL;
	int count = 0;
	char line[10];

	while (fgets(line, sizeof(line), file)) {
		if (line[0] == '#' || line[0] == '\n') continue;

		int operationId = atoi(line);
		if (operationId >= 0 && operationId <= 31) {  // Considera operações entre 0 e 31
			ids = realloc(ids, (count + 1) * sizeof(int));
			if (!ids) {
				perror("Memory allocation error.");
				fclose(file);
				return 0;
			}
			ids[count] = operationId;
			count++;
		}
	}

	fclose(file);
	*operationIds = ids;
	*operationCount = count;
	return 1;
}

// Função para encontrar uma operação numa máquina com base no ID da operação
Operation* findOperationById(Machine *machine, int operationId) {
	for (int i = 0; i < machine->operationCount; i++) {
		if (machine->operations[i].operationNumber == operationId) {
			return &machine->operations[i];
		}
	}
	return NULL;  // Retorna NULL se a operação não for encontrada
}

//maybe
void executeBunchOfInstructions() {
}







