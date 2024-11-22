#include <stdio.h>
#include "asm.h"

int buffer[5] = {0};
int length = 5;  
int tail = 0;
int head = 0 ;          
int value;

int main() {
    for (int i = 1; i <= 10; i++) {
        value = i;
        int res = enqueue_value(buffer, length, &tail, &head, value);
        printf("Value = %d   || { ", value);

        for (int j = 0; j < length; j++) {
            printf("%d, ", buffer[j]);
        }
        printf("} || res = %d\n", res);
    }
    return 0;
}
