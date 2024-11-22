# Nome do executável
TARGET = program

# Arquivos fonte
SRC_C = main.c
SRC_ASM = func.s ../usac09/asm.s

# Regra para compilar o código C
$(TARGET): main.o func.o
	gcc main.o func.o -o $(TARGET)

main.o: $(SRC_C) func.h
	gcc -g -Wall -c $(SRC_C) -o main.o

func.o: $(SRC_ASM)
	as -o func.o $(SRC_ASM)

# Regra para executar o programa
run: $(TARGET)
	./$(TARGET)

# Limpeza dos arquivos gerados
clean:
	rm -f *.o $(TARGET)
