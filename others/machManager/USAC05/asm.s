.section .text
.global enqueue_value

enqueue_value:
   
	push %rbx           
	push %rbp            
	push %r12            
	push %r13           
	push %r14            

   
	mov 0x0(%rdx), %r12  
	mov 0x0(%rcx), %r13
	mov %r8, %r14 

	lea (%rdi, %r12, 4), %rbx 
	mov %r14, (%rbx)

	add $1, %r12               
	cmp %rsi, %r12             
	je reset_head
	jmp continue_head
	               
reset_head:
	mov $0, %r12
	
continue_head:
	mov %r12, 0x0(%rdx)

	cmp %r12, %r13            
	je full_buffer               

	mov $0, %rax               
	jmp end_function

full_buffer:
	add $1, %r13             
	cmp %rsi, %r13           
	je reset_tail            
	jmp continue_tail

reset_tail:
	mov $0, %r13             

continue_tail:
	mov %r13, 0x0(%rcx)      

	mov $1, %rax            

end_function:
	pop %r14                 
	pop %r13
	pop %r12
	pop %rbp
	pop %rbx
	ret
