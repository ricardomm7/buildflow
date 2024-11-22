.section .text
.global move_n_to_array

# Parameters:
# %rdi -> buffer (int*)
# %esi -> length (int)
# %rdx -> tail pointer (int*)
# %rcx -> head pointer (int*)
# %r8d -> n (int)
# %r9  -> array (int*)

move_n_to_array:
	# Prólogo
    pushq %rbp
    movq %rsp, %rbp
    pushq %rbx
    pushq %r12
    pushq %r13
    pushq %r14
    pushq %r15

    movl (%rcx), %r10d         # r10d = *head
    movl (%rdx), %r11d         # r11d = *tail
    
    movl %r10d, %eax          # eax = head
    subl %r11d, %eax          # eax = head - tail
    addl %esi, %eax           # Add length to handle negative result
    
    cmpl %esi, %eax			  # Compares with the buffer size
    jl skip_mod				  # If eax<esi skips
    subl %esi, %eax           # Subtract length if result >= length
    
skip_mod:
    cmpl %r8d, %eax			  # Compares the available elements with n
    jl fail
    
    xorl %eax, %eax           # i = 0
    movl (%rdx), %r11d        # Current tail position
    
copy_loop:
    cmpl %r8d, %eax           # Compare i with n
    jge copy_done
    
    movl (%rdi,%r11,4), %r10d # Load buffer[tail]
    movl %r10d, (%r9,%rax,4)  # Store to array[i]
    
    incl %r11d
    cmpl %esi, %r11d          # Compare tail with length
    jl no_wrap
    xorl %r11d, %r11d         # Reset tail to 0 if at length

no_wrap:
    incl %eax                 # Increment counter
    jmp copy_loop

copy_done:
    movl %r11d, (%rdx)
    movl $1, %eax             # Return success
    jmp end

fail:
    movl $0, %eax             # Return failure

end:
	# Epílogo
	popq %r15
    popq %r14
    popq %r13
    popq %r12
    popq %rbx
    movq %rbp, %rsp
    popq %rbp
    ret
