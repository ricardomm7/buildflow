#include <stdio.h>
#include "asm.h"

#define BUFFER_LENGTH 5

// Função auxiliar para imprimir o estado atual do buffer
void print_buffer(int* buffer, int length, int head, int tail) {
    printf("Buffer: [");
    for (int i = 0; i < length; i++) {
        printf("%d", buffer[i]);
        if (i < length - 1) printf(", ");
    }
    printf("]  (head=%d, tail=%d)\n", head, tail);
}

int main() {
    int buffer[BUFFER_LENGTH] = {0};
    int head = 0, tail = 0;
    int value;

    printf("\n=== Teste 1: Encher o buffer ===\n");
    for (int i = 1; i <= 6; i++) {
        printf("\nTentando inserir: %d\n", i);
        int result = enqueue_value(buffer, BUFFER_LENGTH, &tail, &head, i);
        print_buffer(buffer, BUFFER_LENGTH, head, tail);
        if (result) {
            printf("Buffer cheio após inserir %d\n", i);
        } else {
            printf("Valor %d inserido com sucesso\n", i);
        }
    }

    printf("\n=== Teste 2: Remover alguns elementos ===\n");
    for (int i = 0; i < 3; i++) {
        printf("\nTentando remover elemento...\n");
        if (dequeue_value(buffer, BUFFER_LENGTH, &tail, &head, &value)) {
            printf("Removido: %d\n", value);
        } else {
            printf("Buffer vazio!\n");
        }
        print_buffer(buffer, BUFFER_LENGTH, head, tail);
    }

    printf("\n=== Teste 3: Inserir novos elementos ===\n");
    for (int i = 10; i <= 12; i++) {
        printf("\nTentando inserir: %d\n", i);
        int result = enqueue_value(buffer, BUFFER_LENGTH, &tail, &head, i);
        print_buffer(buffer, BUFFER_LENGTH, head, tail);
        if (result) {
            printf("Buffer cheio após inserir %d\n", i);
        } else {
            printf("Valor %d inserido com sucesso\n", i);
        }
    }

    printf("\n=== Teste 4: Esvaziar o buffer ===\n");
    while (1) {
        printf("\nTentando remover elemento...\n");
        if (dequeue_value(buffer, BUFFER_LENGTH, &tail, &head, &value)) {
            printf("Removido: %d\n", value);
            print_buffer(buffer, BUFFER_LENGTH, head, tail);
        } else {
            printf("Buffer vazio!\n");
            print_buffer(buffer, BUFFER_LENGTH, head, tail);
            break;
        }
    }

    printf("\n=== Teste 5: Operações com buffer vazio ===\n");
    printf("\nTentando remover de buffer vazio...\n");
    if (dequeue_value(buffer, BUFFER_LENGTH, &tail, &head, &value)) {
        printf("Removido: %d\n", value);
    } else {
        printf("Buffer vazio!\n");
    }
    print_buffer(buffer, BUFFER_LENGTH, head, tail);

    printf("\n=== Teste 6: Ciclo completo do buffer ===\n");
    for (int i = 20; i <= 25; i++) {
        printf("\nTentando inserir: %d\n", i);
        int result = enqueue_value(buffer, BUFFER_LENGTH, &tail, &head, i);
        print_buffer(buffer, BUFFER_LENGTH, head, tail);
        if (result) {
            printf("Buffer cheio após inserir %d\n", i);
        } else {
            printf("Valor %d inserido com sucesso\n", i);
        }
    }

    return 0;
}
