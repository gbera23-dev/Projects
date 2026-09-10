#include "../../dyn_dispatcher/dyn_dispatcher.h"

#ifndef FUNCTIONS
#define FUNCTIONS


//cmds access for outer libraries
Command* get_cmd(int idx); 

// functions 

int exit_f(int argc, void* argv[]);

int up_f(int argc, void* argv[]);

int full_f(int argc, void* argv[]);

int check_f(int argc, void* argv[]);

//registrations 

void reg_exit_cmd();

void reg_up_cmd();

void reg_full_cmd();

void reg_check_cmd();

void initialize_commands_array(); 

#endif  