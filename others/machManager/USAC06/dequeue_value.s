.section .text
.global dequeue_value

# Parâmetros:
# %rdi --> array (int*)
# %rsi --> length (int)
# %rdx --> read (int*)
# %rcx --> write (int*)
# %r8  --> value (int*)

dequeue_value:
    # Prólogo: guarda o estado da stack
    pushq %rbp                   # Salva o valor do base pointer (bp)
    movq %rsp, %rbp              # Alinha o primeiro pointer ao stack pointer
    subq $16, %rsp               # Reserva espaço na stack

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
    # Epílogo: restaura o estado original da stack
    movq %rbp, %rsp              # Restaura o stack pointer ao primeiro pointer
    popq %rbp                    # Restaura o primeiro pointer
    ret
