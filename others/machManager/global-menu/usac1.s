.section .text
	.global extract_data

extract_data:
    # Prólogo
    pushq %rbp
    movq %rsp, %rbp
	
	# Verifica se o ponteiro é NULL
	cmpq $0, %rdi       
	je error_return
	cmpq $0, %rsi       
	je error_return
	
	# Verifica se a string apontada é vazia (primeiro byte == 0)
	movb (%rdi), %al    
	cmpb $0, %al
	je error_return
	movb (%rsi), %al    
	cmpb $0, %al
	je error_return
	
	#Verifica primeiro char do token
	movb (%rsi), %al      
	cmpb $'H', %al        
	je compare_token           
	cmpb $'T', %al        
	je compare_token           

	jmp error_return       
	
	
compare_token:
	xorq %rax, %rax
	
    movb (%rdi), %al
    cmpb $0, %al
    je error_return  # String terminou sem encontrar o token

    movb (%rsi), %r9b
    cmpb $0, %r9b
    je check_end_token # Token consumido, verificar '&'

    cmpb %al, %r9b     
    jne next_char

    # Avançar em ambos
    incq %rdi
    incq %rsi
    jmp compare_token

next_char:
    incq %rdi
    jmp compare_token

check_end_token:
    cmpb $'&', (%rdi)
    jne error_return
    addq $1, %rdi

    cmpb $'u', (%rdi)
    jne error_return
    cmpb $'n', 1(%rdi)
    jne error_return
    cmpb $'i', 2(%rdi)
    jne error_return
    cmpb $'t', 3(%rdi)
    jne error_return
    cmpb $':', 4(%rdi)
    jne error_return
    addq $5, %rdi

copy_unit:
    movb (%rdi), %al
    cmpb $'&', %al
    je find_value
    cmpb $0, %al
    je error_return

    movb %al, (%rdx)
    incq %rdi
    incq %rdx
    jmp copy_unit

find_value:
    movb $0, (%rdx) # Terminar string unit
    incq %rdi
    cmpb $'v', (%rdi)
    jne error_return
    cmpb $'a', 1(%rdi)
    jne error_return
    cmpb $'l', 2(%rdi)
    jne error_return
    cmpb $'u', 3(%rdi)
    jne error_return
    cmpb $'e', 4(%rdi)
    jne error_return
    cmpb $':', 5(%rdi)
    jne error_return
    addq $6, %rdi
    xorq %rax,%rax

value_loop:
    movzbq (%rdi), %r8  
    cmpb $0, %r8b         
    je success_return              
    cmpb $'#', %r8b
    je success_return

    sub $'0', %r8b      
    imul $10, %rax       
    addq %r8, %rax      
    inc %rdi 
    
    jmp value_loop

error_return:
	movb $0, (%rdx)       
    movl $0, (%rcx)       
    xorq %rax, %rax       
    jmp end

success_return:
    movl %eax, (%rcx)
    movq $1, %rax

end:
    movq %rbp, %rsp
    popq %rbp
    ret
