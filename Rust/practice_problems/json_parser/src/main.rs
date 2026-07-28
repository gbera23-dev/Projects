
mod json_parser; 

use std::fs;
use std::io; 
use std::collections::HashMap;

use crate::json_parser::JsonVal; 

fn main() {
    let mut file_name = String::new(); 
    println!("Please, input file name: "); 
    let _ = io::stdin().read_line(&mut file_name);
    println!("file name is {}", file_name);

    let res = std::fs::read_to_string(file_name.trim());
    let map: HashMap<String, JsonVal>;

    if res.is_err() {return}

    map = json_parser::parse_json(res.unwrap()).unwrap();
        
    for (size, val) in map.iter().enumerate() {
        println!("key is {}, val is {:?}", val.0, val.1);
    }
}
