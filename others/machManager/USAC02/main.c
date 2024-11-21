#include <stdio.h>
#include "asm.h"

int main() {
    int value = 1; // 011010
	char bits[5];
	int res = get_number_binary(value, bits);

	if (res == 1) {
		printf("%d: %d, %d, %d, %d, %d\n", res, bits[4], bits[3], bits[2], bits[1], bits[0]);
	} else {
		printf("%d:\n", res);
	}

    
    int value1 = 32; // 0:
    char bits1[5] = {0};         
    int res1 = get_number_binary(value1, bits1);  

    if (res1 == 1) {
		printf("%d: %d, %d, %d, %d, %d\n", res1, bits[4], bits[3], bits[2], bits[1], bits[0]);
	} else {
		printf("%d:\n", res1);
	}

    return 0;
}
