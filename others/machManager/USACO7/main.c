#include <stdio.h>
#include "asm.h"

#include <stdio.h>
#include "asm.h"

int buffer[5] = {1,3,5,4,0};
int length = 5;  
int tail = 0;
int head = 0 ;          
int value;
int main() {
    int res = get_n_element(buffer,length, &tail,&head);
    printf("tamanho =%d\n" ,length);
    printf("valores alocados =%d\n",res);
    return 0;
}
