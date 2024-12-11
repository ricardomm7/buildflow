#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <termios.h>
#include <unistd.h>

#include "func.h"

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

    tty.c_cflag = (tty.c_cflag & ~CSIZE) | CS8;
    tty.c_iflag &= ~IGNBRK;
    tty.c_lflag = 0;
    tty.c_oflag = 0;
    tty.c_cc[VMIN] = 1;   // Minimum number of characters to read
    tty.c_cc[VTIME] = 10; // Timeout in deciseconds (1 second)

    tty.c_iflag &= ~(IXON | IXOFF | IXANY);
    tty.c_cflag |= (CLOCAL | CREAD);
    tty.c_cflag &= ~(PARENB | PARODD);
    tty.c_cflag &= ~CSTOPB;
    tty.c_cflag &= ~CRTSCTS;

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

    printf("Command sent: %s\n", command);
}

void read_response(int fd, char *buffer, size_t buffer_size) {
    int total_bytes_read = 0;

    memset(buffer, 0, buffer_size);

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
}

int main() {
    const char *portname = "/dev/ttyS0";
    const char *command = "ON,1,1,0,1,0\n";
    int baudrate = B115200;
    char response[BUFFER_SIZE];

    int fd = configure_serial(portname, baudrate);
    if (fd == -1) {
        return EXIT_FAILURE;
    }

    send_command(fd, command);

    printf("Waiting for response...\n");
    read_response(fd, response, BUFFER_SIZE);
    printf("Response received: %s\n", response);

    close(fd);
    return EXIT_SUCCESS;
}
