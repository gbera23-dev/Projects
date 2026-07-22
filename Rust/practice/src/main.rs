


use std::env::{self, args};
use std::fs::{self, read_to_string}; 
use std::collections::HashMap;
use std::vec; 

fn main() {
    if std::env::args().len() != 2 {
        panic!("Two arguments must be provided!");
    }
    let opt_file_name = std::env::args().nth(1); 

    match opt_file_name {
        Some(ref val) => {
            println!("Found: {}", val);
        }
        None => {
            println!("file name could not be obtained");
            panic!("Aborting execution...");
        }
    }; 

    let file_name: &str = &opt_file_name.unwrap();

    let str_data = std::fs::read_to_string(file_name);

    match str_data {
        Ok(ref val) => {
            println!("data: {}", val);
        }, 
        Err(e) => {
            println!("Failed to read file: {}", e);
            panic!("Aborting execution");
        }
    }

    let data = str_data.unwrap();

    let mut freq_map: HashMap<&str, u64> = HashMap::new(); 

    for (index, str) in data.split_whitespace().enumerate() {
        let mut e = freq_map.get(str); 

        if e.is_none() {
            freq_map.insert(str, 1); 
        }
        else {
            freq_map.insert(str, e.unwrap()+1);
        }
    }
    println!("Printing words from most frequent to less frequent!"); 

    let mut sorted_pairs: Vec<(&&str, &u64)> = freq_map.iter().collect();

    sorted_pairs.sort_by(|a, b| b.1.cmp(a.1));
    for (k, v) in sorted_pairs {
        println!("{} {}", k, v); 
    }
}