
use std::env; 
use std::fs; 

fn main() {
    let arg_len = std::env::args().len() - 1;
    if arg_len != 2 {
        println!("Please, provide two arguments!.."); 
        std::process::exit(1);
    }
    let file_name = std::env::args().nth(1).unwrap();
    let to_search = std::env::args().nth(2).unwrap(); 

    let read_s = std::fs::read_to_string(file_name).unwrap();

    for word in read_s.trim().split_whitespace() {
        if word == to_search { 
            println!("Holy shit! We got it!.. We got it!!!"); 
            return; 
        }
    }

    println!("Word could not be found!.."); 
}