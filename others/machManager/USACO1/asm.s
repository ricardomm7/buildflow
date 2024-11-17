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
    # %rcx = value --> ponteiro int

find_token:
    movb (%rdi), %al            
    cmpb $0, %al                
    je end_error                
    cmpb %al, (%rsi)            
    je verify_token             
    inc %rdi                    
    jmp find_token

verify_token:
    movb (%rsi), %al            
    cmpb $0, %al                
    je find_index_unit          
    movb (%rdi), %al            
    cmpb %al, (%rsi)            
    jne find_token              
    inc %rdi                    
    inc %rsi                    
    jmp verify_token

find_index_unit:
    movb (%rdi), %al            
    cmpb $':', %al              
    jne skip_unit
    inc %rdi                    
    jmp save_unit

skip_unit:
    inc %rdi                    
    jmp find_index_unit         

save_unit:
    movb (%rdi), %al            
    cmpb $'&', %al              
    je find_index_value         
    movb %al, (%rdx)            
    inc %rdi                    
    inc %rdx                    
    jmp save_unit

find_index_value:
          
    addq $7, %rdi               
    xorq %rax, %rax             

save_value:
    movzbq (%rdi), %r8          
    cmpb $0, %r8b               # Verifica se chegou ao final da string
    je end_success              # Se sim, termina com sucesso
    cmpb $'#', %r8b             
    je end_success

    sub $'0', %r8b              # Converte ASCII para número
    imul $10, %rax              # Multiplica o acumulador por 10
    addq %r8, %rax              # Adiciona o número atual

    inc %rdi                    
    jmp save_value              

end_error:
    movb $0, (%rdx)             # Limpa a unidade
    movq $0, (%rcx)             # Zera o valor numérico
    xor %eax, %eax              # Retorna 0 (falha)
    jmp end

end_success:
    movq %rax, (%rcx)           # Salva o valor final em *value
    mov $1, %eax                # Retorna 1 (sucesso)

end:
    # Epílogo
    mov %rbp, %rsp
    pop %rbp
    ret
