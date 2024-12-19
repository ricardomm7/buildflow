#include <stdio.h>
#include "../include/func.h"
#include "../include/machine.h"

extern Machine *machineList;

void error(const char *context, int machineId) {
    printf("[Error] Machine %d: %s\n", machineId, context);
}

void exceedTemperature(float value, float minTemp, float maxTemp, int machineId) {
    printf("[Alert] Machine %d: Temperature out of range! Current value: %.2f°C. Allowed range: %.2f°C - %.2f°C.\n", 
           machineId, value, minTemp, maxTemp);
}

void exceedHumidity(float value, float minHum, float maxHum, int machineId) {
    printf("[Alert] Machine %d: Humidity out of range! Current value: %.2f%%. Allowed range: %.2f%% - %.2f%%.\n", 
           machineId, value, minHum, maxHum);
}

void check_for_alerts_for_machine(Machine *machine) {
    int tempMedian, humMedian;

	if(get_n_element(machine->tempBuffer, machine->bufferLength, &machine->tempTail, &machine->tempHead) >= machine->medianWindow){
		//int numTempElements = get_n_element(machine->tempBuffer, machine->bufferLength, &machine->tempTail, &machine->tempHead);
		//printf("[DEBUG] Machine %d: tempBuffer elements = %d, required = %d\n", machine->id, numTempElements, machine->medianWindow);
		
		if (!calculateMachineTemperatureMedian(machine, &tempMedian)) {
			error("Failed to calculate temperature median.", machine->id);
			return;
		}

		if (tempMedian > machine->maxTemp || tempMedian < machine->minTemp) {
			exceedTemperature(tempMedian, machine->minTemp, machine->maxTemp, machine->id);
		} else {
			printf("[Info] Machine %d: Temperature within range. Current value: %d°C. Allowed range: %d°C - %d°C.\n",
				   machine->id, tempMedian, machine->minTemp, machine->maxTemp);
		}
	} else {
		printf("[DEBUG] No check for %d: %s\n", machine->id, machine->name);
	}
        
	if(get_n_element(machine->humidityBuffer, machine->bufferLength, &machine->humidityTail, &machine->humidityHead) >= machine->medianWindow){
		//int numTempElements = get_n_element(machine->tempBuffer, machine->bufferLength, &machine->tempTail, &machine->tempHead);
		//printf("[DEBUG] Machine %d: tempBuffer elements = %d, required = %d\n", machine->id, numTempElements, machine->medianWindow);
		
		if (!calculateMachineHumidityMedian(machine, &humMedian)) {
			error("Failed to calculate humidity median.", machine->id);
			return;
		}

		if (humMedian > machine->maxHumidity || humMedian < machine->minHumidity) {
			exceedHumidity(humMedian, machine->minHumidity, machine->maxHumidity, machine->id);
		} else {
			printf("[Info] Machine %d: Humidity within range. Current value: %d%%. Allowed range: %d%% - %d%%.\n",
				   machine->id, humMedian, machine->minHumidity, machine->maxHumidity);
		}
	} else {
		printf("[DEBUG] No check for %d: %s\n", machine->id, machine->name);
	}
}

void check_for_alerts() {
    Machine *currentMachine = machineList;

    while (currentMachine != NULL) {
		//printf("Checking alerts for machine %d: %s\n", currentMachine->id, currentMachine->name);
		check_for_alerts_for_machine(currentMachine);
		currentMachine = currentMachine->next;
    }
}
