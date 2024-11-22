.global median
# Parametros esperados:
# vec - %rdi
# length - %esi
# me   - %rdx
median:    
	# Prólogo
    pushq %rbp           # Guarda o base pointer antigo
    movq %rsp, %rbp     # Estabelece novo base pointer
    pushq %rbx          # Salva registradores callee-saved
    pushq %r12
    pushq %r13
    
    cmp $0, %esi
    jle end_bad
    
    push %rsi
    push %rdx
    movq $1, %rdx
    call sort_array
    
    cmp $0, %eax
    je end_bad
    pop %rdx
    pop %rsi
    testl $1, %esi
    jnz is_odd
    
is_even:
    movl %esi, %ecx
    sarl $1, %ecx           # Usa sarl ao invés de shr
    leaq (%rdi, %rcx, 4), %rax
    movl (%rax), %eax
    subl $1, %ecx
    leaq (%rdi, %rcx, 4), %r9
    movl (%r9), %r9d
    # Soma com sinal
    addl %r9d, %eax
    # Divisão por 2 preservando o sinal
    sarl $1, %eax          # Usa sarl ao invés de shrl
    movl %eax, (%rdx)
    movl $1, %eax
    jmp end
    
is_odd:
    movl %esi, %ecx
    sarl $1, %esi          # Usa sarl ao invés de shr
    leaq (%rdi, %rsi, 4), %rax
    movl (%rax), %eax
    movl %eax, (%rdx)
    movl $1, %eax
end:
	# Epílogo
    popq %r13           # Restaura registradores callee-saved
    popq %r12
    popq %rbx
    movq %rbp, %rsp     # Restaura stack pointer
    popq %rbp           # Restaura base pointer antigo
    
    ret
    
end_bad:
	# Epílogo
    popq %r13           # Restaura registradores callee-saved
    popq %r12
    popq %rbx
    movq %rbp, %rsp     # Restaura stack pointer
    popq %rbp           # Restaura base pointer antigo
    
    movl $0, %eax
    ret
