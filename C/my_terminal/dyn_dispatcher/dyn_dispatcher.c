
#include "dyn_dispatcher.h"
#include "../lru_cache/lru_cache.h"
#include "../hashmap/hashmap.h"
#include "../hashmap/type_impls/strmap.h"
#include <string.h>
#include <stdio.h> 
#include <unistd.h>
#include <stdlib.h>
const int NUM_COMMANDS = 3; 
Hashmap* hmap; 

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

//registration logic 

Command* register_exit_cmd() {
    Command* cmd = malloc(sizeof(Command)); 
    cmd->name = "EXIT"; 
    cmd->handler = exit_f; 
    cmd->description = "Command used to toggle that application is eligible for destruction"; 
    return cmd; 
}

Command* register_up_cmd() {
    Command* cmd = malloc(sizeof(Command)); 
    cmd->name = "UP"; 
    cmd->handler = up_f; 
    cmd->description = "Command used to see previous commands"; 
    return cmd; 
}

Command* register_full_cmd() {
    Command* cmd = malloc(sizeof(Command)); 
    cmd->name = "FULL"; 
    cmd->handler = full_f; 
    cmd->description = "Command used to see all current data in cache";
    return cmd; 
}


void init_dispatcher() {
    hmap = hashmap_create(16, str_hash_f, str_cmp_f, str_dup_f, NULL, str_free_cmd_struct);
    Command* exit_cmd = register_exit_cmd(); 
    Command* full_cmd = register_full_cmd(); 
    Command* up_cmd = register_up_cmd();   
    hashmap_put(hmap, exit_cmd->name, exit_cmd); 
    hashmap_put(hmap, full_cmd->name, full_cmd); 
    hashmap_put(hmap, up_cmd->name, up_cmd); 
}

void destroy_dispatcher() {
    hashmap_destroy(hmap); 
}

int execute_dispatcher(char* cmd_name, int argc, void* argv[]) {
    Command* cmd = (Command*)hashmap_get(hmap, cmd_name); 
    if(cmd!=NULL) {
        return cmd->handler(argc, argv); 
    }
    return 0; 
}
