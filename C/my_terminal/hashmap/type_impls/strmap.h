

#ifndef STRMAP 
#define STRMAP

int str_hash_f(void* key);

int str_cmp_f(void* first, void* second);

void* str_dup_f(void* obj);

void str_free_key(void* key); 

void str_free_cmd_struct(void* obj);

#endif 