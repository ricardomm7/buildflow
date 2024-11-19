#include <stdio.h>
#include "func.h"

int main() {
	int array[] = {132,3214,41234,1,23,424,13,53546,124,9765};
	int size=10;
	int me;

	median(array, size, &me);

	printf("\n\nThe result is %d.\n\n", me);
}
