.section .text
.global format_command
format_command:
    # Prólogo - Guardar registos que vamos usar
    pushq %rbp            # Guarda o base pointer
    movq %rsp, %rbp       # Estabelece novo frame pointer
    pushq %r8             # Guarda o registo r8 na pilha
    pushq %r9             # Guarda o registo r9 na pilha
    pushq %rdi            # Guarda o registo rdi na pilha
    pushq %rsi            # Guarda o registo rsi na pilha
    pushq %rdx            # Guarda o registo rdx na pilha
    
    # Verificar parâmetros nulos
    cmpq $0, %rdi         # Verifica se string de entrada é nula
    je invalid_input
    cmpq $0, %rdx         # Verifica se buffer de saída é nulo
    je invalid_input
    
    # Guardar parâmetros em registos seguros
    movq %rdi, %r8        # r8 = endereço da string de entrada
    movq %rdx, %r9        # r9 = endereço do buffer de saída
    movl %esi, %r11d      # r11d = valor de n
    
    # Criar buffer temporário na pilha com alinhamento
    subq $32, %rsp        # Reserva 32 bytes na pilha (16 bytes alinhados)
    movq %rsp, %rdx       # rdx aponta para o buffer temporário
    
    # Inicializar contador para string limpa
    xorl %ecx, %ecx       # ecx = 0 (contador de caracteres)
    
copy_no_spaces:
    movb (%r8), %al       # Carrega um caractere da string de entrada
    cmpb $0, %al          # Verifica se é o fim da string
    je end_copy           # Se for, termina a cópia
    cmpb $32, %al         # Verifica se é um espaço
    je skip_space         # Se for, ignora o espaço
    
    # Verificar tamanho máximo do buffer
    cmpl $16, %ecx        # Verifica se excedeu tamanho máximo
    jae cleanup_invalid   # Se sim, é inválido
    
    # Converter para maiúsculas
    cmpb $'a', %al        # Verifica se é letra minúscula
    jl store_char         # Se não for, guarda como está
    cmpb $'z', %al        # Verifica se está no intervalo a-z
    jg store_char         # Se não for, guarda como está
    subb $32, %al         # Converte para maiúscula
    
store_char:
    movb %al, (%rdx)      # Guarda o caractere no buffer temporário
    incq %rdx             # Avança no buffer temporário
    incl %ecx             # Incrementa o contador
    
skip_space:
    incq %r8              # Avança para o próximo caractere
    jmp copy_no_spaces    # Continua o loop
    
end_copy:
    movb $0, (%rdx)       # Termina a string com null
    
    # Compara com comandos válidos
    movq %rsp, %rdi       # rdi aponta para início do buffer temporário
    
    # Verifica comprimento
    cmpl $2, %ecx         # Verifica se tem 2 caracteres
    je check_two_chars    # Se sim, verifica ON/OP
    cmpl $3, %ecx         # Verifica se tem 3 caracteres
    je check_commands     # Se sim, verifica OFF/CMD
    jmp cleanup_invalid   # Se não, é inválido

check_two_chars:
    # Verifica se é "ON"
    cmpb $'O', (%rdi)     # Verifica primeiro caractere
    jne check_if_op       # Se não for 'O', verifica se é OP
    cmpb $'N', 1(%rdi)    # Verifica segundo caractere
    je on_command         # Se for 'N', é comando ON
    
check_if_op:
    cmpb $'O', (%rdi)     # Verifica primeiro caractere
    jne cleanup_invalid   # Se não for 'O', é inválido
    cmpb $'P', 1(%rdi)    # Verifica segundo caractere
    je op_command         # Se for 'P', é comando OP
    jmp cleanup_invalid   # Se não, é inválido

check_commands:
    # Verifica se é "OFF"
    cmpb $'O', (%rdi)     # Verifica primeiro caractere
    jne check_if_cmd      # Se não for 'O', verifica se é CMD
    cmpb $'F', 1(%rdi)    # Verifica segundo caractere
    jne cleanup_invalid   # Se não for 'F', é inválido
    cmpb $'F', 2(%rdi)    # Verifica terceiro caractere
    je off_command        # Se for 'F', é comando OFF
    jmp cleanup_invalid   # Se não, é inválido

