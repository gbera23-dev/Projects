#include "strmap.h"
#include <stdlib.h>
#include <string.h>
#include "../../dyn_dispatcher/dyn_dispatcher.h"

int djb2(unsigned char *str) {
    unsigned int hash = 5381;
    int c;
    while ((c = *str++)) {
        hash = ((hash << 5) + hash) + c; 
    }
    return hash;
}

int str_hash_f(void* key) {
    return djb2((unsigned char*)key);
}

int str_cmp_f(void* first, void* second) {
    return strcmp((char*)first, (char*)second); 
}

void* str_dup_f(void* obj) {
    return strdup((char*)obj);
}

void str_free_key(void* key) {
    free((char*)key); 
}

void str_free_cmd_struct(void* obj) {
    free((Command*)obj);
}