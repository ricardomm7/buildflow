.globl sort_array
    
sort_array:
    # Prólogo da função - Guarda os registos necessários
	pushq %rbp           # Guarda o pointer base anterior
	movq %rsp, %rbp      # Atualiza o pointer base para a stack atual
	pushq %rbx           # Guarda o registo %rbx
	pushq %r12           # Guarda o registo %r12
	pushq %r13           # Guarda o registo %r13
	pushq %r14           # Guarda o registo %r14
    
    # Parameters mapping:
    # %rdi = array pointer
    # %rsi = length
    # %dl = order (1 for ascending, 0 for descending)
    
    # Verifica se o comprimento do array é menor ou igual a 0
	cmpl $0, %esi         # Compara length (rsi) com 0
	jle invalid_length    # Se length <= 0, salta para invalid_length
    
    # Inicializa o contador do loop externo
	movl %esi, %r12d      # Copia o comprimento (length) para %r12d
	subl $1, %r12d        #  Decrementa %r12d para representar (length - 1)
    
outer_loop:
	cmpl $0, %r12d       # Verifica se o contador externo (%r12d) é maior que 0
	jle sort_success     # Se não, o array já está ordenado
    
	xorl %r13d, %r13d    # Zera %r13d (contador interno = 0)
    
inner_loop:
	cmpl %r12d, %r13d    # Verifica se o contador interno >= contador externo
	jge end_outer_loop   # Se sim, termina o loop interno
    
	movl %r13d, %r14d    # Copia o índice atual para %r14d
	movl (%rdi,%r14,4), %eax    # Carrega vec[j] em %eax
	movl 4(%rdi,%r14,4), %ebx   # Carrega vec[j+1] em %ebx
    
	testb %dl, %dl        # Verifica a ordem (1 para crescente, 0 para decrescente)
	jz descending_compare # Se ordem = 0, salta para comparação decrescente
   
ascending_compare:
	cmpl %ebx, %eax      # Compara vec[j] (%eax) com vec[j+1] (%ebx)
	jle no_swap          # Se vec[j] <= vec[j+1], não há troca
	jmp do_swap          # Caso contrário, realiza a troca
    
descending_compare:
	cmpl %eax, %ebx      # Compara vec[j+1] (%ebx) com vec[j] (%eax)
	jle no_swap          # Se vec[j+1] <= vec[j], não há troca
    
do_swap:
	movl %ebx, (%rdi,%r14,4)    # Armazena vec[j+1] em vec[j]
	movl %eax, 4(%rdi,%r14,4)   # Armazena vec[j] em vec[j+1]
   
no_swap:
	incl %r13d            # Incrementa %r13d
	jmp inner_loop        # Volta para o início do loop interno
    
end_outer_loop:
	decl %r12d           # Decrementa %r12d
	jmp outer_loop       # Volta para o início do loop externo
    
invalid_length:
	xorl %eax, %eax      # Retorna 0 caso o comprimento seja inválido
	jmp end_function     # Salta para o final

sort_success:
	movl $1, %eax        # Retorna 1 (sucesso)
    
end_function:
    # Epílogo da função - Restaura os registos salvos
	popq %r14            # Restaura %r14
	popq %r13            # Restaura %r13
	popq %r12            # Restaura %r12
	popq %rbx            # Restaura %rbx
	popq %rbp            # Restaura o pointer base
	ret                  # Retorna para o caller
