.section .text
.global get_n_element


	# %rdi --> buffer (*int)
    # %rsi --> length (int)
    # %rdx --> tail (*int)
    # %rcx --> head (*int)

    #atenção este codigo só deve ser chamado se o buffer estiver com o deque e enqueu e o sort
    #bem feitos isto pk se tiver um zero entre tres elemntos ele não irá ler os proximos
    #pois o zero é a flag que sinaliza o termino do vetor
get_n_element:
	xorq %rax,%rax
loop:
	cmpb $0, (%rdi)
	je end
	add $1, %rax
	add $4, %rdi
	jmp loop
	
end: 
	ret

