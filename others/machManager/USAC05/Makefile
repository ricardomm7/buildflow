TARGET = program

SRC_C = main.c
SRC_ASM = asm.s

$(TARGET): main.o asm.o
	gcc main.o asm.o -o $(TARGET)

main.o: $(SRC_C) asm.h
	gcc -g -Wall -c $(SRC_C) -o main.o

func.o: $(SRC_ASM)
	as -o asm.o $(SRC_ASM)

run: $(TARGET)
	./$(TARGET)

clean:
	rm -f *.o $(TARGET)