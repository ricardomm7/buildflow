.global median

# Parametros esperados:
# vec - %rdi
# length - %esi
# me   - %rdx

median:	
	cmp $0, %esi
	jle end_bad
	
	push %rdi
	push %rsi
	push %rdx
	#call sort_array - when all is settttttttt usac9
	pop %rdx
	pop %rsi
	pop %rdi

	testl $1, %esi  # Faz um AND bitwise entre %esi e 1
	jnz is_odd  # Se o bit menos significativo for 1, salta para is_odd (ímpar)
	
is_even:
	movl %esi, %ecx
    shr $1, %ecx  # Divide o tamanho por 2 (índice do 2º elemento central)
    leaq (%rdi, %rcx, 4), %rax   # Calcula o endereço do 2º elemento central
    movl (%rax), %eax

    subl $1, %ecx   # Calcula o índice do 1º elemento central
    leaq (%rdi, %rcx, 4), %r9 # Calcula o endereço do 1º elemento central
    movl (%r9), %r9d  # Carrega o valor do 1º elemento central em %r9d

    addl %r9d, %eax  
    shrl $1, %eax # Divide por 2 (média dos dois elementos)

    movl %eax, (%rdx)  
    movl $1, %eax 
    jmp end
    
is_odd:
    movl %esi, %ecx
	shr $1, %esi  # Desloca os bits de %esi uma posição para a direita (equivalente a dividir por 2)
	leaq (%rdi, %rsi, 4), %rax  # Calcula o endereço do elemento central
	movl (%rax), %eax

    movl %eax, (%rdx) 
    movl $1, %eax   

end:
	ret
	
end_bad:
    movl $0, %eax 
	ret
