.section .text
    .global extract_data

extract_data:
    # Prólogo
    push %rbp                  
    mov %rsp, %rbp             

    # Parâmetros de entrada:
    # %rdi = str --> ponteiro char[]
    # %rsi = token --> ponteiro char[]
    # %rdx = unit --> ponteiro char[]
    # %rcx = value  --> ponteiro int

    #Falta só criar uma função de incio que verifica se rdi está correto e sai se não tiver
    #Falta tbm corrigir um erro porque o valor sai 2 e nãa 20 o que se deve á imul de 0
    #ou seja temos de por uma linha que caso o caracter seja zero os numero são multiplicados só por 10 e não adicionados

find_token:
    movb (%rdi), %al
    movb (%rsi), %bl
    cmp %al, %bl
    je find_index_unit
    inc %rdi
    jmp find_token

find_index_unit:
    movb (%rdi), %al
    cmp $'T', %al
    jne token_H
    addq $10, %rdi               # Avança o ponteiro str em 10 posições (pula para a unidade)
    jmp save_unit

token_H:
    cmp $'H', %al
    addq $9, %rdi

save_unit:
    movb (%rdi), %al
    cmp $'&', %al                # Verifica se o caractere atual é '&' (final da unidade)
    je find_index_value
    movb %al, (%rdx)
    inc %rdi
    inc %rdx
    jmp save_unit

find_index_value:
    addq $7, %rdi                # Avança o ponteiro str para o início do valor numérico

save_value:
    movb (%rdi), %al
    cmp $'#', %al
    je end
    cmp $'\0', %al
    je end

    sub $'0', %al                # Converte caractere ASCII para número (0-9)
    imul $10, %r8d               # Multiplica o valor acumulado por 10
    add %al, %r8b                # Adiciona o dígito convertido ao acumulador de valor

    inc %rdi
    jmp save_value

end:
    mov %r8d, (%rcx)             # Armazena o valor final em *value
    mov $1, %eax                 # Define o valor de retorno como 1 (sucesso)

    # Epílogo
    mov %rbp, %rsp
    pop %rbp
    ret
