
#include "hashmap/hashmap.h"
#include <stdio.h>

const int DEFAULT_CAPACITY = 8; 



//utility functions 

void append_map(Hashmap* hashmap, int hash_val, int idx, void* key, void* val) {
    Hashnode* new_node = malloc(sizeof(Hashnode)); 
        new_node->hash_val = hash_val; 
        new_node->key = key;
        new_node->val = val;
        new_node->next = NULL;  

        if(hashmap->bucket_array[idx].tail == NULL) {
            hashmap->bucket_array[idx].head = new_node; 
            hashmap->bucket_array[idx].tail = new_node; 
        }
        else {
            hashmap->bucket_array[idx].tail->next = new_node; 
            hashmap->bucket_array[idx].tail = new_node; 
        }
        hashmap->bucket_array[idx].bucket_len++; 
}

//state functions 

Hashmap* create_hashmap(int init_capacity, hash_funct_t hash_function, cmp_funct_t cmp_function, dup_funct_t dup_function) {
    Hashmap* hashmap = malloc(sizeof(Hashmap)); 
    hashmap->num_buckets=init_capacity!=-1 ? init_capacity : DEFAULT_CAPACITY;
    hashmap->hash_function = hash_function; 
    hashmap->cmp_function = cmp_function; 
    hashmap->dup_function = dup_function; 
    hashmap->bucket_array = malloc(hashmap->num_buckets*sizeof(Bucket)); 

    for(int i = 0; i < hashmap->num_buckets; i++) {
        hashmap->bucket_array[i].head = NULL; 
        hashmap->bucket_array[i].tail = NULL; 
        hashmap->bucket_array[i].bucket_len = 0; 
    }
    return hashmap;  
} 

void put(Hashmap* hashmap, void* key, void* val) {
    int hash_val = hashmap->hash_function(key); 
    int idx = hash_val % hashmap->num_buckets; 

    Hashnode* trav = hashmap->bucket_array[idx].head; 
    
    while(trav!=NULL) {
        if(hashmap->cmp_function(trav->key, key)==0) {
            trav->val = val;
            break; 
        }
        trav = trav->next; 
    }

    if(trav == NULL) {
        append_map(hashmap, hash_val, idx, key, val); 
    }
}

void* get(Hashmap* hashmap, void* key) {
    return NULL; 
}

void* remove(Hashmap* hashmap, void* key) {
    return NULL; 
}

int contains_key(Hashmap* hashmap, void* key) {
    return 0; 
}

void free_linkedlist(Hashnode* hashnode) {
    while(hashnode!=NULL) {
        Hashnode* tmp = hashnode; 
        hashnode = hashnode->next; 
        free(tmp); 
    }
}

void destroy_hashmap(Hashmap* hashmap) {
    for(int i = 0; i < hashmap->num_buckets; i++) {
        free_linkedlist(hashmap->bucket_array[i].head);
    }
    free(hashmap->bucket_array);
    free(hashmap);
}