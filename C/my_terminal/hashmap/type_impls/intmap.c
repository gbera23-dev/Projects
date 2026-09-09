#include "intmap.h"
#include <stdlib.h>

int int_hash_f(void* key) {
    if((*(int*)key) < 0)return 0; 
    return *(int*)key; 
}

int int_cmp_f(void* first, void* second) {
    if ((*(int*)first) == (*(int*)second))return 0;
    return -1; 
}

void* int_dup_f(void* obj) {
    int* sec = malloc(sizeof(int)); 
    (*sec) = *(int*)obj;
    return sec;  
}