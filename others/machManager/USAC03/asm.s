.section .text  
.global get_number

get_number:
    # Prólogo
    push %rbp                   # Salva o valor base da pilha
    mov %rsp, %rbp              # Atualiza o valor base da pilha
    
	#rdi *char
	#rsi *int
	movq $0, %rdx
	movq $0, %rcx
skip:
	cmpb $' ',(%rdi)
	jne add_numbers
	
	inc %rdi
	jmp skip
	
add_numbers:
    movb (%rdi), %al            
    cmpb $0, %al                
    je end                      
    
    cmpb $'0', %al              
    jl verify_digit     
    cmpb $'9', %al              
    jg end    
	
	add $1, %rcx 
    # Converte caractere para número
    subb $'0', %al 
    movzbq %al, %rax             
    imul $10, %rdx             
    add %rax, %rdx             
                      

    inc %rdi                    
    jmp add_numbers             

verify_digit:

	cmpb $0, (%rdi)
    je end 
    
    cmpb $' ', (%rdi)              
    jne bad_character              
    
          
    inc %rdi
    jmp verify_digit
bad_character:
	xor %rcx,%rcx

end:
    cmpq $0, %rcx               # Verifica se encontrou algum dígito
    jg end_sucess              # Se não encontrou, falha

    movq $0, (%rsi)           # Salva o número (32 bits) em *n
    movq $0, %rax               # Retorna 1 (sucesso)
    jmp done

end_sucess:
    mov $1, %rax             # Retorna 0 (falha)
    movq %rdx, (%rsi)             # Zera o valor de *n (32 bits)

done:
    # Epílogo
    mov %rbp, %rsp              # Restaura a pilha
    pop %rbp                    # Restaura o valor base da pilha
    ret
