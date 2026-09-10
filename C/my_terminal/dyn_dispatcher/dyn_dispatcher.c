
#include "dyn_dispatcher.h"
#include "../lru_cache/lru_cache.h"
#include "../hashmap/hashmap.h"
#include "../hashmap/type_impls/strmap.h"
#include "special_functions/functions.h"
#include <string.h>
#include <stdio.h> 
#include <unistd.h>
#include <stdlib.h>

//global hashmap storage for dispatching 
Hashmap* hmap; 

Command* construct_command(char* cmd_name, cmd_handler_t funct, char* description) {
    Command* cmd = malloc(sizeof(Command)); 
    cmd->name=cmd_name; 
    cmd->handler=funct; 
    cmd->description=description; 
    return cmd; 
}

void register_function(Command* com) {
    hashmap_put(hmap, com->name, com);  
}

void register_functions() {
    int i = 0; 
    for(int i = 0; get_cmd(i)!=NULL; i++) {
        register_function(get_cmd(i)); 
    }
}

void init_dispatcher() {
    hmap = hashmap_create(16, str_hash_f, str_cmp_f, str_dup_f, NULL, str_free_cmd_struct);
    initialize_commands_array();
    register_functions(); 
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
