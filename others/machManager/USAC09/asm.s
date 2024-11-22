.globl sort_array
    
sort_array:
    # Function prologue
	pushq %rbp
	movq %rsp, %rbp
	pushq %rbx           # Save callee-saved registers
	pushq %r12
	pushq %r13
	pushq %r14
    
    # Parameters mapping:
    # %rdi = array pointer
    # %rsi = length
    # %dl = order (1 for ascending, 0 for descending)
    
    # Check if length <= 0
	cmpl $0, %esi
	jle invalid_length
    
    # Initialize outer loop counter
	movl %esi, %r12d     # r12d = length
	subl $1, %r12d       # r12d = length - 1
    
outer_loop:
	cmpl $0, %r12d       # Check if outer counter > 0
	jle sort_success     # If not, sorting is done
    
    # Initialize inner loop counter and swap flag
	xorl %r13d, %r13d    # r13d = inner counter = 0
    
inner_loop:
	cmpl %r12d, %r13d    # Compare inner counter with outer
	jge end_outer_loop   # If inner >= outer, end inner loop
    
    # Load adjacent elements
	movl %r13d, %r14d
	movl (%rdi,%r14,4), %eax    # eax = vec[j]
	movl 4(%rdi,%r14,4), %ebx   # ebx = vec[j+1]
    
    # Compare based on order
	testb %dl, %dl
	jz descending_compare
   
ascending_compare:
	cmpl %ebx, %eax      # Compare for ascending
	jle no_swap
	jmp do_swap
    
descending_compare:
	cmpl %eax, %ebx      # Compare for descending
	jle no_swap
    
do_swap:
	# Swap elements
	movl %ebx, (%rdi,%r14,4)    # vec[j] = vec[j+1]
	movl %eax, 4(%rdi,%r14,4)   # vec[j+1] = vec[j]
   
no_swap:
	incl %r13d           # Increment inner counter
	jmp inner_loop
    
end_outer_loop:
	decl %r12d           # Decrement outer counter
	jmp outer_loop
    
invalid_length:
	xorl %eax, %eax      # Return 0 for invalid length
	jmp end_function

sort_success:
	movl $1, %eax        # Return 1 for success
    
end_function:
	# Function epilogue
	popq %r14            # Restore callee-saved registers
	popq %r13
	popq %r12
	popq %rbx
	popq %rbp
	ret
