#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <termios.h>
#include <unistd.h>

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

    tty.c_cflag = (tty.c_cflag & ~CSIZE) | CS8; // 8 bits por caractere
    tty.c_iflag &= ~(IXON | IXOFF | IXANY);    // Desabilita controle de fluxo por software
    tty.c_lflag &= ~(ICANON | ECHO | ECHOE | ISIG); // Desabilita modo canônico
    tty.c_oflag = 0;                           // Sem processamento de saída
    tty.c_cc[VMIN]  = 1;                       // Lê no mínimo 1 caractere
    tty.c_cc[VTIME] = 10;                      // Timeout de 1 segundo

    tty.c_cflag |= (CLOCAL | CREAD);           // Habilita receptor
    tty.c_cflag &= ~(PARENB | PARODD);         // Desabilita paridade
    tty.c_cflag &= ~CSTOPB;                    // 1 bit de parada
    tty.c_cflag &= ~CRTSCTS;                   // Sem controle de fluxo por hardware

    if (tcsetattr(fd, TCSANOW, &tty) != 0) {
        perror("Error setting terminal attributes");
        close(fd);
        return -1;
    }

    return fd;
}

void send_led_command(int fd, const char *command) {
    int bytes_written = write(fd, command, strlen(command));
    if (bytes_written != strlen(command)) {
        fprintf(stderr, "Error: Could not write full command to serial port.\n");
        return;
    }
    printf("LED command sent: %s\n", command);
}

int main() {
    const char *portname = "/dev/ttyS0";
    int baudrate = B115200;

    int fd = configure_serial(portname, baudrate);
    if (fd == -1) {
        return EXIT_FAILURE;
    }

    // Enviar comando para ligar os LEDs
    send_led_command(fd, "ON,1,1,0,1,0\n");

    // Ler resposta do Pico
    char buffer[128];
    int n = read(fd, buffer, sizeof(buffer) - 1);
    if (n > 0) {
        buffer[n] = '\0';
        printf("Received from Pico: %s\n", buffer);
    } else {
        fprintf(stderr, "No response from Pico.\n");
    }

    close(fd);
    return EXIT_SUCCESS;
}
