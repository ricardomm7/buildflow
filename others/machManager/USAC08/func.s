.global move_n_to_array

# Parametros esperados:
# buffer - %rdi
# length - %esi
# tail   - %rdx
# head   - %rcx
# n      - %r8d
# array  - %r9

move_n_to_array:
    movl $0, %r10d

loop_start:
    cmpl %r8d, %r10d  # compara contador %r10d com o número de elementos %r8d
    jge verify  # se %r10d >= %r8d, sai do loop

    movl (%rdi, %r10, 4), %eax  # carrega buffer[r10d] em %eax
    movl %eax, (%r9, %r10, 4)  # armazena em array[r10d]

    incl %r10d # incrementa o contador
    jmp loop_start # volta ao início do loop

verify:
    cmpl %r8d, %r10d  # verifica se movemos exatamente 'n' elementos
    jne fail  # se não, retorna 0 (falha)

success:
    movl $1, %eax  # coloca 1 em %eax para indicar sucesso
    jmp end

fail:
    movl $0, %eax  # coloca 0 em %eax para indicar falha

end:
    ret
