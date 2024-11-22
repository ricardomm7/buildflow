#include <stdio.h>
#include "asm.h"

int main() {
    char cmd1[20];
    char cmd2[20];
    char cmd23[20];
    char cmd3[20];


    // Teste com comando válido "ON" e valor 26
    char op1[] = " oN ";
    int value1 = 26;  // Binário: 11010
    int res = format_command(op1, value1, cmd1);
    printf("%d: %s\n", res, cmd1);  // Esperado: 1: ON,1,1,0,1,0

    // Teste com comando válido "OFF" e valor 19
    char op2[] = " O  f F ";
    int value2 = 19;  // Binário: 10011
    int res2 = format_command(op2, value2, cmd2);
    printf("%d: %s\n", res2, cmd2);  // Esperado: 1: OFF,1,0,0,1,1
    
    char op23[] = "O  p";
    int value23 = 1; // Binário : 00001
    int res23 = format_command(op23, value23, cmd23);
    printf("%d: %s\n", res23, cmd23);  // Esperado: 1: OP,0,0,0,0,1

    // Teste com comando inválido "aaa" e valor 5
    char op3[] = " aaa ";
    int value3 = 5;  // Valor irrelevante para comando inválido
    int res3 = format_command(op3, value3, cmd3);
    printf("%d: %s\n", res3, cmd3);  // Esperado: 0:

    return 0;
}
