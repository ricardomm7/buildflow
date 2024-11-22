.global enqueue_value

enqueue_value:
    pushq %rbp
	movq %rsp, %rbp

    # Parameters mapping:
    # %rdi = buffer pointer
    # %rsi = length  (buffer size)
    # %rdx = read pointer (head)
    # %rcx = write pointer (tail)
    # %r8d = value to insert

    movl (%rdx), %r9d    # r9d = read position
	movl (%rcx), %r10d   # r10d = write position

	movl %r8d, (%rdi,%r10,4)  # buffer[write] = value

	movl %r10d, %r11d    # r11d = current write
	addl $1, %r11d       # r11d = write + 1
	cmpl %esi, %r11d     # Compare with length
	jl write_no_wrap     # If less than length, skip next instruction
	xorl %r11d, %r11d    # If equal/greater, reset to 0

write_no_wrap:
	movl %r11d, (%rcx)   # Store new write position

	cmpl %r11d, %r9d     # Compare read with new write
	jne buffer_not_full  # If not equal, buffer not full

	movl %r11d, %r9d     # New read position = new write position
	addl $1, %r9d        # Increment read position
	cmpl %esi, %r9d      # Compare with length
	jl read_no_wrap                 # If less than length, skip next instruction
	xorl %r9d, %r9d      # If equal/greater, reset to 0

read_no_wrap :
	movl %r9d, (%rdx)    # Update read pointer
	movl $1, %eax        # Return 1 (buffer full)
	jmp end_function     # Jump to end

buffer_not_full:
	movl $0, %eax        # Return 0

end_function:
	popq %rbp
	ret