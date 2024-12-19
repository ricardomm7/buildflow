#include <stdlib.h>
#include "../include/machine.h"
#include "../include/func.h"

int initializeMachineBuffers(Machine *machine, int bufferLength, int medianWindow) {
    if (machine == NULL || bufferLength <= 0 || medianWindow > bufferLength) {
        return 0;
    }
    
    machine->bufferLength = bufferLength;
    machine->medianWindow = medianWindow;
    
    // Alocar memória com malloc (sem inicializar com zero)
    machine->tempBuffer = (int*)malloc(bufferLength * sizeof(int));
    machine->humidityBuffer = (int*)malloc(bufferLength * sizeof(int));
    
    if (machine->tempBuffer == NULL || machine->humidityBuffer == NULL) {
        free(machine->tempBuffer);
        free(machine->humidityBuffer);
        return 0;
    }
    
    // Inicializar buffers com valores "inválidos" (-1, por exemplo)
    for (int i = 0; i < bufferLength; i++) {
        machine->tempBuffer[i] = -1; // Valor inicial para o buffer de temperatura
        machine->humidityBuffer[i] = -1; // Valor inicial para o buffer de umidade
    }
    
    machine->tempTail = bufferLength - 1;
    machine->tempHead = 0;
    machine->humidityTail = bufferLength - 1;
    machine->humidityHead = 0;
    
    return 1;
}

int enqueueMachineTemperature(Machine *machine, int temperature) {
    if (!machine || !machine->tempBuffer) return 0;
    
    return enqueue_value(machine->tempBuffer, machine->bufferLength, &machine->tempTail, &machine->tempHead, temperature);
}

int enqueueMachineHumidity(Machine *machine, int humidity) {
    if (!machine || !machine->humidityBuffer) return 0;
    
    return enqueue_value(machine->humidityBuffer, machine->bufferLength, &machine->humidityTail, &machine->humidityHead, humidity);
}

int calculateMachineBufferMedian(int *buffer, int bufferLength, int *tail, int *head,int medianWindow, int *medianr) {
    if (!buffer || !tail || !head || !medianr) {
        return 0;
    }
    
    // Verificar número de elementos disponíveis
    int elements = get_n_element(buffer, bufferLength, tail, head);
    if (elements < medianWindow) {
        return 0;  // Dados insuficientes para calcular mediana
    }
    
    // Alocar memória para cópia temporária dos elementos
    int *tempCopy = malloc(medianWindow * sizeof(int));
    if (!tempCopy) {
        return 0;  // Falha na alocação de memória
    }
    
    // Mover elementos para array temporário usando função de assembly
    if (move_n_to_array(buffer, bufferLength, tail, head, medianWindow, tempCopy) != medianWindow) {
        free(tempCopy);
        return 0;
    }
    
    int medianResult;
    if (median(tempCopy, medianWindow, &medianResult) == -1) {
        free(tempCopy);
        return 0;
    }
    
    *medianr = medianResult;
    free(tempCopy);
    return 1;
}

int calculateMachineTemperatureMedian(Machine *machine, int *median) {
    if (!machine || !machine->tempBuffer) return 0;
    
    return calculateMachineBufferMedian(
        machine->tempBuffer, 
        machine->bufferLength, 
        &machine->tempTail, 
        &machine->tempHead, 
        machine->medianWindow, 
        median
    );
}

int calculateMachineHumidityMedian(Machine *machine, int *median) {
    if (!machine || !machine->humidityBuffer) return 0;
    
    return calculateMachineBufferMedian(
        machine->humidityBuffer, 
        machine->bufferLength, 
        &machine->humidityTail, 
        &machine->humidityHead, 
        machine->medianWindow, 
        median
    );
}
