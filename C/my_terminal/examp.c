#include <stdio.h> 
#include "hashmap/hashmap.h"
#include <stdlib.h>
#include "hashmap/type_impls/strmap.h"
#include <sys/stat.h>
#include <sys/types.h>
#include <time.h> 
#include <string.h> 

#define NUM_ELEMS 500000

void print(Hashmap* hashmap) {
    for(int i = 0; i < NUM_ELEMS; i++) {
        printf("key: %d, val: %d\n", i, *(int*)hashmap_get(hashmap, &i));
    }
}

void test(Hashmap* hashmap) {
    for(int key = hashmap->num_elems-1; key >= 0; key--) { 
        char buff[1024];
        snprintf(buff, 17, "vuustr%d", key);
         int* val = hashmap_get(hashmap, buff);
         if((*val)!=(NUM_ELEMS-key)) {
            printf("gotcha!\n"); 
         }
    }
}

int main(int argc, char* argv[]) {
    struct timespec tspc; 
    int stat = clock_gettime(CLOCK_REALTIME, &tspc); 
    int clock_start_sec = tspc.tv_sec, clock_start_nano = tspc.tv_nsec;
    Hashmap* hashmap = hashmap_create(8, str_hash_f, str_cmp_f, str_dup_f, str_free_key, NULL);

    char* arr[NUM_ELEMS+1]; 
    int int_arr[NUM_ELEMS+1]; 
    
    for(int i = 0; i < NUM_ELEMS; i++) {
        char buff[1024];
        snprintf(buff, 1024, "vuustr%d", i);
        arr[i]=strdup(buff); 
        int_arr[i]=NUM_ELEMS-i; 
    } 

    for(int i = 0; i < NUM_ELEMS; i++) {
        hashmap_put(hashmap, arr[i], &int_arr[i]);
    }

    // let's write some tests 
    test(hashmap); 

    for(int i = 0; i < NUM_ELEMS; i++) { 
        char buff[1024];
        snprintf(buff, 1024, "vuustr%d", i);
	    hashmap_remove(hashmap, buff); 
    }
    
    hashmap_destroy(hashmap); 
    stat = clock_gettime(CLOCK_REALTIME, &tspc);
    int clock_end_sec = tspc.tv_sec, clock_end_nano = tspc.tv_nsec;

    int diff_sec = clock_end_sec-clock_start_sec; 
    printf("seconds needed: %d. nano start: %d, nano end: %d. diff_mili: %lf\n", diff_sec, clock_start_nano, clock_end_nano,
    ((double)(clock_end_nano-clock_start_nano)) / 1000000L ); 
    return 0; 
}
