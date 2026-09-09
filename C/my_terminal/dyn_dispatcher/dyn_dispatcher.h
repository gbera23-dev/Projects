

#ifndef DYN_DISPATCHER 
#define DYN_DISPATCHER 


/*General handler function that is executed*/
typedef int (*cmd_handler_t) (int argc, void* argv[]);

typedef struct Command { 
    const char* name; //function name 
    cmd_handler_t handler; 
    const char* description; 
} Command; 

int execute_dispatcher(char* cmd_name, int argc, void* argv[]);
void init_dispatcher(); 
void destroy_dispatcher(); 

#endif 