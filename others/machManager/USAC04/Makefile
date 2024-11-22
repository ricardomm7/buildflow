main: main.o asm.o get_number_binary.o
	gcc -g -Wall -Wextra main.o asm.o get_number_binary.o -znoexecstack -o main

main.o: main.c asm.h
	gcc -g -Wall -Wextra -fanalyzer -c main.c -o main.o

asm.o: format_command.s
	gcc -g -Wall -Wextra -fanalyzer -c format_command.s -o asm.o

get_number_binary.o: get_number_binary.s
	gcc -g -Wall -Wextra -fanalyzer -c get_number_binary.s -o get_number_binary.o

run: main
	./main

clean:
	rm -f *.o main
