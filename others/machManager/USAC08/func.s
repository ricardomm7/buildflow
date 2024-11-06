.global move_n_to_array

# Parametros esperados:
# buffer - %rdi
# length - %rsi
# tail   - %rdx
# head   - %rcx
# n      - %r8
# array  - %r9

move_n_to_array:
    xor %rbx, %rbx  # inicializar rbx para 0

loop_start:
    cmp %r8, %rbx  # compara contador %rbx com o número de elementos %r8
    jge verify  # se %rbx >= %r8, sai do loop

    # Move o valor de buffer[rbx] para array[rbx]
    mov (%rdi, %rbx, 4), %rax  # carrega buffer[rbx] em %rax
    mov %rax, (%r9, %rbx, 4)  # armazena em array[rbx]

    inc %rbx # incrementa o contador
    jmp loop_start # volta ao início do loop

verify:
    # Verifica se todos os elementos foram movidos
    cmp %r8, %rbx  # verifica se movemos exatamente 'n' elementos
    jne fail  # se não, retorna 0 (falha)

success:
    movl $1, %eax  # coloca 1 em %eax para indicar sucesso
    jmp end

fail:
    movl $0, %eax  # coloca 0 em %eax para indicar falha

end:
    ret
