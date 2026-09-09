#include <stdio.h> 
#include "lru_cache/lru_cache.h"
#include "dyn_dispatcher/dyn_dispatcher.h"

void print_cache(Node* head) {
    while(head!=NULL) {
        if(head->nxt==NULL) {
            printf("%s", head->command); 
            break; 
        }
        printf("%s, ", head->command);
        head=head->nxt; 
    }printf("\n"); 
}

int main(int argc, char* argv[]) {
    init_dispatcher();
    lru_cache* lru = create(3);
    for(int i = 0; i < 10; i++) {
        char ch[3]; 
        ch[0]='a'; ch[1] = 'b'; ch[2] = '0' + i; 
        add_to_cache(lru, ch); 
    }
    char* token_arr[2]; 
    token_arr[0] = "UP"; 
    token_arr[1] = "3"; 
    void* arr[2];
    arr[0] = lru; arr[1] = token_arr;  
    
    int res = execute("full_cmd", 2, arr);
    destroy_dispatcher();
    destroy_cache(lru); 
    return 0; 
}
