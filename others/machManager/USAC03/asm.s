.section .text
    .global get_number

get_number:
    # Prólogo
    push %rbp                   # Salva o valor base da pilha
    mov %rsp, %rbp              # Atualiza o valor base da pilha

	#rdi *char
	#rsi *int
	movq $0, %rdx
	xorq %rax,%rax

	movb (%rdi), %al
	cmp $0,%al
	je  error

skip_spaces:
	cmpb $' ',(%rdi)
	jne add_numbers


	inc %rdi
	jmp skip_spaces

add_numbers:
    movb (%rdi), %al
    cmpb $0, %al
    je end

    cmpb $'0', %al
    jl verify_space
    cmpb $'9', %al
    jg error


    # Converte caractere para número
    subb $'0', %al
    movzbq %al, %rax
    imul $10, %rdx
    add %rax, %rdx

    inc %rdi
    jmp add_numbers

verify_space:
    cmpb $' ', %al
    jne error         # Se não for espaço, é erro
    inc %rdi
    jmp add_numbers

error:
	movl $0, (%rsi)
	xorq %rdx, %rdx
	movq %rdx, %rax
	jmp done

end:

    movq $1, %rax
    movq %rdx, (%rsi)

done:
    # Epílogo
    mov %rbp, %rsp              # Restaura a pilha
    pop %rbp                    # Restaura o valor base da pilha
    ret
