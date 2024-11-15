# Executable name
TARGET = program

# Source files
SRC_C = main.c
SRC_ASM = asm.s

# Rule to compile and link C and assembly code
$(TARGET): main.o asm.o
	gcc main.o asm.o -o $(TARGET)

main.o: $(SRC_C) asm.h
	gcc -g -Wall -c $(SRC_C) -o main.o

asm.o: $(SRC_ASM)
	as -o asm.o $(SRC_ASM)

# Rule to execute the program
run: $(TARGET)
	./$(TARGET)

# Clean up generated files
clean:
	rm -f *.o $(TARGET)
