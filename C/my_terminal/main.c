#include <unistd.h> 
#include <stdio.h> 
#include <string.h> 
#include <sys/types.h> 
#include <sys/wait.h>
#include <signal.h> 

#define PRINT(arg) (write(1, (arg), sizeof(arg)-1))

//constant variables 
const int STDIN_FD = 0; 
const int STDOUT_FD = 1; 
const int BUFFER_LEN = 1024; 
const int TOKEN_ARR_LEN = 128; 

//status messages 
const char ERROR_MSG[] = "command failed to execute...\n";
const char GOODBYE_MSG[] = "goodbye!\n";
const char START_MSG[] = "Starting execution...\n";
const char END_MSG[] = "---------------------\n"; 
const char LISTEN_MSG[] = "Listening for input!..\n";

//special commands
const char CLEAR_CMD[] = "clear";  

int tokenize(char* buffer, char** token_arr) {

    //using strtok utility 
    char* token = strtok(buffer, " ");
    int curr_idx = 0;

    while(token != NULL) { 
        token_arr[curr_idx++] = token; 
        token = strtok(NULL, " ");
    }

    token_arr[curr_idx] = NULL;

    return curr_idx; 
}

int spawn_child_process(char* argv[]) {
    int child_pid = fork(); 

    if(child_pid == 0) {
        int status = execvp(argv[0], argv);
        if(status==-1) {
            PRINT(ERROR_MSG);
            kill(getpid(), SIGTERM);
        }
        return 0; 
    }
    
    return child_pid; 
}

int wait_for_child_death(int child_pid) {
    int status = 0; 
    wait(&status); 

    //if child did not die on its own, strangle it yourself
    kill(child_pid, SIGKILL);

    return status; 
}

int execute_special_command(char** token_arr, int num_tokens) {
    return 0; 
}

void init() {
}

/*
PULSE creation. 
give terminal heart. 
it listens on STDIN, and writes to STDOUT 
*/
int main(int argv, char* argc[]) {
    PRINT(START_MSG);
    init(); 
    char buffer[BUFFER_LEN];
    char* token_arr[TOKEN_ARR_LEN];

    while(1) {
        PRINT(LISTEN_MSG);

        int num_read = read(STDIN_FD, buffer, BUFFER_LEN);

        if(num_read<=1)continue;
        
        buffer[num_read-1]='\0'; 

        if(strcmp(buffer, "EXIT")==0) {
            break; 
        }

        int num_tokens = tokenize(buffer, token_arr);

        if(execute_special_command(token_arr, num_tokens)) {
            continue; 
        }

        int child_pid = spawn_child_process(token_arr);

        int status = wait_for_child_death(child_pid); 

        PRINT(END_MSG);
    }
    PRINT(GOODBYE_MSG);
    return 0; 
}