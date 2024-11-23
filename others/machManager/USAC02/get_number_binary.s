.section .text
.global get_number_binary

get_number_binary:
   # Prólogo - Guardar apenas registos necessários
    push %r8             # Guarda o registo r8 na stack
    push %r9             # Guarda o registo r9 na stack
    push %rdi            # Guarda o registo rdi na stack
    push %rsi            # Guarda o registo rsi na stack
    push %rdx            # Guarda o registo rdx na stack

    mov %edi, %ecx              # Carrega o argumento n em ecx (edi contém o número)
    mov %rsi, %rdx              # Carrega o ponteiro bits em rdx (rsi contém o array)

    cmp $31, %ecx               # Verifica se n > 31
    ja out_of_range             # Se sim, vai para out_of_range

    cmp $0, %ecx                # Verifica se n < 0
    jl out_of_range             # Se sim, vai para out_of_range

    mov $0, %rax                # Inicializa rax como índice do array (0)
    mov $5, %eax                # Define o contador de bits (5 bits para o intervalo [0, 31])

convert_bits:
    mov %ecx, %r9d              # Move o numero para %r9d
    and $1, %r9d                # Operação and bit a bit para isolar o bit menos significativo (LSB)
    movb %r9b, (%rdx)           # Armazena o bit no buffer de saída
    inc %rdx                    # Incrementa o ponteiro para gravar na próxima posição
    shr $1, %ecx                # Desloca n para a direita para processar o próximo bit
    dec %eax                    # Decrementa o contador de bits
    jnz convert_bits            # Continua até que todos os 5 bits sejam processados

    mov $1, %eax                # Define retorno como 1 (sucesso)
    jmp end                     # Salta para o final

out_of_range:
    mov $0, %eax                # Define retorno como 0 (erro)

end:
   # Epílogo - Restaura registos
    pop %rdx             # Restaura rdx
    pop %rsi             # Restaura rsi
    pop %rdi             # Restaura rdi
    pop %r9              # Restaura r9
    pop %r8              # Restaura r8
    ret                   # Retorna
