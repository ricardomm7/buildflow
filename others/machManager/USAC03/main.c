#include <stdio.h>
#include "asm.h"


int n = 0;
char str [] = " 89 ";
char str2 [] = " 8 - -9 ";
int main() {
	
 	
 	int res = get_number ( str , &n) ;
 	printf ( "%d: %d\n" ,res , n ); // 1: 89
 	
 	res = get_number ( str2 , &n ) ;
 	printf ( "%d: %d\n" ,res , n ); // 0:
    return 0;
}
