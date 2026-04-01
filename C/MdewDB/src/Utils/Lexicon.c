#include <stdio.h> 
#include <string.h> 
#include <stdlib.h> 
#include "Lexicon.h"
#include <assert.h>

//Private Function Prototypes 
void addWordRec(node* root, char* word);
void findWordRec(node* root, char* word, char** prefix, int i, int* contains_word, 
    node** end_node_ref, node** last_divergent_node_ref, 
    int* branch_index);
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
void addWord(Lexicon* lex, char* word, int* status) {  
    if(!lex->num_words) {
        init(lex); 
    }
    addWordRec(lex->root, word);
    lex->num_words++; 
    if(status != NULL) {
        *status = STATUS_OK;
    } 
}

/*PRIVATE ACCESS: Recursively adds a new word to the tree*/
void addWordRec(node* root, char* word) {
    char current_letter = word[0];
    if(current_letter) {
        node* next_node = root->nextLetters[current_letter - 'a']; 
        if(!next_node) {
            next_node = createNode(!(strlen(word) - 1));    
            root->nextLetters[current_letter - 'a'] = next_node;    
            root->reference_count++;    
        }
        addWordRec(next_node, ++word); 
    } else {
        root->isWord = 1; 
    }
}
/*PRIVATE ACCESS: Recursively looks for a word in tree and fills in optional word 
information: 

char** prefix : reference to the longest prefix in tree that our word starts with

int* contains_word : reference to a int, which is 1 if word is maintained by tree

node** end_node_ref : reference to the node where word ends

node** last_divergent_node_ref : reference to largest common prefix of all 
the words currently in the lexicon with this 
word(note that largest common prefix is assumed to be strictly not be our word)

int* branch_index: reference to the index of the branch that needs to be cut off from last
divergent node 
*/
void findWordRec(node* root, char* word, char** prefix, int i, int* contains_word, 
node** end_node_ref, node** last_divergent_node_ref, int* branch_index) {
    char current_letter = word[0]; 
    if(current_letter) {
        node* next_node = root->nextLetters[current_letter - 'a']; 
        if(!next_node)return; 
        if(prefix)(*prefix)[i++] = current_letter; 
        
        if(last_divergent_node_ref && root->reference_count > 1) {
            *last_divergent_node_ref = root; 
            *branch_index = current_letter - 'a'; 
        }

        findWordRec(next_node, ++word, prefix, i, contains_word, end_node_ref,
             last_divergent_node_ref, branch_index); 
    } else {
        if(prefix)(*prefix)[i] = 0; 
        if(contains_word)*contains_word = root->isWord;

        if(end_node_ref)*end_node_ref = root;
    }
}


/*PRIVATE ACCESS: Initializes a new tree*/
void init(Lexicon* lex) {
    lex->root = createNode(0);
}

/*PRIVATE ACCESS: Creates a new node*/
node* createNode(int isWord) {
    node* res = malloc(sizeof(node));
    res->isWord = isWord; 
    res->reference_count = 0; 
    for(int i = 0; i < 26; i++) {
        res->nextLetters[i] = NULL; 
    }
    return res; 
}

/*
Removes a word from the lexicon. 
returns 1, if removal was successful, 0 otherwise 
*/
void removeWord(Lexicon* lex, char* word, int* status) {
    node* end_node = NULL; 
    node* last_div_node = NULL; 
    int branch_index; 
    findWordRec(lex->root, word, NULL, 0, NULL, &end_node, &last_div_node, 
        &branch_index); 
   
    if(!end_node) {
        if(status)*status = STATUS_NOT_FOUND;
        return;
    }

    if(end_node->reference_count) {
        end_node->isWord = 0;
        lex->num_words--;
        if(status)*status = STATUS_DELETED;
        return; 
    } 
    last_div_node->reference_count--; 
    last_div_node->nextLetters[branch_index] = NULL; 
    lex->num_words--; 
    if(status)*status = STATUS_DELETED;
}

/*
Looks for a word in the lexicon. 
Returns NULL, if neither a word, not any of its prefix is in the lexicon. 
Returns prefix string, where lexicon failed to continue search of the word. 
*/
char* findWord(Lexicon* lex, char* word, int* status) { 
    char* prefix = malloc(strlen(word)); 
    int contains_word = 0; 
    findWordRec(lex->root, word, &prefix, 0, &contains_word, NULL, NULL, NULL); 
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
// int main() { 
//     int status; 
//     Lexicon* new_lex = initLexicon(&status); 
//     addWord(new_lex, strdup("so"), &status); 
//     addWord(new_lex, strdup("pashastick"), &status); 
//     assert(new_lex->root->nextLetters['s' - 'a']);
//     assert(new_lex->root->nextLetters['s' - 'a']->nextLetters['o' - 'a']);
//     printf("%d\n", new_lex->root->nextLetters['s' - 'a']->nextLetters['o' - 'a']->isWord);
//     printf("%s\n",findWord(new_lex, strdup("soleimanpasha"), &status)); 
//     printf("%s\n", findWord(new_lex, strdup("pashast"), &status)); 
//         printf("%d\n", status);

//     printf("%s\n", findWord(new_lex, strdup("pashastomboli"), &status));
//     printf("%d\n", status); 

//        addWord(new_lex, strdup("pashastomboli"), &status);
//               addWord(new_lex, strdup("pashastomboliqqq"), &status);
//     printf("%d\n", status); 
//             printf("%s\n", findWord(new_lex, strdup("pashastomboliqqqqqqqqq"), &status));
//             printf("%d\n", status);

//              printf("string: %s ", findWord(new_lex, strdup("pas"), &status));
//              printf("status: %d\n", status); 
        
//              removeWord(new_lex, strdup("pashastomboli"), &status); 
//         printf("status is : %d\n", status); 
        
//         printf("%s\n", findWord(new_lex, strdup("pashastomboli"), &status));
        
//         printf("status should be 1 now: %d\n", status);

//         //lets create divergence and see if removeWord works 

//             addWord(new_lex, strdup("sona"), &status);
//             printf("status should be 0 ::: %d\n", status); 
//             addWord(new_lex, strdup("sopo"), &status); 
//             printf("status should be 0 ::: %d\n", status); 
//             addWord(new_lex, strdup("sorini"), &status); 
//             printf("status should be 0 ::: %d\n", status); 


//             removeWord(new_lex, strdup("sorini"), &status); 

//             printf("status should be 2 ::: %d\n", status); 

//             printf("string is : %s\n", findWord(new_lex, strdup("sorini"), &status)); 

//             printf("status should be 1 ::: %d\n", status); 

//             printf("string is : %s\n", findWord(new_lex, strdup("sopo"), &status)); 

//             printf("status should be 0 ::: %d\n", status); 

//             printf("string is : %s\n", findWord(new_lex, strdup("sona"), &status));
            
//             printf("status should be 0 ::: %d\n", status); 
//     return 0; 
// }