#include <unistd.h> 
#include <stdio.h> 
#include <stdlib.h>
#include <string.h> 
#include <sys/types.h> 
#include <sys/wait.h>
#include <signal.h> 
#include "lru_cache/lru_cache.h"

//constant variables 
const int STDIN_FD = 0; 
const int STDOUT_FD = 1; 
const int BUFFER_LEN = 1024; 
const int TOKEN_ARR_LEN = 128; 
const int CACHE_CAPACITY = 15;
const char STR_BINDER_CH = '`';

//status messages 
const char ERROR_MSG[] = "command failed to execute...\n";
const char GOODBYE_MSG[] = "goodbye!\n";
const char START_MSG[] = "Starting execution...\n";
const char END_MSG[] = "---------------------\n"; 
const char LISTEN_MSG[] = "Listening for input!..\n";

//special commands
const char EXIT_CMD[] = "EXIT";
const char UP_CMD[] = "UP";
const char DOWN_CMD[] = "DOWN";
const char FULL_CACHE[] = "FULL"; 


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
            printf("%s", ERROR_MSG);
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

int execute_special_command(lru_cache* cache, char** token_arr, int num_tokens, int* exit_toggled) {
    if(strcmp(token_arr[0], "EXIT")==0) {
        *exit_toggled=1; 
        return 1;
    }
    else if(strcmp(token_arr[0], UP_CMD)==0) {
        char* cached_val = get_not_that_recent(cache, atoi(token_arr[1]));
        printf("%s\n", cached_val); 
        return 1;
    }

    else if(strcmp(token_arr[0], FULL_CACHE)==0) {
        char** data = get_all_curr_data(cache);
        for(int i = 0; i < cache->curr_size; i++) {
            if(i == cache->curr_size-1)printf("%s", data[i]);
            else printf("%s, ", data[i]);
        }printf("\n");
        free(data); 
        return 1; 
    }
    return 0; 
}

int execute_normal_command(char** token_arr) {
    return wait_for_child_death(spawn_child_process(token_arr));
}

void bind_strings(char* buffer, int num_read){
    int bracket_present = 0; 
    for(int i = 0; i < num_read; i++) {
        if(buffer[i]=='\"') {
            bracket_present ^= 1; continue; 
        } 

        if((buffer[i] == ' ') && bracket_present) {
            buffer[i] = STR_BINDER_CH;
        }
    }
}

void replace_char(char* str, char to_replace, char replace_into){
    for(int i = 0; i < strlen(str); i++) {
        if(str[i]==to_replace)str[i]=replace_into; 
    }
}

void unbind_strings(char** tokens, int num_tokens) {
    for(int i = 0; i < num_tokens; i++) {
        char* token = tokens[i]; 
        if(token[0]=='\"') {
            replace_char(token, STR_BINDER_CH, ' '); 
        }
    }
}


void init() {}

void cleanup(lru_cache* cache) {
    destroy_cache(cache); 
}

/*
PULSE creation. 
give terminal heart. 
it listens on STDIN, and writes to STDOUT 
*/
int main(int argv, char* argc[]) {
    printf("%s", START_MSG);
    lru_cache* cache = create(CACHE_CAPACITY); 
    init(); 
    char buffer[BUFFER_LEN];
    char* token_arr[TOKEN_ARR_LEN];
    int exit_toggled=0; 

    while(1) {
        printf("%s", LISTEN_MSG);

        int num_read = read(STDIN_FD, buffer, BUFFER_LEN);

        if(num_read<=1)continue;
        
        buffer[num_read-1]='\0'; 

        bind_strings(buffer, num_read);

        int num_tokens = tokenize(buffer, token_arr);

        unbind_strings(token_arr, num_tokens);

        if(execute_special_command(cache, token_arr, num_tokens, &exit_toggled)) {
            add_to_cache(cache, buffer); 
            if(exit_toggled)break; 
            continue; 
        }

        int status = execute_normal_command(token_arr);

        add_to_cache(cache, buffer);

        printf("%s", END_MSG);
    }
    cleanup(cache); 
    printf("%s", GOODBYE_MSG);
    return 0; 
}