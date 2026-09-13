#include <unistd.h> 
#include <stdio.h> 
#include <sys/socket.h>
#include <arpa/inet.h>
#include <stdlib.h> 
#include <string.h> 
#include <sys/wait.h>
#include <sys/types.h>

#define SERVER_PORT 8082
#define BUFFER_LEN 1024 
#define STD_IN 0
#define STD_OUT 1
#define SHELL_PATH "utils/my_terminal"


//inject my_terminal shell through fork+exec
void initiate_client_shell(int connection_fd) {
    printf("server: initiating shell for the client\n");
    int pid = fork(); 

    if(pid<0) {
        printf("server: something went wrong, exiting...\n");
        exit(0);  
    }
    //child 
    if(pid == 0) {
        dup2(connection_fd, STD_IN); 
        dup2(connection_fd, STD_OUT);
        char *argv[] = {"/usr/bin/stdbuf", "-oL", "-eL", SHELL_PATH, NULL};
  
        int status = execvp(argv[0], argv); 
        if(status < 0) {
            printf("something went wrong!...\n"); 
            exit(0); 
        }
    }

    else {
        printf("server: waiting for client's shell interactions\n"); 
        int stat; 
        wait(&stat);
    }
}


void serve_client(int connection_fd) {
    char buffer[BUFFER_LEN];
    
    initiate_client_shell(connection_fd);
    
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