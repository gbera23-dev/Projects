

#ifndef HASHMAP
#define HASHMAP 


typedef int (*hash_funct_t) (void* key); 

typedef int (*cmp_funct_t) (void* first, void* second);

typedef void* (*dup_funct_t) (void* obj); 

typedef struct Hashnode {
    void* key; 
    void* val; 
    int hash_val;
    struct Hashnode* next; 
} Hashnode; 

typedef struct Bucket {
    Hashnode* head; 
    Hashnode* tail; 
    int bucket_len; 
} Bucket; 

typedef struct Hashmap {
    Bucket* bucket_array;
    int num_buckets; 
    int num_elems; 
    hash_funct_t hash_function;  
    cmp_funct_t cmp_function; 
    dup_funct_t dup_function; 
} Hashmap; 

Hashmap* hashmap_create(int init_capacity, hash_funct_t hash_function, cmp_funct_t comp_function, 
    dup_funct_t dup_function); 

void hashmap_put(Hashmap* hashmap, void* key, void* val); 

void* hashmap_get(Hashmap* hashmap, void* key); 

int hashmap_size(Hashmap* hashmap);

void hashmap_remove(Hashmap* hashmap, void* key); 

int hashmap_contains_key(Hashmap* hashmap, void* key); 

void hashmap_destroy(Hashmap* hashmap); 

#endif