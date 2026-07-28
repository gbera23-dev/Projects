
use std::collections::HashMap;

const STR_SPLIT: &str = "\"";
const START_JSON: &str = "{";
const END_JSON: &str = "}"; 
const START_JSON_LIST: &str = "["; 
const END_JSON_LIST: &str = "]";
const KEY_VAL_SEPARATOR: &str = ":";
const SPLIT_QUERY_SYMBOL: &str = "/";
 
 #[derive(Debug)]
pub enum JsonVal {
    SimpleVal(Option<String>), 
    ComplexVal(Option<HashMap<String, JsonVal>>)
}

pub struct JsonMap {
    internal_map: HashMap<String, JsonVal>
}


impl JsonMap {
    
    pub fn send_query(&self, query: &str) -> Option<Vec<String>> {
        
        let m = &self.internal_map; 

        let mut curr_val = Option::<&JsonVal>::None; 

        for str in query.split(SPLIT_QUERY_SYMBOL) {
            if curr_val.is_none() {
                curr_val = m.get(str); 
                continue; 
            }
            if curr_val.unwrap().is_complex_val() {
                curr_val = curr_val.unwrap().extract_complex_val().as_ref().unwrap().get(str);
            }
        }
        
        if curr_val.is_none() {return Option::None};

        let curr = curr_val.unwrap();
        if curr.is_simple_val() {
            return Option::Some(vec![curr.extract_simple_val().clone().unwrap()]);
        } 
        self.handle_complex_res(curr)
    } 

    fn handle_complex_res(&self, curr: &JsonVal) -> Option<Vec<String>>{
        let mut sol = Vec::new(); 

        let curr_map = curr.extract_complex_val().as_ref().unwrap();

        for key in curr_map.keys() {
            sol.push(format!("{}{}", key.clone(), "..."));
        }
        Option::Some(sol)
    }
}


impl JsonVal {
    pub fn extract_simple_val(&self) -> &Option<String> {
        match self {
            JsonVal::ComplexVal(_) => {&Option::None}, 
            JsonVal::SimpleVal(val) => {&val}
        }
    }

    pub fn extract_complex_val(&self) -> &Option<HashMap<String, JsonVal>> {
        match self {
            JsonVal::ComplexVal(val) => {&val}, 
            JsonVal::SimpleVal(_) => {&Option::None}
        }
    }

    pub fn is_simple_val(&self) -> bool {
        match self {
            JsonVal::ComplexVal(_) => {false}, 
            JsonVal::SimpleVal(_) => {true}
        }
    }
    
    pub fn is_complex_val(&self) -> bool {
        match self {
            JsonVal::ComplexVal(_) => {true}, 
            JsonVal::SimpleVal(_) => {false}
        }
    }

}

pub fn parse_json(mut json_data:  String) -> Result<JsonMap, String> {

    json_data = clean_json_data(json_data);

    let vec = build_json_token_vec(json_data);

    let mut is_visited = vec![false; vec.len()];

    let map = build_json_map(&vec, &mut is_visited);

    Result::Ok(JsonMap{internal_map: map})
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