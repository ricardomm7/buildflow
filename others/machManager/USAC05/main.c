#include <stdio.h>
#include "asm.h"

int main() {

	int length = 5;  // Length of the buffer
	int buffer[5] = {0};  // Buffer to store the values, initialized to 0

	int head = 0;
	int tail = 0;

	printf("Initial buffer: ");
	for (int i = 0; i < length; i++) {
		printf("%d ", buffer[i]);
	}
	printf("\n");

	printf("Inserting values into the buffer:\n");

	for (int i = 1; i <= 7; i++) {  // We try to insert 7 values, more than the buffer length
		printf("Inserting %d: ", i);
		int result = enqueue_value(buffer, length, &tail, &head, i);  // Call the assembly function

		printf("Buffer after insertion: ");
		for (int j = 0; j < length; j++) {
			printf("%d ", buffer[j]);
		}
		printf("\n");

		if (result == 1) {
			printf("Buffer is full after inserting %d.\n", i);
		} else {
			printf("Buffer is not full after inserting %d.\n", i);
		}
	}

	return 0;
}
