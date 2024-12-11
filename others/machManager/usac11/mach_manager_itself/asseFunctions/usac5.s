.global enqueue_value

enqueue_value:
    # Prólogo - Configurar o frame da função
    pushq %rbp          # Guarda o pointer base na stack
	movq %rsp, %rbp     # Atualiza o pointer base para o atual stack pointer

    movl (%rdx), %r9d    # Carrega a posição de leitura (read position) em %r9d
	movl (%rcx), %r10d   # Carrega a posição de escrita (write position) em %r10d

	movl %r8d, (%rdi,%r10,4)  # Grava o valor (r8d) na posição buffer[write]

	movl %r10d, %r11d    # Copia a posição de escrita para %r11d
	addl $1, %r11d       # Incrementa a posição de escrita
	cmpl %esi, %r11d     # Compara com o comprimento do buffer
	jl write_no_wrap     # Se menor que o comprimento, salta para write_no_wrap
	xorl %r11d, %r11d    # Se igual ou maior, resets %r11d para 0

write_no_wrap:
	movl %r11d, (%rcx)   # Atualiza a posição de escrita (write position)

	cmpl %r11d, %r9d     # Compara a posição de leitura com a nova posição de escrita
	jne buffer_not_full  # Se diferente, o buffer não está cheio

	movl %r11d, %r9d     # Atualiza a posição de leitura para a nova posição de escrita
	addl $1, %r9d        # Incrementa a posição de leitura
	cmpl %esi, %r9d      # Compara com o comprimento do buffer
	jl read_no_wrap      # Se menor, salta para read_no_wrap
	xorl %r9d, %r9d      # Se igual ou maior, resets %r9d para 0

read_no_wrap :
	movl %r9d, (%rdx)    # Atualiza o pointer de leitura (read pointer)
	movl $1, %eax        # Define o valor de return como 1 (buffer cheio)
	jmp end_function     # Salta para o final

buffer_not_full:
	movl $0, %eax        # Define o valor de return como 0 (buffer não cheio)

end_function:
    # Epílogo - Restaurar registos
	popq %rbp            # Restaura o pointer base
	ret                  # Retorna para o caller