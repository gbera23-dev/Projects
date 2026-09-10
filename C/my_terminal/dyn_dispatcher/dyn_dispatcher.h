

#ifndef DYN_DISPATCHER 
#define DYN_DISPATCHER 


/*General handler function that is executed*/
typedef int (*cmd_handler_t) (int argc, void* argv[]);

typedef struct Command { 
    char* name; //function name 
    cmd_handler_t handler; 
    char* description; 
} Command; 

//constructor for Command struct, instantiates in heap
Command* construct_command(char* cmd_name, cmd_handler_t funct, char* description); 

int execute_dispatcher(char* cmd_name, int argc, void* argv[]);
void init_dispatcher(); 
void destroy_dispatcher(); 

#endif