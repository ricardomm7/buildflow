.section .text
.global extract_data
extract_data:
    # Prólogo
    pushq %rbp
    movq %rsp, %rbp
    
    # Verificar parâmetros nulos ou vazios
    cmpq $0, %rdi              # Verificar str
    je error_return
    cmpq $0, %rsi              # Verificar token
    je error_return
    
    # Salvar registradores
    pushq %rbx
    pushq %r12
    pushq %r13
    pushq %r14
    pushq %r15
    
    # Guardar parâmetros
    movq %rdi, %r12            # str
    movq %rsi, %r13            # token
    movq %rdx, %r14            # unit
    movq %rcx, %r15            # value
    
    # Inicializar value com 0
    movl $0, (%r15)
    # Inicializar unit como string vazia
    movb $0, (%r14)
    
find_token:
    movq %r12, %rdi            # str atual
    movq %r13, %rsi            # token
    
compare_token:
    movb (%rsi), %al
    cmpb $0, %al               # Fim do token?
    je check_end_token         # Se sim, verificar se é token completo
    
    movb (%rdi), %bl
    cmpb $0, %bl               # Fim da string?
    je error_return
    
    cmpb %al, %bl
    jne next_char
    
    incq %rdi
    incq %rsi
    jmp compare_token
    
check_end_token:
    # Verificar se é um token exato (TEMP ou HUM)
    movb (%rdi), %al
    cmpb $'&', %al             # Próximo char deve ser '&'
    jne next_char
    
    # Verificar se é seguido por "unit:"
    cmpb $'u', 1(%rdi)
    jne error_return
    cmpb $'n', 2(%rdi)
    jne error_return
    cmpb $'i', 3(%rdi)
    jne error_return
    cmpb $'t', 4(%rdi)
    jne error_return
    cmpb $':', 5(%rdi)
    jne error_return
    
    # Token encontrado, procurar unit
    addq $6, %rdi              # Pular "&unit:"
    movq %r14, %rdx            # Restaurar ponteiro unit
    
copy_unit:
    movb (%rdi), %al
    cmpb $'&', %al             # Fim da unit?
    je find_value
    cmpb $0, %al               # Fim da string?
    je error_return
    
    movb %al, (%rdx)           # Copiar caractere
    incq %rdi
    incq %rdx
    jmp copy_unit
    
find_value:
    movb $0, (%rdx)            # Terminar string unit
    
    # Verificar "value:"
    cmpb $'v', 1(%rdi)
    jne error_return
    cmpb $'a', 2(%rdi)
    jne error_return
    cmpb $'l', 3(%rdi)
    jne error_return
    cmpb $'u', 4(%rdi)
    jne error_return
    cmpb $'e', 5(%rdi)
    jne error_return
    cmpb $':', 6(%rdi)
    jne error_return
    
    addq $7, %rdi              # Pular "&value:"
    movl $0, (%r15)           # Zerar o valor antes de começar
    
value_loop:
    xorl %ebx, %ebx           # Zerar ebx antes de cada dígito
    movb (%rdi), %bl
    cmpb $'#', %bl            # Fim do valor?
    je success_return
    cmpb $0, %bl              # Fim da string?
    je success_return
    
    subb $'0', %bl            # Converter ASCII para número
    movl (%r15), %eax         # Carregar valor atual
    imull $10, %eax           # Multiplicar por 10
    addl %ebx, %eax           # Adicionar novo dígito
    movl %eax, (%r15)         # Salvar novo valor
    
    incq %rdi
    jmp value_loop
    
next_char:
    incq %r12
    movb (%r12), %al
    cmpb $0, %al              # Verificar fim da string
    je error_return
    jmp find_token
    
error_return:
    # Limpar value e unit
    movl $0, (%r15)           # Usar movl para value
    movb $0, (%r14)           # Usar movb para unit
    xorl %eax, %eax           # Retornar 0
    jmp cleanup
    
success_return:
    movl $1, %eax             # Retornar 1
    
cleanup:
    popq %r15
    popq %r14
    popq %r13
    popq %r12
    popq %rbx
    
    # Epílogo
    movq %rbp, %rsp
    popq %rbp
    ret
