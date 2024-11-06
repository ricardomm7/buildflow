#include <stdio.h>
#include "func.h"

int main() {
	int buffer[] = {132,3214,41234,1,23,424,13,53546,124,9765};
	int size=10;
	int n = 5;
	int array[n];
	
	int result = move_n_to_array(buffer, size,&buffer[9],&buffer[0],n,array);
	
	printf("\n\nThe result is %d.\n\n", result);
	
	printf("Array contents: \n");
    for (int i = 0; i < n; i++) {
        printf("%d ", array[i]);
    }
    printf("\n\n");
    
    printf("Inicial Array contents: \n");
    for (int i = 0; i < size; i++) {
        printf("%d ", buffer[i]);
    }
    printf("\n\n");
}
