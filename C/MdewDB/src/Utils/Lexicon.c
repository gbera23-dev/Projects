#include <stdio.h> 
#include <string.h> 
#include <stdlib.h> 
#include "Lexicon.h"
#include <assert.h>

//Private Function Prototypes 
void addWordRec(node* root, char* word);
void findWordRec(node* root, char* word, char** prefix, int i, int* contains_word);
void init(Lexicon* lex);
node* createNode(int isWord);


/*
Creates a new lexicon and returns its address as Lexicon* 
*/
Lexicon* initLexicon(int* status) {
    Lexicon* lexicon = malloc(sizeof(Lexicon)); 
    lexicon->num_words = 0; 
    lexicon->root = NULL; 
    return lexicon; 
}


/*
Adds a new word to the lexicon. 
returns 1, if addition was successful, 0 otherwise
*/
int addWord(Lexicon* lex, char* word, int* status) {  
    if(!lex->num_words) {
        init(lex); 
    }
    addWordRec(lex->root, word);
    lex->num_words++; 
    if(status != NULL) {
        *status = STATUS_OK;
    }
    return 1; 
}

/*PRIVATE ACCESS: Recursively adds a new word to the tree*/
void addWordRec(node* root, char* word) {
    char current_letter = word[0];
    if(current_letter) {
        node* next_node = root->nextLetters[current_letter - 'a']; 
        if(!next_node) {
            next_node = createNode(!(strlen(word) - 1));    
            root->nextLetters[current_letter - 'a'] = next_node;       
        }
        addWordRec(next_node, ++word); 
    } else {
        root->isWord = 1; 
    }
}
/*PRIVATE ACCESS: Recursively looks for a word in tree, builds the prefix and returns it*/
void findWordRec(node* root, char* word, char** prefix, int i, int* contains_word) {
    char current_letter = word[0]; 
    if(current_letter) {
        node* next_node = root->nextLetters[current_letter - 'a']; 
        if(!next_node)return; 
        (*prefix)[i++] = current_letter; 
        findWordRec(next_node, ++word, prefix, i, contains_word); 
    } else {
        (*prefix)[i] = 0; 
        *contains_word = root->isWord;
    }
}


/*PRIVATE ACCESS: Initializes a new tree*/
void init(Lexicon* lex) {
    lex->root = malloc(sizeof(node)); 
    lex->root->isWord = 0; 
    for(int i = 0; i < 26; i++) {
        lex->root->nextLetters[i] = NULL; 
    } 
}

/*PRIVATE ACCESS: Creates a new node*/
node* createNode(int isWord) {
    node* res = malloc(sizeof(node));
    res->isWord = isWord; 
    for(int i = 0; i < 26; i++) {
        res->nextLetters[i] = NULL; 
    }
    return res; 
}

/*
Removes a word from the lexicon. 
returns 1, if removal was successful, 0 otherwise 
*/
int removeWord(Lexicon* lex, char* word, int* status) {
    return 0; 
}

/*
Looks for a word in the lexicon. 
Returns NULL, if neither a word, not any of its prefix is in the lexicon. 
Returns prefix string, where lexicon failed to continue search of the word. 
*/
char* findWord(Lexicon* lex, char* word, int* status) { 
    char* prefix = malloc(strlen(word)); 
    int contains_word = 0; 
    findWordRec(lex->root, word, &prefix, 0, &contains_word); 
    if(status != NULL) {
        *status = strcmp(prefix, word) == 0 && contains_word ? STATUS_OK : STATUS_NOT_FOUND;
    }
    return prefix; 
}


/*
Given the prefix, traverses lexicon and looks for all words that are starting with prefix. 
If prefix is NULL, returns all strings currently maintained by a lexicon as a string array.
Otherwise, returns array of all strings that are starting with prefix.   
*/
char** getAllWords(Lexicon* lex, char* prefix, int* status) {
    return NULL; 
}

//temporary main, for testing purposes
int main() { 
    int status; 
    Lexicon* new_lex = initLexicon(&status); 
    int res = addWord(new_lex, strdup("so"), &status); 
    addWord(new_lex, strdup("pashastick"), &status); 
    assert(new_lex->root->nextLetters['s' - 'a']);
    assert(new_lex->root->nextLetters['s' - 'a']->nextLetters['o' - 'a']);
    printf("%d\n", new_lex->root->nextLetters['s' - 'a']->nextLetters['o' - 'a']->isWord);
    printf("%s\n",findWord(new_lex, strdup("soleimanpasha"), &status)); 
    printf("%s\n", findWord(new_lex, strdup("pashast"), &status)); 
        printf("%d\n", status);

    printf("%s\n", findWord(new_lex, strdup("pashastomboli"), &status));
    printf("%d\n", status); 
        printf("%d\n", addWord(new_lex, strdup("pashastomboliqqqqqqqqq"), &status));
                printf("%d\n", addWord(new_lex, strdup("pasha"), &status));

       printf("%s\n", findWord(new_lex, strdup("pashastomboli"), &status));
    printf("%d\n", status); 
            printf("%s\n", findWord(new_lex, strdup("pashastomboliqqqqqqqqq"), &status));
            printf("%d\n", status);

             printf("%s\n", findWord(new_lex, strdup("pasha"), &status));
            printf("%d\n", status);
    return 0; 
}