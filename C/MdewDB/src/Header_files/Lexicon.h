/*
: : : : :LEXICON: : : : : 
Lexicon is a special data structure, that allows fast addition, removal, maintanance of elements. on the 
abstraction layer, it is a set, that can efficiently determine not only whether string is in the set, but 
also whether some first part of the string(prefix) is in the set or not. Inherently, Lexicon uses a tree,
that, for the worst case, has for each node, 26 other nodes coming out of it. Each of the node simply maintains
a single byte(some character), so in the worst case scenario, I would need 26^10 approx 11 000 000 that is 
11 megabytes, but tradeoff here will be that I would find out whether lexicon maintains a string very fast. 
that is, the time complexity of finding a string p, with length |p| is O(|p|). for small strings like strings
with size 9, 10, 20, 30, the number of operations is miniscule. Lazy initialization of the lexicon will be 
used, so we will not need all 11 megabytes of memory. 

All operations regarding lexicon has time complexity O(|P|), where P is the word that is being deleted, added
or checked. 


*/

#ifndef _Lexicon_ 
#define _Lexicon_

/*
Struct node maintains
isWord integer, which is 0, if starting from root to this node generates a valid word and any other number 
if it does not and array of node pointers that maintain address of the next nodes in the tree. 
reference_count variable maintains number of non null terms inside nextLetters array. 
nextLetter array will be filled with NULLS, if a node is a leaf node, or if there does not exist a path to say 'a', we 
write NULL on that position that is 'a' - 'a' = 0 in this case. 
*/
typedef struct node {
    int isWord; 
    int reference_count;
    struct node* nextLetters[26]; 
}node; 


typedef struct {
    struct node* root;
    int num_words;  
}Lexicon; 

typedef enum {
    STATUS_OK, 
    STATUS_NOT_FOUND, 
    STATUS_DELETED,
    STATUS_INTERNAL_ERROR
}Status; 


/*
For each function below, user can provide an address of the status integer, and appropriate enumaration will 
be assigned on that address. If user does not care about status, he can simply pass NULL.
*/


/*
Creates a new lexicon and returns its address as Lexicon* 
*/
Lexicon* initLexicon(int* status);

/*
Adds a new word to the lexicon. 
puts status STATUS_OK, if addition was successful, STATUS_INTERNAL_ERROR otherwise
*/
void addWord(Lexicon* lex, char* word, int* status);

/*
Removes a word from the lexicon. 
puts status STATUS_DELETED, if removal was successful, STATUS_NOT_FOUND otherwise 
*/
void removeWord(Lexicon* lex, char* word, int* status);

/*
Looks for a word in the lexicon. 
Returns NULL, if neither a word, not any of its prefix is in the lexicon. 
Returns copy of prefix string, where lexicon failed to continue search of the word. 
*/
char* findWord(Lexicon* lex, char* word, int* status); 


/*
Given the prefix, traverses lexicon and looks for all words that are starting with prefix. 
If prefix is NULL, returns all strings currently maintained by a lexicon as a string array.
Otherwise, returns array of all strings that are starting with prefix.   
*/
char** getAllWords(Lexicon* lex, char* prefix, int* status); 

/*
Destroys the given lexicon
*/
void destroyLexicon(Lexicon* lex); 

#endif