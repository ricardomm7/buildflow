#include <stdio.h>
#include "asm.h"

int main() {
    int value = 26;             // Número de entrada para teste
    char bits[5] = {0};         // Inicializa o array de bits com 0 para evitar dados não inicializados
    int res = get_number_binary(value, bits);  // Chamada da função em Assembly

    // Verifica se a função teve sucesso
    if (res == 1) {
        // Imprime o resultado
        printf("%d: %c, %c, %c, %c, %c\n", res, bits[4], bits[3], bits[2], bits[1], bits[0]);
        // Esperado: 1: 1, 1, 0, 1, 0
    } else {
        printf("Erro: Número fora do intervalo.\n");
    }

    return 0;
}
