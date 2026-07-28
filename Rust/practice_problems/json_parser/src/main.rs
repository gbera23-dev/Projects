
mod json_parser; 

use std::fs;
use std::io; 
use std::collections::HashMap;

use crate::json_parser::JsonVal; 
use crate::json_parser::JsonMap;

fn main() {
    let mut file_name = String::new(); 
    println!("Please, input file name: "); 
    let _ = io::stdin().read_line(&mut file_name);
    println!("file name is {}", file_name);

    let res = std::fs::read_to_string(file_name.trim());
    let map: JsonMap;

    if res.is_err() {return}

    map = json_parser::parse_json(res.unwrap()).unwrap();

    let res = map.send_query
    ("system_master_data/user_profiles/user_01/security_settings/two_factor_enabled");

    println!("{:?}", res.unwrap().extract_simple_val().as_ref().unwrap());

}
