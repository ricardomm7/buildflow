main: main.o asm.o enqueue_value.o
	gcc -g -Wall -Wextra main.o asm.o enqueue_value.o -znoexecstack -o main

main.o: main.c asm.h
	gcc -g -Wall -Wextra -fanalyzer -c main.c -o main.o

asm.o: dequeue_value.s
	gcc -g -Wall -Wextra -fanalyzer -c dequeue_value.s -o asm.o

enqueue_value.o: enqueue_value.s
	gcc -g -Wall -Wextra -fanalyzer -c enqueue_value.s -o enqueue_value.o

run: main
	./main

clean:
	rm -f *.o main
