# Nome do executável
TARGET = program
OBJDIR = obj
BINDIR = bin

# Diretórios de include
INCLUDEDIR = ../include

# Flags de compilação
CFLAGS = -g -Wall -Wextra -I$(INCLUDEDIR)

# Criação do diretório obj e bin, se não existir
$(shell mkdir -p $(OBJDIR)/func $(OBJDIR)/asseFunctions $(BINDIR))

# Arquivos fonte
SRC_C = func/communication.c func/alertAnalysis.c func/main.c \
        func/machine.c func/operation.c func/plant_management.c
SRC_ASM = asseFunctions/usac8.s asseFunctions/usac9.s asseFunctions/usac10.s \
          asseFunctions/usac1.s asseFunctions/usac2.s asseFunctions/usac4.s \
          asseFunctions/usac6.s asseFunctions/usac5.s asseFunctions/usac3.s \
          asseFunctions/usac7.s

# Objetos gerados (diretório obj)
OBJ_C = $(SRC_C:%.c=$(OBJDIR)/%.o)
OBJ_ASM = $(SRC_ASM:%.s=$(OBJDIR)/%.o)

# Regras
all: $(BINDIR)/$(TARGET)

# Regra para compilar o código C e ligar o executável
$(BINDIR)/$(TARGET): $(OBJ_C) $(OBJ_ASM)
	gcc $(CFLAGS) $(OBJ_C) $(OBJ_ASM) -o $@

# Compilação do código C
$(OBJDIR)/%.o: %.c
	gcc $(CFLAGS) -c $< -o $@

# Compilação dos arquivos Assembly
$(OBJDIR)/%.o: %.s
	as -o $@ $

# Regra para executar o programa
run: $(BINDIR)/$(TARGET)
	./$(BINDIR)/$(TARGET)

# Limpeza dos arquivos gerados
clean:
	rm -rf $(OBJDIR) $(BINDIR)

# Phony targets
.PHONY: all clean run
