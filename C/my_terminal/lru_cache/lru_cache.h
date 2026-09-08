

#ifndef LRU_CACHE 
#define LRU_CACHE

typedef struct Node {
    char* command; 
    struct Node* prev; 
    struct Node* nxt; 
} Node; 

typedef struct lru_cache {
    Node* head; 
    Node* tail; 
    int cache_capacity; 
    int curr_size; 
} lru_cache;

lru_cache* create(int cache_cap); 

void add_to_cache(lru_cache* cache, char* cmd); 

char* get_recent(lru_cache* cache);

char* get_not_that_recent(lru_cache* cache, int left_from_last); 

char** get_all_curr_data(lru_cache* cache); 

void destroy_cache(lru_cache* cache); 


#endif 