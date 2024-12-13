.section .text
.global format_command
format_command:
    # Prólogo - Guardar apenas registos necessários
    push %r8             # Guarda o registo r8 na stack
    push %r9             # Guarda o registo r9 na stack
    push %rdi            # Guarda o registo rdi na stack
    push %rsi            # Guarda o registo rsi na stack
    push %rdx            # Guarda o registo rdx na stack

    cmpq $0, %rdi         # Verifica se string str é nula
    je invalid_input
    cmpq $0, %rdx         # Verifica se cmd é nulo
    je invalid_input

    movq %rdi, %r8        # r8 = endereço da string str
    movq %rdx, %r9        # r9 = endereço do cmd
    movl %esi, %esi       # guarda n em esi

    xorl %ecx, %ecx       # ecx = 0 (contador de caracteres)

copy_no_spaces:
    movb (%r8), %al       # Carrega um caractere da string
    cmpb $0, %al          # Verifica se é o fim da string
    je end_copy           # Se for, termina a cópia

    cmpb $32, %al         # Verifica se é um espaço
    je skip_space         # Se for, ignora o espaço

    cmpb $'a', %al        # Verifica se é letra minúscula
    jl store_char         # Se não for, guarda como está
    cmpb $'z', %al        # Verifica se está no intervalo a-z
    jg store_char         # Se não for, guarda como está
    subb $32, %al         # Converte para maiúscula

store_char:
    movb %al, (%r9, %rcx) # Guarda caractere temporariamente no início de cmd
    incl %ecx             # Incrementa o contador

skip_space:
    incq %r8              # Avança para o próximo caractere
    jmp copy_no_spaces    # Continua o loop

end_copy:
    movb $0, (%r9, %rcx)  # Termina a string com null

    cmpl $2, %ecx         # Verifica se tem 2 caracteres
    je check_two_chars    # Se sim, verifica ON/OP
    cmpl $3, %ecx         # Verifica se tem 3 caracteres
    je check_commands     # Se sim, verifica OFF
    jmp invalid_input     # Se não, é inválido

check_two_chars:
    # Verifica se é "ON"
    cmpb $'O', (%r9)      # Verifica primeiro caractere
    jne check_if_op       # Se não for 'O', verifica se é OP
    cmpb $'N', 1(%r9)     # Verifica segundo caractere
    je on_command         # Se for 'N', é comando ON

check_if_op:
    # Verifica se é "OP"
    cmpb $'O', (%r9)      # Verifica primeiro caractere
    jne invalid_input     # Se não for 'O', é inválido
    cmpb $'P', 1(%r9)     # Verifica segundo caractere
    je op_command         # Se for 'P', é comando OP
    jmp invalid_input     # Se não, é inválido

check_commands:
    # Verifica se é "OFF"
    cmpb $'O', (%r9)      # Verifica primeiro caractere
    jne invalid_input     # Se não for 'O', é invalido
    cmpb $'F', 1(%r9)     # Verifica segundo caractere
    jne invalid_input     # Se não for 'F', é inválido
    cmpb $'F', 2(%r9)     # Verifica terceiro caractere
    je off_command        # Se for 'F', é comando OFF
    jmp invalid_input     # Se não, é inválido

on_command:
    movq %r9, %rdx        # rdx aponta para cmd
    movb $'O', (%rdx)     # Escreve 'O'
    movb $'N', 1(%rdx)    # Escreve 'N'
    movb $',', 2(%rdx)    # Escreve ','
    addq $3, %rdx         # Avança 3 posições
    jmp convert_binary    # Converte número para binário

op_command:
    movq %r9, %rdx        # rdx aponta para cmd
    movb $'O', (%rdx)     # Escreve 'O'
    movb $'P', 1(%rdx)    # Escreve 'P'
    movb $',', 2(%rdx)    # Escreve ','
    addq $3, %rdx         # Avança 3 posições
    jmp convert_binary    # Converte número para binário

off_command:
    movq %r9, %rdx        # rdx aponta para cmd
    movb $'O', (%rdx)     # Escreve 'O'
    movb $'F', 1(%rdx)    # Escreve 'F'
    movb $'F', 2(%rdx)    # Escreve 'F'
    movb $',', 3(%rdx)    # Escreve ','
    addq $4, %rdx         # Avança 4 posições

convert_binary:
    movq %rdx, %r8        # Guarda posição atual
    movl %esi, %edi       # Move n para primeiro argumento
    leaq 16(%r9), %rsi    # Usa espaço após o comando para bits

    push %r8
    push %r9
    push %rdi
    push %rdx
    push %rcx
    push %rax

    call get_number_binary # Chama função de conversão para binário

    cmpl $0, %eax         # Verifica se houve erro
    je invalid_input      # Se sim, retorna erro

    pop %rax
    pop %rcx
    pop %rdx
    pop %rdi
    pop %r9
    pop %r8

    movq %r8, %rdx        # Restaura posição no cmd
    leaq 20(%r9), %rsi    # rsi aponta para último bit
    movl $5, %ecx         # Contador = 5 bits

convert_loop:
    movb (%rsi), %al      # Carrega bit
    addb $'0', %al        # Converte para caractere
    movb %al, (%rdx)      # Guarda caractere
    incq %rdx             # Avança no cmd
    decq %rsi             # Recua no array de bits
    decl %ecx             # Decrementa contador
    je finish_string      # Se acabou, termina string
    movb $',', (%rdx)     # Se não, adiciona vírgula
    incq %rdx             # Avança no cmd
    jmp convert_loop      # Continua loop

finish_string:
    movb $0, (%rdx)       # Termina string com null
    movl $1, %eax         # Retorna sucesso
    jmp end               # Vai para final

invalid_input:
    movl $0, %eax         # Retorna 0
    movq %r9, %rdx        # rdx aponta para cmd
    movb $0, (%rdx)       # Termina string com null

end:
    # Epílogo - Restaura registos
    pop %rdx             # Restaura rdx
    pop %rsi             # Restaura rsi
    pop %rdi             # Restaura rdi
    pop %r9              # Restaura r9
    pop %r8              # Restaura r8
    ret                   # Retorna
