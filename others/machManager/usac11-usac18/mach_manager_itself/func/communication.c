#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <termios.h>
#include <unistd.h>

#include "../include/func.h"
#include "../include/machine.h"

#define BUFFER_SIZE 256

int configure_serial(const char *portname, int baudrate) {
    int fd = open(portname, O_RDWR | O_NOCTTY | O_SYNC);
    if (fd == -1) {
        perror("Error opening serial port");
        return -1;
    }

    struct termios tty;
    if (tcgetattr(fd, &tty) != 0) {
        perror("Error getting terminal attributes");
        close(fd);
        return -1;
    }

    cfsetospeed(&tty, baudrate);
    cfsetispeed(&tty, baudrate);

    // Configurações básicas: 8N1 (8 bits, sem paridade, 1 bit de parada)
    tty.c_cflag = (tty.c_cflag & ~CSIZE) | CS8;
    tty.c_iflag &= ~IGNBRK;
    tty.c_lflag = 0; // Modo raw
    tty.c_oflag = 0;
    tty.c_cc[VMIN] = 1;   // Ler no mínimo 1 byte
    tty.c_cc[VTIME] = 1;  // Timeout de 0.1 segundos

    tty.c_iflag &= ~(IXON | IXOFF | IXANY); // Sem controle de fluxo por software
    tty.c_cflag |= (CLOCAL | CREAD);       // Habilita leitura e sem controle de modem
    tty.c_cflag &= ~(PARENB | PARODD);     // Sem paridade
    tty.c_cflag &= ~CSTOPB;                // 1 bit de parada
    tty.c_cflag &= ~CRTSCTS;               // Sem controle de fluxo por hardware

    if (tcsetattr(fd, TCSANOW, &tty) != 0) {
        perror("Error setting terminal attributes");
        close(fd);
        return -1;
    }

    return fd;
}

void send_command(int fd, const char *command) {
    int bytes_written = write(fd, command, strlen(command));
    if (bytes_written < 0) {
        perror("Error writing to serial port");
        return;
    }

    //printf("Command sent: %s\n", command);
}

void read_response(int fd, char *buffer, size_t buffer_size) {
    memset(buffer, 0, buffer_size);
    int total_bytes_read = 0;

    while (1) {
        int bytes_read = read(fd, buffer + total_bytes_read, buffer_size - total_bytes_read - 1);
        if (bytes_read < 0) {
            perror("Error reading from serial port");
            break;
        }
        if (bytes_read == 0) {
            break; 
        }

        total_bytes_read += bytes_read;

        if (strchr(buffer, '\n') != NULL || total_bytes_read >= buffer_size - 1) {
            break;
        }
    }

    buffer[total_bytes_read] = '\0';

    while (total_bytes_read > 0 && (buffer[total_bytes_read - 1] == ' ' || buffer[total_bytes_read - 1] == '\n')) {
        buffer[total_bytes_read - 1] = '\0';
        total_bytes_read--;
    }
    //printf("Bytes read: %d\n", total_bytes_read); // Log de depuração
}


int send_and_read_from_machine(const char *command, char *response,  Machine *machine) {
    const char *portname = "/dev/ttyACM0";
    int baudrate = B115200;
    int temperature, humidity;

    int fd = configure_serial(portname, baudrate);
    if (fd == -1) {
        fprintf(stderr, "Failed to configure serial port.\n");
        return -1;
    }

    send_command(fd, command);

    read_response(fd, response, BUFFER_SIZE);

	char hum[]="HUM";
	char temp[]="TEMP";
	char celc[20];
	char perc[20];
	
	//printf("\n\n%s;\n\n", response);
	
	if (extract_data(response, temp, celc, &temperature) == 1 &&
        extract_data(response, hum, perc, &humidity) == 1) {
        
        //printf("\n\n\n\nTemperature %d \n\n\n\n", temperature);
        //printf("\n\n\n\nHumidity %d \n\n\n\n", humidity);
        // Enfileirar dados na máquina específica
        enqueueMachineTemperature(machine, (int)temperature);
        enqueueMachineHumidity(machine, (int)humidity);        
    }
	
	// FOR USAC18 N SEI SE É AQUI
    check_for_alerts();

    close(fd);
    return 0;
}
