.section .text
.global enqueue_value


	# %rdi --> buffer (*int)
    # %rsi --> length (int)
    # %rdx --> tail (*int)
    # %rcx --> head (*int)
    # %r8  --> value (int)
    
enqueue_value:
   xorq %rax, %rax
    
    cmp (%rdx), %rsi
    je restart_buffer
    

    movq (%rdx), %rax
    imulq $4, %rax
    addq %rax, %rdi

    movl %r8d, (%rdi)
    
    # Incrementa o índice
    movq (%rdx), %rax
    addq $1, %rax
    movq %rax, (%rdx)
    jmp end

restart_buffer:
	movq $0, (%rdx)
	jmp enqueue_value

end:

    # Carrega o índice do último elemento com base no comprimento do buffer
    movq %rsi, %rax       
    sub $1, %rax             
    imulq $4, %rax        
    addq %rax, %rdi       

    cmpq $0, (%rdi)       
    jne empty
    movq $1, %rax         
    ret
empty:
	xor %rax,%rax
	ret
    

