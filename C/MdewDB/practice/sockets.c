#include <netinet/in.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <fcntl.h>
#include <unistd.h>
#include "ConsolePrintManager.h"
#include <pthread.h> 
#include <semaphore.h>
#include <time.h>

#define PORT 5039 
#define true 1 
#define false 0


const int NUM_USERS = 3; 

typedef struct {
    int accepted_socket_fd; 
}userThread; 


void* run(void* args) {
    userThread* userthread = (userThread*)args;

      while(true) {
    char buffer[2048]; 
    printf("server is waiting for user thread with socket fd : %d\n", userthread->accepted_socket_fd);
    ssize_t size = read(userthread->accepted_socket_fd, buffer, 1024);  
    printf("data was read by the server from user with socket's file descriptor %d\n", userthread->accepted_socket_fd);
    buffer[size] = 0;
    printf("client sent a message: %s", buffer);
//     char* message = malloc(10000); 
//     snprintf(message, 10000, "hello user(socket fd ::: %d), I got the message!\nHere is your message in red : %s%s%s",
//         userthread->accepted_socket_fd,getColor("red"), buffer, 
// getColor("black")); 
//     write(userthread->accepted_socket_fd, message, strlen(message)); 
//     printf("data was sent to a user with socket's file descriptor %d\n", userthread->accepted_socket_fd);     
}
    close(userthread->accepted_socket_fd); 
    free(userthread);
    return NULL; 
}


//initiaiting debugging procedure for finding out why server interprets request sent from one user as sent by other user. 

//first of all, we shall understand what listen's second argument does 
//and how does the program behave when thread number is very very large
//I will try changing listening queue to a large number and see what is happening
//same thing happens, so listen is not the problem
//now I will increase number of maximum allowed users to see what happens
//if request is sent from a particular thread, write happens just fine 
//if request is sent from a thread, then from other thread, server understands that other thread
//but if then request is sent from any other threads, the one thread only gets all the answers
//this is strange behaviour. 

//newest development to this bug: 
//bug occurs in the following way, the latest thread is the one that gets data back always
//whenever we start up a new user, for some reason server registers him as the one sending the data 
//and writes back data to it. meaning that the newest thread runs always. while other threads are waiting. 
//this means whatever our accepted sockets are connected to is really concurrent
//to test this out, I will sleep the thread after read and write and see what happens. 


//now as I have understood how this communication works, I will create a dispatcher module that will handle 
//new users automatically and be a middleman between a user and a server. 


int main(int argc, char** argv) { 
    pthread_t user_threads[NUM_USERS]; //NUM_USERS users 
    int i = 0; 
    int socket_fd; //listening socket 
    socket_fd = socket(AF_INET, SOCK_STREAM, 0); 

    struct sockaddr_in sockaddr;
    sockaddr.sin_port = htons(PORT); //names a socket, tells is on what port to listen to 
    sockaddr.sin_family = AF_INET; //respecified the domain 
    sockaddr.sin_addr.s_addr = INADDR_ANY;

    bind(socket_fd, (struct sockaddr*)&sockaddr, sizeof(sockaddr)); 

    listen(socket_fd, 1000);

    while(true) {
    unsigned int struct_size = sizeof(sockaddr);  
    int accepted_socket_fd = accept(socket_fd, (struct sockaddr*)&sockaddr, &struct_size); 
        if(i >= NUM_USERS)break;
        userThread* userthread = malloc(sizeof(userThread)); userthread->accepted_socket_fd=accepted_socket_fd;
        printf("i was %d when new user thread was created, socket file descriptor is %d...\n", i, accepted_socket_fd);
        pthread_create(&user_threads[i++],0, run, userthread);
    }

    printf("out of loop!\n");
    for(int j = 0; j < NUM_USERS; j++) {
        pthread_join(user_threads[i], NULL);
    }
    printf("server gotta rest now...\n");
    close(socket_fd); 
    return 0; 
}


