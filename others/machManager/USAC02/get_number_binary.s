.section .data
.global get_number_binary

.section .text
.global get_number_binary
get_number_binary:
    # Prólogo
    push %rbp
    mov %rsp, %rbp
    sub $16, %rsp               # Reserva espaço na pilha

    mov %edi, %ecx              # Carrega o argumento n em ecx
    mov %rsi, %rdx              # Carrega o ponteiro bits em rdx

    # Verificação do intervalo de n (0 a 31)
    cmp $31, %ecx               # Compara n com 31
    ja out_of_range             # Se n > 31, salta para out_of_range

    # Loop para obter os bits (LSB para MSB)
    mov $5, %eax                # Configura o contador de 5 bits

convert_bits:
    mov %ecx, %ebx              # Carrega n em ebx temporariamente
    and $1, %ebx                # Aplica a máscara para extrair o bit menos significativo (LSB)
    add $48, %bl                # Converte o bit para caractere ASCII ('0' ou '1')
    movb %bl, (%rdx)            # Armazena o bit atual em bits
    inc %rdx                    # Avança o ponteiro de bits para a próxima posição
    shr $1, %ecx                # Desloca n para a direita para obter o próximo bit
    dec %eax                    # Decrementa o contador de bits
    jnz convert_bits            # Continua até completar 5 bits

    mov $1, %eax                # Define retorno como 1 (sucesso)
    jmp end                     # Salta para o fim

out_of_range:
    mov $0, %eax                # Define retorno como 0 (erro)

end:
    # Epílogo
    mov %rbp, %rsp
    pop %rbp
    ret
