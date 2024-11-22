# Nome do executável
TARGET = program

# Arquivos fonte
SRC_C = main.c
SRC_ASM = asm.s

# Regra para compilar o código C
$(TARGET): main.o asm.o
	gcc main.o asm.o -o $(TARGET)

main.o: $(SRC_C) asm.h
	gcc -g -Wall -c $(SRC_C) -o main.o

func.o: $(SRC_ASM)
	as -o asm.o $(SRC_ASM)

# Regra para executar o programa
run: $(TARGET)
	./$(TARGET)

# Limpeza dos arquivos gerados
clean:
	rm -f *.o $(TARGET)
