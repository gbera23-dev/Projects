This is my miniature project, whose purpose is to open a json file, read it, and parse it into data structure. 

Idea is to have a module that has static function, which takes file name as an argument, opens it, reads its 
data and retrieves touple of hashmap<String, (String, Option<Hashmap>)>

Idea is the following: 

if we have json data {a: b, c: {g: f, t: c}}, then we will get: 

hashmap = {"a": ("b", Option::None), "c": ("hashmap", Option::Some({"g": ("f", Option::None), "t": ("c", Option::None)}))}.

full ownership to this hashmap is passed onto the client. 
