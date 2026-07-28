
use std::collections::HashMap;

const STR_SPLIT: &str = "\"";

pub enum JsonVal {
    SimpleVal(String), 
    ComplexVal(Option<HashMap<String, JsonVal>>)
}

pub fn parse_json(mut json_data:  String) -> Result<HashMap<String, JsonVal>, String> {

    json_data = clean_json_data(json_data);

    let vec = build_json_token_vec(json_data);

    println!("{:?}", vec); 

    let mut idx = 0; 

    let map = build_json_map(&vec, &mut idx);

    Result::Ok(map)
}

fn build_json_map(vec: &Vec<String>, idx: &i32) -> HashMap<String, JsonVal> {

    HashMap::<String, JsonVal>::new()
}


fn clean_json_data(json_data: String) -> String {
    let mut cleaned_up_data = String::new(); 
    for ch in json_data.bytes().enumerate() {
        if ch.1!=b' ' {
            cleaned_up_data.push(ch.1 as char);
            if ch.1==b'}' || ch.1==b']'  
            {cleaned_up_data.push('\"')}
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