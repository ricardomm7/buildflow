.section .text
.global dequeue_value

# Parâmetros:
# %rdi --> array (int*)
# %rsi --> length (int)
# %rdx --> read (int*)
# %rcx --> write (int*)
# %r8  --> value (int*)

dequeue_value:
    # Prólogo - Guardar apenas registos necessários
    push %r8             # Guarda o registo r8 na stack
    push %r9             # Guarda o registo r9 na stack
    push %rdi            # Guarda o registo rdi na stack
    push %rsi            # Guarda o registo rsi na stack
    push %rdx            # Guarda o registo rdx na stack

    # Lógica da função
    movl (%rdx), %eax            # eax = read
    cmpl (%rcx), %eax            # compara read com write
    je empty                     # se iguais, buffer vazio

    movl (%rdi,%rax,4), %eax     # eax = array[read]
    movl %eax, (%r8)             # *value = eax

    movl (%rdx), %eax
    addl $1, %eax                # read++

    cmpl %esi, %eax              # compara com length
    jne no_reset
    movl $0, %eax                # coloca o read a 0

no_reset:
    movl %eax, (%rdx)            # atualiza read
    movl $1, %eax                # retorna sucesso
    jmp end

empty:
    movl $0, %eax                # retorna falha

end:
    # Epílogo - Restaura registos
    pop %rdx             # Restaura rdx
    pop %rsi             # Restaura rsi
    pop %rdi             # Restaura rdi
    pop %r9              # Restaura r9
    pop %r8              # Restaura r8
    ret                   # Retorna
