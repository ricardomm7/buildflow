#include <stdio.h>
#include "asm.h"

char str [] ="TEMP&unit:celsius&value:20#HUM&unit:percentage&value:80";
char token [] ="TEMP" ;
char unit [20];
int value;

int main() {
    
    int res = extract_data ( str , token , unit , &value );
	printf ( "%d: %s,%d \n" , res , unit , value ); 
	char token2 [] = "AAA";
	res = extract_data (str, token2,unit,&value);
	printf ( "%d: %s,%d \n" , res , unit , value ); //0: ,0
    
    return 0;
}
