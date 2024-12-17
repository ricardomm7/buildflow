#include <stdio.h>
#include <stdlib.h>
#include "func.h"

int main() {
    int choice;
    do {
        printf("\nBUILDFLOW MACHMANAGER MAIN MENU\n");
        printf("1. List Machines\n");
        printf("2. Add Machine\n");
        printf("3. Remove Machine\n");
        printf("4. Assign Operation to Machine\n");
        printf("5. Save Machines\n");
        printf("6. Save Operations\n");
        printf("7. Create New Operation\n");
        printf("8. Export Machine Operations to CSV\n");
        printf("0. Exit\n");
        printf("Choose an option: ");
        scanf("%d", &choice);

        switch(choice) {
            case 1: { break;}
            case 2: { break;}
            case 3: { break;}
            
            case 0: { return 0;}
        }
    } while (choice != 0);
    return 0;
}
