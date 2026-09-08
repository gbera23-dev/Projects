#include <stdio.h> 


int main(int argc, char* argv[]) {
    printf("hello!\n"); 
    printf("number of args is %d\n"
        ,argc);
    printf("args are: "); 
    for(int i = 0; i < argc; i++) {
        printf("%s ", argv[i]);
    }printf("\n"); 
    return 0; 
}