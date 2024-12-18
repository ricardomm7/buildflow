# Makefile para o projeto de gestão de máquinas

# Compilador
CC = gcc

# Flags de compilação
CFLAGS = -Wall -Wextra -I./include

# Diretórios
SRCDIR = src
OBJDIR = obj
BINDIR = bin

# Lista de arquivos fonte
SOURCES = machine.c operation.c plant_management.c main.c
OBJECTS = $(SOURCES:%.c=$(OBJDIR)/%.o)

# Nome do executável
EXECUTABLE = $(BINDIR)/main

# Regra padrão
all: $(EXECUTABLE)

# Regra para criar o executável
$(EXECUTABLE): $(OBJECTS)
	@mkdir -p $(BINDIR)
	$(CC) $(CFLAGS) -o $@ $^

# Regra para criar arquivos objeto
$(OBJDIR)/%.o: $(SRCDIR)/%.c
	@mkdir -p $(OBJDIR)
	$(CC) $(CFLAGS) -c $< -o $@

# Limpar arquivos compilados
clean:
	rm -rf $(OBJDIR) $(BINDIR)

# Phony targets
.PHONY: all clean