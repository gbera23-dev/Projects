#include "functions.h"
#include <stdio.h> 
#include <unistd.h> 
#include <stdlib.h> 
#include <sys/stat.h>
#include <sys/types.h>
#include "../../lru_cache/lru_cache.h"
#include <time.h> 
#include <string.h> 
#define MAX_COMMANDS 1024
#define BUFF_SIZE 2048 
#define STDOUT_FD 1 


// array
//we shall expose array of Commands so that dispatcher traverses through it and registers them one by one
Command* cmds[MAX_COMMANDS];


//functions 

int mr_meeseeks_f(int argc, void* argv[]) {
    char** token_arr = (char**)argv[1]; 
    token_arr[0] = "./my_terminal"; 
    token_arr[1] = NULL; 
    printf("PUFFF!!.."); 
    printf("It is meee, meeseeks! Look at mee!!\n"); 
    return 0; 
}

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

int makedir_f(int argc, void* argv[]) {
    char** token_arr = (char**)argv[1];
    char* dir_name = token_arr[1]; 
    int res = mkdir(dir_name, 0700);

    if(res == 0) {
        printf("Directory has been created!\n"); 
    }
    else {
        printf("Directory creation failed!..\n"); 
    }

    return 1; 
}

int chdir_f(int argc, void* argv[]) {
    char** token_arr = (char**)argv[1]; 
    char* dir_name = token_arr[1]; 
    int res = chdir(dir_name);
    
    if(res == 0) {
        printf("Directory has changed successfully!\n"); 
    }
    else {
        printf("Failed to change a current directory!..\n"); 
    }
    return 1; 
}


int currdir_f(int argc, void* argv[]) {
    char buff[BUFF_SIZE];
    getcwd(buff, BUFF_SIZE);
    printf("Your current directory is %s. Do not get LOST AGAIN!\n", buff); 
    return 1; 
}

int rmdir_f(int argc, void* argv[]) {
    char** token_arr = (char**)argv[1]; 
    char* dir_name = token_arr[1]; 
    int res = rmdir(dir_name); 

    if(res == 0) {
        printf("Directory has been deleted successfully!.. Gone, but not forgotten...\n"); 
    }
    else {
        printf("Now you are gonna just throw it all away, huh? Clean the directory before deletion!\n");
    }
    return 1; 
}

int time_f(int argc, void* argv[]) {
    struct timespec tspc;
    struct tm* local_time;
    char time_string[32];  
    int stat = clock_gettime(CLOCK_REALTIME, &tspc); 
    if(stat != 0) {
        printf("could not determine time, approaching black hole...\n"); 
    }
    else {
        local_time = localtime(&tspc.tv_sec);
        if(local_time == NULL) {
            printf("failed to determine local time!..\n"); 
        }

        printf("Time is %d:%d:%d okoloko\n", local_time->tm_hour, local_time->tm_min, local_time->tm_sec);
    }
    return 1; 
}

int ultq_f(int argc, void* argv[]) {
    char* dramatic_pause_txt = "listen.... Carefully... answer is: ";
    write(STDOUT_FD, dramatic_pause_txt, strlen(dramatic_pause_txt));
    sleep(3);
    printf("42\n");  
    return 1; 
}

//registrations 

void reg_meeseeks_cmd(int* idx) {
    cmds[(*idx)++] = construct_command("SPAWNMEESEEKS", mr_meeseeks_f,
    "Command spawns new meeseeks"); 
}

void reg_ultq_cmd(int* idx) {
    cmds[(*idx)++] = construct_command("ANSWER_TO_ULTIMATE_QUESTION", ultq_f, 
        "Command used to resolve existential crisis"); 
}

void reg_time_cmd(int* idx) {
    cmds[(*idx)++] = construct_command("GETTIME", time_f, "Command used to get current system time"); 
}

void reg_rmdir_cmd(int* idx) {
    cmds[(*idx)++] = construct_command("RMDIR", rmdir_f, "Command used to delete a empty directory"); 
}

void reg_currdir_cmd(int* idx) {
    cmds[(*idx)++] = construct_command("CURRDIR", currdir_f, "Command used to get current directory"); 
}

void reg_chdir_cmd(int* idx) {
    cmds[(*idx)++] = construct_command("CHDIR", chdir_f, 
        "Command used to change a current working directory"); 
}

void reg_makedir_cmd(int* idx) {
    cmds[(*idx)++] = construct_command("MAKEDIR", makedir_f, "Command used to make a new directory"); 
}

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
    reg_makedir_cmd(&idx); 
    reg_chdir_cmd(&idx); 
    reg_currdir_cmd(&idx);
    reg_rmdir_cmd(&idx);  
    reg_time_cmd(&idx); 
    reg_ultq_cmd(&idx); 
    reg_meeseeks_cmd(&idx); 
    cmds[idx]=NULL; 
}


Command* get_cmd(int idx) {
    return cmds[idx]; 
}
