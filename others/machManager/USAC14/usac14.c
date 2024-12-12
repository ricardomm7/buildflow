#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_NAME_LENGTH 50
#define MAX_STATE_LENGTH 10
#define MAX_OPERATION_NAME_LENGTH 50
#define MAX_TIMESTAMP_LENGTH 20

// Structure to represent an operation
typedef struct {
    char operationName[MAX_OPERATION_NAME_LENGTH]; // Operation name
    char state[MAX_STATE_LENGTH];                  // State ("OP", "ON", "OFF")
    char timestamp[MAX_TIMESTAMP_LENGTH];          // Timestamp
    float temperature;                             // Recorded temperature
    float humidity;                                // Recorded humidity
    int operationNumber;                           // Operation number (0-31)
} Operation;

// Structure to represent a machine
typedef struct Machine {
    int id;                     // Identifier
    int minTemp;                // Minimum temperature
    int maxTemp;                // Maximum temperature
    int minHumidity;            // Minimum humidity
    int maxHumidity;            // Maximum humidity
    int bufferLength;           // Circular buffer size
    int medianWindow;           // Median window size
    char name[MAX_NAME_LENGTH]; // Machine name
    Operation *operations;      // Dynamic memory for operations
    struct Machine *next;       // Pointer to the next machine in the list
} Machine;

// Head of the linked list
Machine *head = NULL;

// Function to add a machine to the plant floor
void addMachine() {
    int id, minTemp, maxTemp, minHumidity, maxHumidity, bufferLength, medianWindow;
    char name[MAX_NAME_LENGTH];
    
    printf("Enter machine ID: ");
    scanf("%d", &id);
    printf("Enter machine name: ");
    scanf("%s", name);
    printf("Enter minimum temperature: ");
    scanf("%d", &minTemp);
    printf("Enter maximum temperature: ");
    scanf("%d", &maxTemp);
    printf("Enter minimum humidity: ");
    scanf("%d", &minHumidity);
    printf("Enter maximum humidity: ");
    scanf("%d", &maxHumidity);
    printf("Enter buffer length: ");
    scanf("%d", &bufferLength);
    printf("Enter median window size: ");
    scanf("%d", &medianWindow);

    // Allocate memory for the new machine
    Machine *newMachine = (Machine *)malloc(sizeof(Machine));
    if (newMachine == NULL) {
        printf("Memory allocation failed.\n");
        return;
    }
    // Initialize machine properties
    newMachine->id = id;
    strncpy(newMachine->name, name, MAX_NAME_LENGTH);
    newMachine->minTemp = minTemp;
    newMachine->maxTemp = maxTemp;
    newMachine->minHumidity = minHumidity;
    newMachine->maxHumidity = maxHumidity;
    newMachine->bufferLength = bufferLength;
    newMachine->medianWindow = medianWindow;
    newMachine->operations = NULL; // Initialize operations to NULL
    newMachine->next = head;       // Insert at the beginning of the list
    head = newMachine;
    printf("Machine '%s' added successfully.\n", name);
}

// Function to remove a machine from the plant floor
void removeMachine() {
    int id;
    printf("Enter the machine ID to remove: ");
    scanf("%d", &id);

    Machine *current = head;
    Machine *previous = NULL;
    while (current != NULL) {
        if (current->id == id) {
            // Check if the machine is not operating
            if (current->operations != NULL && strcmp(current->operations->state, "OP") == 0) {
                printf("Machine '%s' is currently operating and cannot be removed.\n", current->name);
                return;
            }
            // Remove the machine from the list
            if (previous == NULL) {
                head = current->next;
            } else {
                previous->next = current->next;
            }
            free(current->operations); // Free operations memory if allocated
            free(current);             // Free machine memory
            printf("Machine with ID %d removed successfully.\n", id);
            return;
        }
        previous = current;
        current = current->next;
    }
    printf("Machine with ID %d not found.\n", id);
}

// Function to read the current status of a machine
void readMachineStatus() {
    int id;
    printf("Enter the machine ID to view status: ");
    scanf("%d", &id);

    Machine *current = head;
    while (current != NULL) {
        if (current->id == id) {
            printf("Machine ID: %d\n", current->id);
            printf("Name: %s\n", current->name);
            printf("Temperature Range: %d - %d\n", current->minTemp, current->maxTemp);
            printf("Humidity Range: %d - %d\n", current->minHumidity, current->maxHumidity);
            printf("Buffer Length: %d\n", current->bufferLength);
            printf("Median Window: %d\n", current->medianWindow);
            if (current->operations != NULL) {
                printf("Last Operation: %s\n", current->operations->operationName);
                printf("State: %s\n", current->operations->state);
                printf("Timestamp: %s\n", current->operations->timestamp);
                printf("Temperature: %.2f\n", current->operations->temperature);
                printf("Humidity: %.2f\n", current->operations->humidity);
            } else {
                printf("No operations recorded.\n");
            }
            return;
        }
        current = current->next;
    }
    printf("Machine with ID %d not found.\n", id);
}

// Function to free all allocated memory
void freeMachines() {
    Machine *current = head;
    while (current != NULL) {
        Machine *temp = current;
        current = current->next;
        free(temp->operations); // Free operations memory if allocated
        free(temp);             // Free machine memory
    }
    head = NULL;
}

int main() {
    int choice;
    do {
        printf("\nPlant Floor Management Menu:\n");
        printf("1. Add a Machine\n");
        printf("2. Remove a Machine\n");
        printf("3. View Machine Status\n");
        printf("4. Exit\n");
        printf("Enter your choice: ");
        scanf("%d", &choice);

        switch (choice) {
            case 1:
                addMachine();
                break;
            case 2:
                removeMachine();
                break;
            case 3:
                readMachineStatus();
                break;
            case 4:
                printf("Exiting program.\n");
                freeMachines();
                break;
            default:
                printf("Invalid choice. Please try again.\n");
        }
    } while (choice != 4);

    return 0;
}
