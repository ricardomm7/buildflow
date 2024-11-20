#include <stdio.h>
#include "func.h"

int main() {
	int buffer[] = {3,8,12,36,40,78,109,220,775};
	int me = 0;
	int le = 9;
	
	int result = median(buffer, le, &me);
	
	printf("\n\nThe median is %d.\n\n", result);
}
