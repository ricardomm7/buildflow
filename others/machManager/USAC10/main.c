#include <stdio.h>
#include "func.h"

int main() {
	int buffer[] = {3,8,12,36,40,79,109,220,775, 1024};
	int me = 0;
	int le = 10;
	
	int result = median(buffer, le, &me);
	
	printf("\n\nOutput is %d.\n\n", result);
	
	printf("\n\nThe median is %d.\n\n", me);
}
