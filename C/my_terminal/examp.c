#include <stdio.h> 
#include "lru_cache/lru_cache.h"

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
    lru_cache* lru = create(3);
    for(int i = 0; i < 10; i++) {
        char ch[3]; 
        ch[0]='a'; ch[1] = 'b'; ch[2] = '0' + i; 
        add_to_cache(lru, ch); 
        printf("value is: %s\n", get_recent(lru)); 
        print_cache(lru->head); 
    }
    destroy_cache(lru); 
    return 0; 
}
