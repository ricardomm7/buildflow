main: main.o asm.o
		gcc main.o asm.o -znoexecstack -o main

main.o: main.c asm.h
		gcc -g -Wall -Wextra -fanalyzer -c main.c -o main.o
		
asm.o: get_number_binary.s
		gcc -g -Wall -Wextra -fanalyzer -c get_number_binary.s -o asm.o
		
run: main 
		./main

clean:
		rm *.o main
