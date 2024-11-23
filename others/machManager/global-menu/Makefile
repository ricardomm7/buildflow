# Nome do executável
TARGET = program

# Arquivos fonte
SRC_C = main.c
SRC_ASM = usac8.s usac9.s usac10.s usac1.s usac2.s usac4.s usac6.s usac5.s usac3.s usac7.s

# Objetos gerados
OBJ_C = $(SRC_C:.c=.o)
OBJ_ASM = $(SRC_ASM:.s=.o)

# Regra para compilar o código C e ligar o executável
$(TARGET): $(OBJ_C) $(OBJ_ASM)
	gcc $(OBJ_C) $(OBJ_ASM) -o $(TARGET)

# Compilação do código C
%.o: %.c
	gcc -g -Wall -c $< -o $@

# Compilação dos arquivos Assembly
%.o: %.s
	as -o $@ $<

# Regra para executar o programa
run: $(TARGET)
	./$(TARGET)

# Limpeza dos arquivos gerados
clean:
	rm -f *.o $(TARGET)
