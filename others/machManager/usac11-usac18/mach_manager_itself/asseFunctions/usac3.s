.section .text
	.global get_number
get_number:
    # Prologue
    push %rbp
    mov %rsp, %rbp


    xorq %rdx, %rdx
    xorq %rax, %rax


    cmpb $0, (%rdi)
    je error

skip_spaces:
    movb (%rdi), %al
    cmpb $0, %al
    je error
    cmpb $' ', %al
    jne process_number
    inc %rdi
    jmp skip_spaces


process_number:
    movb (%rdi), %al
    cmpb $0, %al
    je success
    cmpb $' ', %al
    je check_trailing

    # Verify it's a digit
    cmpb $'0', %al
    jl error
    cmpb $'9', %al
    jg error

    # Convert char to number
    subb $'0', %al
    movzbq %al, %rax

    # Multiply current total by 10
    imulq $10, %rdx
    addq %rax, %rdx

    inc %rdi
    jmp process_number

check_trailing:
    inc %rdi
    movb (%rdi), %al
    cmpb $0, %al
    je success
    cmpb $' ', %al
    je check_trailing
    jmp error

error:
    movl $0, (%rsi)
    xorq %rax, %rax
    jmp done

success:
    movl %edx, (%rsi)
    movq $1, %rax

done:
    mov %rbp, %rsp
    pop %rbp
    ret
