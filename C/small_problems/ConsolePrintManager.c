#include <stdio.h>
#include <stdlib.h>
#include <ctype.h> 
#include <string.h>
#include "ConsolePrintManager.h"
#define RED "\x1b[31m"
#define GREEN "\x1b[32m"
#define YELLOW "\x1b[33m"
#define BLACK "\x1b[0m"

/*module is used to provide easier way to print things on console. printf functionality
will be used to implement the behavior*/


char* toUpperCase(char* str) {
    char* cpy = strdup(str); 
    char* it = cpy; 
    while(*it != '\0') {
        *it = toupper(*it);
        it++;
    }
    return cpy; 
}

char* getColor(char* color) { 
    char* capitalizedColor = toUpperCase(color); 
    if(strcmp("RED", capitalizedColor) == 0){
        free(capitalizedColor);  
        return RED; 
    }
    if(strcmp("GREEN", capitalizedColor) == 0){
        free(capitalizedColor);  
        return GREEN; 
    }
    if(strcmp("YELLOW", capitalizedColor) == 0){
        free(capitalizedColor);  
        return YELLOW;
    } 
    if(strcmp("BLACK", capitalizedColor) == 0){
        free(capitalizedColor);  
        return BLACK;
    } 
    return "NOT FOUND"; 
}

void colorTheText(char* color) {
    printf("%s",getColor(color));
}
