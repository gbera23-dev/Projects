#include "functions.h"
#include <stdio.h> 
#include <unistd.h> 
#include <stdlib.h> 
#include "../../lru_cache/lru_cache.h"
#define MAX_COMMANDS 1024


// array
//we shall expose array of Commands so that dispatcher traverses through it and registers them one by one
Command* cmds[MAX_COMMANDS];


//functions 

int exit_f(int argc, void* argv[]) {
    int* exit_toggled = (int*)argv[3]; 
    *exit_toggled=1;
    return 1; 
}

int up_f(int argc, void* argv[]) {
    lru_cache* cache = (lru_cache*)argv[0]; 
    char** token_arr = (char**)argv[1]; 
    char* cached_val = get_not_that_recent(cache, atoi(token_arr[1]));
    printf("%s\n", cached_val); 
    return 1; 
}

int full_f(int argc, void* argv[]) {
    lru_cache* cache = (lru_cache*)argv[0]; 
    char** data = get_all_curr_data(cache);
    for(int i = 0; i < cache->curr_size; i++) {
        if(i == cache->curr_size-1)printf("%s", data[i]);
        else printf("%s, ", data[i]);
    }printf("\n");
    free(data); 
    return 1; 
}

int check_f(int argc, void* argv[]) {
    printf("Terminal is responding!..\n"); 
    return 1; 
}

//registrations 

void reg_exit_cmd(int* idx) {
    cmds[(*idx)++] = construct_command("EXIT", exit_f, 
        "Command used to toggle that application is eligible for destruction");
}

void reg_up_cmd(int* idx) {
    cmds[(*idx)++] = construct_command("UP", up_f, 
        "Command used to see previous commands");  
}

void reg_full_cmd(int* idx) {
    cmds[(*idx)++] = construct_command("FULL", full_f, 
        "Command used to see all current data in cache");  
}

void reg_check_cmd(int* idx) {
    cmds[(*idx)++] = construct_command("CHECK", check_f, 
        "Command checks that terminal is responsive"); 
}

void initialize_commands_array() {
    int idx = 0; 
    reg_exit_cmd(&idx); 
    reg_up_cmd(&idx);
    reg_full_cmd(&idx); 
    reg_check_cmd(&idx); 
    cmds[idx]=NULL; 
}


Command* get_cmd(int idx) {
    return cmds[idx]; 
}
