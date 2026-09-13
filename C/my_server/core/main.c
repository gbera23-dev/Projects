#include <unistd.h> 
#include <stdio.h> 
#include <sys/socket.h>
#include <arpa/inet.h>
#include <stdlib.h> 
#include <string.h> 
#define SERVER_PORT 8086
#define BUFFER_LEN 1024 

//inject my_terminal shell through fork+exec

void disconnect_client(int connection_fd) {
    printf("server: gracefully disconnecting client\n"); 
    //send the goodbye message
    char* msg = "goodbye!\n";
            
    if(write(connection_fd, msg, strlen(msg))<0){
        printf("server: failed to write message\n"); 
    }
}

void serve_client(int connection_fd) {
    char buffer[BUFFER_LEN];
    while(1) {
        int num_read = read(connection_fd, buffer, BUFFER_LEN);
        if(num_read<=0) {
            printf("server: failed to read the data\n"); 
            break; 
        }
        buffer[num_read-1]=0;

        printf("server: NUM_READ: %d, %s\n", num_read, buffer); 

        if(strcmp(buffer, "QUIT")==0) {
            disconnect_client(connection_fd);
            break; 
        }

        buffer[num_read-1]='\n'; buffer[num_read]=0;
        int num_written = 
        write(connection_fd, buffer, num_read);

        if(num_written<0) {
            printf("server: failed to write data\n");
            break;
        }
    }
    printf("server: closing client connection\n"); 
    close(connection_fd); 
}

void run_server_loop(int sock_fd, 
    struct sockaddr_in* socket_addr) {

    while(1) {
        printf("server: waiting for connection...\n"); 
        socklen_t sock_len = sizeof(*socket_addr); 
        int new_connection = accept(sock_fd, (struct sockaddr*)socket_addr,
            &sock_len);   

        if(new_connection<0) {
            printf("server: failed to accept connection\n"); 
            continue; 
        }

        printf("server: connection accepted!\n");          
        serve_client(new_connection);
        printf("server: connection finished!\n"); 
    }
}

int main(int argc, char* argv[]) {
  
    int sock_fd = socket(AF_INET, SOCK_STREAM,
    0);

    int opt = 1; 
    if(setsockopt(sock_fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt)) < 0) {
        printf("server: setting socket option failed\n");
        exit(0); 
    }

    if(sock_fd<0){
        printf("server: failed socket creation\n"); 
        exit(0); 
    }

    struct sockaddr_in socket_addr; 

    socket_addr.sin_family = AF_INET; 
    socket_addr.sin_port = htons(SERVER_PORT);
    socket_addr.sin_addr.s_addr = htonl(INADDR_ANY); 

    if(bind(sock_fd, (struct sockaddr*)&socket_addr, sizeof(socket_addr))<0) {
        printf("server: failed binding socket\n"); 
        exit(0); 
    }

    if(listen(sock_fd, 1)<0) {
        printf("server: failed socket listening\n");
        exit(0); 
    } 

    run_server_loop(sock_fd, &socket_addr); 
    
    close(sock_fd); 
    return 0; 
}