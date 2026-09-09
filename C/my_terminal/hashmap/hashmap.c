
#include "hashmap.h"
#include <unistd.h> 
#include <stdlib.h> 
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
Hashmap* hashmap_create(int init_capacity, hash_funct_t hash_function, 
    cmp_funct_t cmp_function, dup_funct_t dup_function, 
    free_funct_t key_free_function, free_funct_t val_free_function) {
    Hashmap* hashmap = malloc(sizeof(Hashmap)); 
    hashmap->num_buckets=init_capacity!=-1 ? init_capacity : DEFAULT_CAPACITY;
    hashmap->num_elems=0; 
    hashmap->hash_function = hash_function; 
    hashmap->cmp_function = cmp_function; 
    hashmap->dup_function = dup_function; 
    hashmap->key_free_function = key_free_function;
    hashmap->val_free_function = val_free_function;
    hashmap->bucket_array = malloc(hashmap->num_buckets*sizeof(Bucket)); 

    for(int i = 0; i < hashmap->num_buckets; i++) {
        hashmap->bucket_array[i].head = NULL; 
        hashmap->bucket_array[i].tail = NULL; 
        hashmap->bucket_array[i].bucket_len = 0; 
    }
    return hashmap;  
} 

void hashmap_put(Hashmap* hashmap, void* key, void* val) {
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
        hashmap->num_elems++;
    }
}

void* hashmap_get(Hashmap* hashmap, void* key) {
    int hash_val = hashmap->hash_function(key); 
    int idx = hash_val % hashmap->num_buckets; 

    Hashnode* trav = hashmap->bucket_array[idx].head; 

    while(trav != NULL) {
        if(hashmap->cmp_function(key, trav->key) == 0) {
            return trav->val; 
        } 
        trav = trav->next; 
    }

    return NULL; 
}

void hashmap_remove(Hashmap* hashmap, void* key) {

    int hash_val = hashmap->hash_function(key); 
    int idx = hash_val % hashmap->num_buckets; 

    Hashnode* trav = hashmap->bucket_array[idx].head; 

    if(trav == NULL)return; 
    if(hashmap->cmp_function(key, trav->key) == 0) {
        Hashnode* tmp = trav;
        if(tmp->next!=NULL){
            hashmap->bucket_array[idx].head = tmp->next; 
        }
        else {
            hashmap->bucket_array[idx].head = NULL; 
            hashmap->bucket_array[idx].tail = NULL; 
        }
        free(tmp); 
        hashmap->bucket_array[idx].bucket_len--; 
        hashmap->num_elems--;
        return; 
    }

    while(trav != NULL && trav->next != NULL) {
        if(hashmap->cmp_function(trav->next->key, key)==0) {
            Hashnode* tmp = trav->next; 
            trav->next = tmp->next; 
            free(tmp);  
            break; 
        }
    }
    hashmap->num_elems--;
    hashmap->bucket_array[0].bucket_len--; 
}

int hashmap_contains_key(Hashmap* hashmap, void* key) {
    return hashmap_get(hashmap, key)!=NULL; 
}

void free_linkedlist(Hashmap* hashmap, Hashnode* hashnode) {
    while(hashnode!=NULL) {
        Hashnode* tmp = hashnode; 
        hashnode = hashnode->next; 
        if(hashmap->key_free_function!=NULL)hashmap->key_free_function(tmp->key);
        if(hashmap->val_free_function!=NULL)hashmap->val_free_function(tmp->val);
        free(tmp); 
    }
}

int hashmap_size(Hashmap* hashmap) {
    return hashmap->num_elems;
}


void hashmap_destroy(Hashmap* hashmap) {
    for(int i = 0; i < hashmap->num_buckets; i++) {
        free_linkedlist(hashmap, hashmap->bucket_array[i].head);
    }
    free(hashmap->bucket_array);
    free(hashmap);
}