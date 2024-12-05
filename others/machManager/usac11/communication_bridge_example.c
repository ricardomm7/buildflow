#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <termios.h>
#include <unistd.h>

int main() {
    const char *portname = "/dev/ttyS0"; // Substitua por sua porta serial (Linux: /dev/ttySx ou /dev/ttyUSBx, Windows: COMx)
    int baudrate = B115200;             // Define o baud rate
    int fd = open(portname, O_RDWR | O_NOCTTY | O_SYNC); // Abre a porta serial

    if (fd == -1) {
        perror("Error opening serial port");
        return EXIT_FAILURE;
    }

    // Configuração da porta serial
    struct termios tty;
    if (tcgetattr(fd, &tty) != 0) {
        perror("Error getting terminal attributes");
        close(fd);
        return EXIT_FAILURE;
    }

    cfsetospeed(&tty, baudrate);
    cfsetispeed(&tty, baudrate);

    tty.c_cflag = (tty.c_cflag & ~CSIZE) | CS8; // 8 bits por caractere
    tty.c_iflag &= ~IGNBRK;                    // Desabilita ignorar quebra
    tty.c_lflag = 0;                           // Modo não canônico
    tty.c_oflag = 0;                           // Sem processamento de saída
    tty.c_cc[VMIN]  = 1;                       // Lê no mínimo 1 caractere
    tty.c_cc[VTIME] = 1;                       // Tempo de espera por caractere (0.1s)

    tty.c_iflag &= ~(IXON | IXOFF | IXANY);    // Desabilita controle de fluxo por software
    tty.c_cflag |= (CLOCAL | CREAD);           // Habilita o receptor
    tty.c_cflag &= ~(PARENB | PARODD);         // Desabilita paridade
    tty.c_cflag &= ~CSTOPB;                    // 1 bit de parada
    tty.c_cflag &= ~CRTSCTS;                   // Sem controle de fluxo por hardware

    if (tcsetattr(fd, TCSANOW, &tty) != 0) {
        perror("Error setting terminal attributes");
        close(fd);
        return EXIT_FAILURE;
    }

    // Enviar dados
    const char *command = "ON,1,1,0,1,0\n"; // Comando a ser enviado
    int bytes_written = write(fd, command, strlen(command));
    if (bytes_written < 0) {
        perror("Error writing to serial port");
        close(fd);
        return EXIT_FAILURE;
    }

    printf("Command sent: %s\n", command);

    // Fechar a porta serial
    close(fd);
    return EXIT_SUCCESS;
}
