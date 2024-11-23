#include <stdio.h>
#include "func.h"

void exibirMenu() {
    printf("===== BUILDFLOW MACHINE MANAGER MENU =====\n");
    printf("1. Extract data.\n");
    printf("2. Number in binary.\n");
    printf("3. Extract a number from a string.\n");
    printf("4. Format command.\n");
    printf("5. Enqueue value.\n");
    printf("6. Dequeue value.\n");
    printf("7. Get occupied size on buffer.\n");
    printf("8. Remove from buffer.\n");
    printf("9. Sort array.\n");
    printf("10. Calculate median.\n");
    printf("0. Sair\n");
    printf("==========================================\n");
    printf("Choose an option: ");
}

int main() {
    int escolha;
    do {
        exibirMenu();
        scanf("%d", &escolha);

        switch (escolha) {
			case 1: {
				char str[100];
				char token[20];
				char unit[20];
				int value;

				printf("Enter the data string (e.g., TEMP&unit:celsius&value:20#HUM&unit:percentage&value:80):\n");
				scanf(" %[^\n]s", str);

				printf("Enter the token to search for (e.g., TEMP):\n");
				scanf("%s", token);

				if (extract_data(str, token, unit, &value)==1) {
					printf("Extraction successful:\n");
					printf("Unit: %s\n", unit);
					printf("Value: %d\n", value);
				} else {
					printf("Failed to extract data for token '%s'.\n", token);
				}
				break;
			}
			case 2: {
				int n;
				char bits[5];

				printf("Enter a number in the range [0, 31]: ");
				scanf("%d", &n);

				if (n < 0 || n > 31) {
					printf("Invalid number. Please enter a number between 0 and 31.\n");
					break;
				}

				if (get_number_binary(n, bits)==1) {
					printf ( "%d, %d, %d, %d, %d\n", bits[4], bits[3], bits[2], bits[1], bits[0]);
				} else {
					printf("Failed to calculate binary representation.\n");
				}
				break;
			}
			case 3: {
				char string[100];
				int n;

				printf("Enter a string: ");
				scanf("%s", string);

				if (get_number(string, &n) == 1) {
					printf("The number extracted is: %d\n", n);
				} else {
					printf("Failed to calculate number representation.\n");
				}

				break;
			}
			case 4: {
				char op[20];
				int n;
				char cmd[20];

				printf("Enter the operation string (e.g., ON, OFF, OP): ");
				scanf(" %[^\n]s", op);

				printf("Enter an integer (0 to 31): ");
				scanf("%d", &n);

				if (n < 0 || n > 31) {
					printf("The number must be in the range [0, 31].\n");
					break;
				}

				if (format_command(op, n, cmd)==1) {
					printf("Formatted command: %s\n", cmd);
				} else {
					printf("Failed to format the command.\n");
				}
				break;
			}
			case 5: {
				int length;

				printf("Enter the length of the buffer: ");
				scanf("%d", &length);

				if (length <= 0) {
					printf("The buffer length must be greater than 0.\n");
					break;
				}

				int buffer[length];
				int head = 0;
				int tail = 0;

				printf("Enter %d elements for the buffer:\n", length);
				for (int i = 0; i < length; i++) {
					printf("Element %d: ", i + 1);
					scanf("%d", &buffer[i]);
					head = (head + 1) % length;
				}
				
				for (int i = 0; i < length; i++) {
					if (buffer[i] == 0) {
						head = i;
						break;
					}
				}

				int value;
				printf("Enter a value to enqueue: ");
				scanf("%d", &value);

				if (enqueue_value(buffer, length, &tail, &head, value) == 1) {
					printf("Buffer is full after enqueuing value: %d\n", value);
					for (int i = 0; i < length; i++) {
                        printf("%d ", buffer[i]);
                    }
				} else {
					printf("Value enqueued successfully: %d\n", value);
					for (int i = 0; i < length; i++) {
                        printf("%d ", buffer[i]);
                    }
				}
				break;
			}
			case 6: {
				int length;

				printf("Enter the length of the buffer: ");
				scanf("%d", &length);

				if (length <= 0) {
					printf("The buffer length must be greater than 0.\n");
					break;
				}

				int buffer[length];
				int head = length-1;
				int tail = 0;

				printf("Enter %d elements for the buffer:\n", length);
				for (int i = 0; i < length; i++) {
					printf("Element %d: ", i + 1);
					scanf("%d", &buffer[i]);
				}

				int value;
				if (dequeue_value(buffer, length, &tail, &head, &value)==1) {
					printf("Dequeued value: %d\n", value);
				} else {
					printf("Failed to dequeue a value. The buffer might be empty.\n");
				}
				break;
			}
			case 7: {
                int size;
                printf("What is the buffer size: ");
                scanf("%d", &size);

                if (size <= 0) {
                    printf("The buffer size should be greater than 0.\n");
                    break;
                }

                int buffer[size];
                int tail = 0;
                int head = 0;

                printf("Place %d buffer elements:\n", size);
                for (int i = 0; i < size; i++) {
                    printf("Element %d: ", i + 1);
                    scanf("%d", &buffer[i]);
                }
                
				for (int i = 0; i < size; i++) {
					if (buffer[i] == 0) {
						head = i;
						break;
					}
				}

                int result = get_n_element(buffer, size, &tail, &head);

				printf("The occupied size in the buffer is: %d", result);
				break;
            }
            case 8: {
                int size, n;
                printf("What is the buffer size: ");
                scanf("%d", &size);

                if (size <= 0) {
                    printf("The buffer size should be greater than 0.\n");
                    break;
                }

                int buffer[size];

                printf("Place %d buffer elements:\n", size);
                for (int i = 0; i < size; i++) {
                    printf("Element %d: ", i + 1);
                    scanf("%d", &buffer[i]);
                }

                printf("How many elements do you want to remove: ");
                scanf("%d", &n);

                if (n < 0 || n > size) {
                    printf("The number of elements to remove should be between 0 and %d.\n", size);
                    break;
                }

                int tail = 0;
                int head = size - 1;
                int array[n];

                int result = move_n_to_array(buffer, size, &tail, &head, n, array);

                if (result == 0) {
                    printf("The removal failed!\n");
                } else if (result == 1) {
                    printf("The removal succeeded! Elements removed:\n");
                    for (int i = 0; i < n; i++) {
                        printf("%d ", array[i]);
                    }
                    printf("\n");
                }
                break;
            }
			case 9: {
				int size2;
				char order2;

				printf("Enter the size of the array: ");
				scanf("%d", &size2);

				if (size2 <= 0) {
					printf("Array size must be greater than 0.\n");
					break;
				}

				int array2[size2];

				printf("Enter %d elements for the array:\n", size2);
				for (int y = 0; y < size2; y++) {
					printf("Element %d: ", y + 1);
					scanf("%d", &array2[y]);
				}

				printf("Enter the order (1 for ascending, 0 for descending): ");
				scanf(" %c", &order2);

				if (order2 != '1' && order2 != '0') {
					printf("Invalid order. Enter 1 for ascending or 0 for descending.\n");
					break;
				}

				if (sort_array(array2, size2, order2) == 1) {
					printf("Sorted array: ");
					for (int i = 0; i < size2; i++) {
						printf("%d ", array2[i]);
					}
					printf("\n");
				} else {
					printf("Sorting failed.\n");
				}
				break;
			}

			case 10: {
				int size3;

				printf("Enter the size of the array: ");
				scanf("%d", &size3);

				if (size3 <= 0) {
					printf("Array size must be greater than 0.\n");
					break;
				}

				int array3[size3];

				printf("Enter %d elements for the array:\n", size3);
				for (int y = 0; y < size3; y++) {
					printf("Element %d: ", y + 1);
					scanf("%d", &array3[y]);
				}

				int me;

				if (median(array3, size3, &me) == 1) {
					printf("The median is: %d\n", me);
				} else {
					printf("Median calculation failed.\n");
				}
				break;
			}
            case 0:
                printf("Goodbye :)\n");
                break;

            default:
                printf("Invalid option. Try again.\n");
                break;
        }
        printf("\n");
    } while (escolha != 0);

    return 0;
}
