#include "lru_cache.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h> 
#define min(arg1, arg2) (arg1) < (arg2) ? (arg1) : (arg2); 


const int DEF_CAPACITY = 5; 
const char* DEF_VAL = "DEFAULT"; 

lru_cache* create(int cache_cap) {
    if(cache_cap<=1)cache_cap=DEF_CAPACITY;

    lru_cache* cache = malloc(sizeof(lru_cache));
    
    cache->cache_capacity = cache_cap; 
    cache->curr_size = 1; 
    cache->head = malloc(sizeof(Node)); 
    
    cache->head->command = strdup(DEF_VAL); 

    cache->head->prev = NULL; 
    cache->head->nxt = NULL; 
    cache->tail = cache->head; 

    return cache; 
}

void remove_least_recent_node(lru_cache* cache) {
    Node* tmp = cache->head; 
    cache->head = cache->head->nxt; 

    free(tmp->command);
    free(tmp); 
    cache->head->prev=NULL; 
}

void add_to_cache(lru_cache* cache, char* cmd) {
    char* cpy_data = strdup(cmd); 

    Node* new_node = malloc(sizeof(Node)); 
    new_node->command = cpy_data; 
    new_node->prev = cache->tail;
    cache->tail->nxt = new_node; 
    new_node->nxt = NULL; 
    cache->tail = new_node; 

    if(cache->curr_size==cache->cache_capacity) {
        remove_least_recent_node(cache); 
    }

    cache->curr_size = min(cache->curr_size+1, cache->cache_capacity); 
}

char* get_recent(lru_cache* cache) {
    return cache->tail->command;
}

char* get_not_that_recent(lru_cache* cache, int left_from_last) {
    Node* trav = cache->tail; 
    while(left_from_last>0 && trav->prev != NULL) {
        trav = trav->prev; 
        left_from_last--;
    }
    return trav->command; 
}

char** get_all_curr_data(lru_cache* cache) {
    char** data = malloc(sizeof(char*)*cache->curr_size);

    Node* trav = cache->head;
    for(int i = 0; i < cache->curr_size && trav!=NULL; i++) {
        data[i] = trav->command;
        trav = trav->nxt; 
    }

    return data; 
}

void destroy_cache(lru_cache* cache) {
    Node* head = cache->head; 
    Node* tmp = NULL; 

    while(head!=NULL) {
        tmp=head->nxt;
        free(head->command);
        free(head); 
        head=tmp; 
    }

    free(cache);
}

