#ifndef FUNC_H
#define FUNC_H
#include "../include/machine.h"

int send_and_read_from_machine(const char *command, char *response, Machine *machine);
void check_for_alerts();
void executeMachineOP(Machine a);

// assembly functions
int move_n_to_array(int* buffer, int length, int *tail, int *head, int n, int* array);
int sort_array(int* vec, int length, char order);
int median(int* vec, int length, int *me);
int extract_data(char* str, char* token, char*unit, int* value);
int get_number_binary(int n, char *bits);
int format_command(char *op, int n, char *cmd);
int enqueue_value(int* buffer, int length, int* tail, int* head, int value);
int dequeue_value(int* buffer, int length, int* tail, int* head, int* value);
int get_number(char* str, int* n);
int get_n_element(int* buffer, int length, int*tail, int* head); 
#endif
