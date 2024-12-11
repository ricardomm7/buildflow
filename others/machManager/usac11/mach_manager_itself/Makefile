# Nome do executável
TARGET = program

# Arquivos fonte
SRC_C = main.c
SRC_ASM = asseFunctions/usac8.s asseFunctions/usac9.s asseFunctions/usac10.s \
          asseFunctions/usac1.s asseFunctions/usac2.s asseFunctions/usac4.s \
          asseFunctions/usac6.s asseFunctions/usac5.s asseFunctions/usac3.s \
          asseFunctions/usac7.s
          
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
