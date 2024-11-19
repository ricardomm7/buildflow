# Nome do compilador C e do assembler
CC = gcc
AS = as

# Flags de compilação e montagem
CFLAGS = -Wall -g           # Para o GCC, com informações de depuração
ASFLAGS = -g                # Para o assembler, com informações de depuração

# Nome do executável final
TARGET = main

# Arquivos-fonte
SRCS_C = main.c
SRCS_ASM = asm.s

# Arquivos objeto gerados
OBJS = main.o asm.o

# Regra padrão: compila o programa
all: $(TARGET)

# Compila o programa vinculando os objetos
$(TARGET): $(OBJS)
	$(CC) $(CFLAGS) -o $(TARGET) $(OBJS)

# Compila o arquivo C em um objeto
main.o: main.c asm.h
	$(CC) $(CFLAGS) -c main.c -o main.o

# Monta o arquivo asm em um objeto
asm.o: asm.s
	$(AS) $(ASFLAGS) asm.s -o asm.o

# Limpa os arquivos compilados
clean:
	rm -f $(OBJS) $(TARGET)

# Executa o programa em modo de depuração (GDB)
debug: $(TARGET)
	gdb ./$(TARGET)

# Compila e executa o programa
run: $(TARGET)
	./$(TARGET)
