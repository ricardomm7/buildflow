#include <stdio.h>
#include "../include/func.h"

// Constantes e variáveis globais
#define BUFFER_LENGTH 5
#define MEDIAN_WINDOW 4

int tempVec[BUFFER_LENGTH] = {0, 8, 9, 2, 5};
int humVec[BUFFER_LENGTH] = {3, 6, 7, 2, 4};
int tail = 0;
int head = 4;

float maxTemp = 25.0;
float minTemp = 4;
float maxHum = 60.0;
float minHum = 0.4;

void error(const char *context) {
    printf("Error: %s\n", context);
}

void exceedTemperature(float value) {
    printf("Alert: Temperature out of range! Current value: %.2f\n", value);
}

void exceedHumidity(float value) {
    printf("Alert: Humidity out of range! Current value: %.2f\n", value);
}

int is_sufficient_data(int vec[], int buffer_length, int *tail, int *head) {
    int n_elements = get_n_element(vec, buffer_length, tail, head);
    //printf("Debug: Number of elements in buffer: %d\n", n_elements);
    return n_elements >= MEDIAN_WINDOW;
}

int calculate_median(int vec[], int window, int *result) {
    int validation = median(vec, window, result);
    if (validation == -1) {
        return 0;
    }
    return 1;
}

void check_temperature_alerts(int median_value) {
    if (median_value > (int) maxTemp) {
        exceedTemperature(median_value);
    } else if (median_value < (int) minTemp) {
        exceedTemperature(median_value);
    }
}

void check_humidity_alerts(int median_value) {
    if (median_value > (int) maxHum) {
        exceedHumidity(median_value);
    } else if (median_value < (int) minHum) {
        exceedHumidity(median_value);
    }
}

void check_for_alerts() {
    int tempMedian, humMedian;

    if (!is_sufficient_data(tempVec, BUFFER_LENGTH, &tail, &head)) {
        error("Insufficient data for temperature median calculation.");
        return;
    }

    if (!calculate_median(tempVec, MEDIAN_WINDOW, &tempMedian)) {
        error("Failed to calculate temperature median.");
        return;
    }
    check_temperature_alerts(tempMedian);

    if (!is_sufficient_data(humVec, BUFFER_LENGTH, &tail, &head)) {
        error("Insufficient data for humidity median calculation.");
        return;
    }

    if (!calculate_median(humVec, MEDIAN_WINDOW, &humMedian)) {
        error("Failed to calculate humidity median.");
        return;
    }
    check_humidity_alerts(humMedian);
}