check_if_cmd:
    cmpb $'C', (%rdi)     # Verifica se primeiro caractere é 'C'
    jne cleanup_invalid   # Se não for, é inválido
    cmpb $'M', 1(%rdi)    # Verifica se segundo caractere é 'M'
    jne cleanup_invalid   # Se não for, é inválido
    cmpb $'D', 2(%rdi)    # Verifica se terceiro caractere é 'D'
    jne cleanup_invalid   # Se não for, é inválido
    jmp cmd_command       # Se for CMD, processa comando CMD

on_command:
    movq %r9, %rdx        # rdx aponta para buffer de saída
    movb $'O', (%rdx)     # Escreve 'O'
    movb $'N', 1(%rdx)    # Escreve 'N'
    movb $',', 2(%rdx)    # Escreve ','
    addq $3, %rdx         # Avança 3 posições
    jmp convert_binary    # Converte número para binário

op_command:
    movq %r9, %rdx        # rdx aponta para buffer de saída
    movb $'O', (%rdx)     # Escreve 'O'
    movb $'P', 1(%rdx)    # Escreve 'P'
    movb $',', 2(%rdx)    # Escreve ','
    addq $3, %rdx         # Avança 3 posições
    jmp convert_binary    # Converte número para binário

off_command:
    movq %r9, %rdx        # rdx aponta para buffer de saída
    movb $'O', (%rdx)     # Escreve 'O'
    movb $'F', 1(%rdx)    # Escreve 'F'
    movb $'F', 2(%rdx)    # Escreve 'F'
    movb $',', 3(%rdx)    # Escreve ','
    addq $4, %rdx         # Avança 4 posições
    jmp convert_binary    # Converte número para binário

cmd_command:
    movq %r9, %rdx        # rdx aponta para buffer de saída
    movb $'C', (%rdx)     # Escreve 'C'
    movb $'M', 1(%rdx)    # Escreve 'M'
    movb $'D', 2(%rdx)    # Escreve 'D'
    movb $',', 3(%rdx)    # Escreve ','
    addq $4, %rdx         # Avança 4 posições
    jmp convert_binary    # Converte número para binário

convert_binary:
    # Salva posição atual no buffer
    movq %rdx, %r8        # Guarda posição atual
    
    # Prepara array de bits
    subq $16, %rsp        # Reserva espaço para array de bits (alinhado)
    movq %rsp, %rsi       # rsi aponta para array de bits
    movl %r11d, %edi      # Restaura n para conversão
    
    call get_number_binary # Chama função de conversão para binário
    
    # Verifica retorno
    cmpl $0, %eax         # Verifica se houve erro
    je cleanup_all_invalid # Se sim, limpa e retorna erro
    
    # Converte bits para string
    movq %r8, %rdx        # Restaura posição no buffer
    movq %rsp, %rsi       # rsi aponta para array de bits
    addq $4, %rsi         # Aponta para último bit
    movl $5, %ecx         # Contador = 5 bits

convert_loop:
    movb (%rsi), %al      # Carrega bit
    addb $'0', %al        # Converte para caractere
    movb %al, (%rdx)      # Guarda caractere
    incq %rdx             # Avança no buffer
    decq %rsi             # Recua no array de bits
    decl %ecx             # Decrementa contador
    je finish_string      # Se acabou, termina string
    movb $',', (%rdx)     # Se não, adiciona vírgula
    incq %rdx             # Avança no buffer
    jmp convert_loop      # Continua loop

finish_string:
    movb $0, (%rdx)       # Termina string com null
    jmp success           # Finaliza com sucesso

cleanup_all_invalid:
    addq $16, %rsp        # Remove espaço do array de bits
    addq $32, %rsp        # Remove espaço do buffer temporário
    jmp invalid_input     # Vai para tratamento de erro

cleanup_invalid:
    addq $32, %rsp        # Remove espaço do buffer temporário
    jmp invalid_input     # Vai para tratamento de erro

success:
    addq $16, %rsp        # Remove espaço do array de bits
    addq $32, %rsp        # Remove espaço do buffer temporário
    movl $1, %eax         # Retorna 1 (sucesso)
    jmp end              # Vai para final

invalid_input:
    movl $0, %eax         # Retorna 0 (erro)
    movq %r9, %rdx        # rdx aponta para buffer de saída
    movb $0, (%rdx)       # Termina string com null

end:
    # Epílogo - Restaura registos
    popq %rdx             # Restaura rdx
    popq %rsi             # Restaura rsi
    popq %rdi             # Restaura rdi
    popq %r9              # Restaura r9
    popq %r8              # Restaura r8
    movq %rbp, %rsp       # Restaura stack pointer
    popq %rbp             # Restaura base pointer
    ret                   # Retorna
