#include <stdio.h> 
#include "hashmap/hashmap.h"
#include <stdlib.h>
#include "hashmap/type_impls/intmap.h"

void print(Hashmap* hashmap) {
    for(int i = 0; i < 100; i++) {
        printf("key: %d, val: %d\n", i, *(int*)hashmap_get(hashmap, &i));
    }
}

int main(int argc, char* argv[]) {
    Hashmap* hashmap = hashmap_create(8, int_hash_f, int_cmp_f, int_dup_f, NULL, NULL);

    int arr[101]; for(int i = 0; i < 101; i++)arr[i]=i; 

    for(int i = 0; i < 100; i++) { 
        hashmap_put(hashmap, &arr[i], &arr[100-i]);
    }

    print(hashmap);

    printf("hashmap size is %d\n", hashmap_size(hashmap));
    int kj = 0;
    printf("contains key? %d\n", hashmap_contains_key(hashmap, &kj));
    int zj = -1;
    printf("does not contain key? %d\n", hashmap_contains_key(hashmap, &zj));

    for(int i = 0; i < 100; i++) {
        hashmap_remove(hashmap, &i); 
        printf("size is: %d\n", hashmap_size(hashmap));
    }
    hashmap_destroy(hashmap); 

    return 0; 
}
