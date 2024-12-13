.section .text
	.global get_n_element

get_n_element:

    # Prólogo
    pushq %rbp
    movq %rsp, %rbp


    cmpl $0, %esi           # length <= 0?
    jle return_zero

    cmpq $0, %rdx           # tail == NULL?
    je return_zero

    cmpq $0, %rcx           # head == NULL?
    je return_zero

    movl (%rdx), %edx
    movl (%rcx), %ecx

    cmpl $0, %edx           # tail < 0
    jl return_zero

    cmpl %esi, %edx         # tail >= length
    jge return_zero

    cmpl $0, %ecx           # head < 0
    jl return_zero

    cmpl %esi, %ecx         # head >= length
    jge return_zero


    cmpl %ecx, %edx         # tail == head
    je return_zero


    movl %ecx, %eax
    subl %edx, %eax

    # Ajuste para se tail > head
    cmpl $0, %eax
    jge done
    addl %esi, %eax

done:
    # Epílogo
    movq %rbp, %rsp
    popq %rbp
    ret

return_zero:
    xorl %eax, %eax
    jmp done

