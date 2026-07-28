
use std::collections::HashMap;

const STR_SPLIT: &str = "\"";
const START_JSON: &str = "{";
const END_JSON: &str = "}"; 
const START_JSON_LIST: &str = "["; 
const END_JSON_LIST: &str = "]";
const KEY_VAL_SEPARATOR: &str = ":";
 
 #[derive(Debug)]
pub enum JsonVal {
    SimpleVal(Option<String>), 
    ComplexVal(Option<HashMap<String, JsonVal>>)
}

pub fn parse_json(mut json_data:  String) -> Result<HashMap<String, JsonVal>, String> {

    json_data = clean_json_data(json_data);

    let vec = build_json_token_vec(json_data);

    let mut is_visited = vec![false; vec.len()];

    let map = build_json_map(&vec, &mut is_visited);

    Result::Ok(map)
}

fn build_json_map(vec: &Vec<String>,  is_visited: &mut Vec<bool>) -> HashMap<String, JsonVal> {

    let mut map = 
    HashMap::<String, JsonVal>::new();

    for (idx, str) in vec.iter().enumerate() {
        if str==START_JSON || is_visited[idx] {continue} 

        is_visited[idx] = true;

        if str==END_JSON{break}

        if str==KEY_VAL_SEPARATOR {
            let key = vec[idx-1].clone();

            let val = if vec[idx+1] != START_JSON {JsonVal::SimpleVal(Option::Some(vec[idx+1].clone()))}

            else {
                JsonVal::ComplexVal(Option::Some(build_json_map(&vec, is_visited)))
            };

            map.insert(key, val); 
        }
    }

    map
}


fn clean_json_data(json_data: String) -> String {
    let mut cleaned_up_data = String::new(); 
    for ch in json_data.bytes().enumerate() {
        if ch.1!=b' ' {
            cleaned_up_data.push(ch.1 as char);

            if ch.1==*END_JSON.as_bytes().get(0).unwrap() || 
            ch.1==*END_JSON_LIST.as_bytes().get(0).unwrap() ||
            ch.1==*KEY_VAL_SEPARATOR.as_bytes().get(0).unwrap() {
            
                cleaned_up_data.push('\"')
            }
        };
    } 
    cleaned_up_data
}

fn build_json_token_vec(json_data: String) -> Vec<String> {
    
    let mut token_vec = Vec::<String>::new(); 

    for str in json_data.split("\"") {

        if str == "" {continue}

        token_vec.push(str.replace
            (|c| c==' ' || c=='\n', ""));
    }

    token_vec
}