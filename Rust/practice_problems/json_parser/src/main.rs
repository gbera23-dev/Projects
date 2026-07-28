
mod json_parser; 

use std::io; 
use std::io::Write;
use crate::json_parser::{JsonMap, SpecialCommand};

fn main() {
    let mut file_name = String::new(); 
    print!("Please, input file name: "); 
    let _ = io::stdout().flush();
    let _ = io::stdin().read_line(&mut file_name);

    let res = std::fs::read_to_string(file_name.trim());
    let map: JsonMap;

    if res.is_err() {return}

    map = json_parser::parse_json(res.unwrap()).unwrap();

    println!("Json parsing went well!..\nparser is happy :)\nparser's comment: oooohhhhh! I love json,  yum yum json, I want to eat json...");
    
    let mut curr_dir = String::from("root"); 
    
    listen_for_user_reqs(&map, &mut curr_dir); 
}


fn listen_for_user_reqs(map: &JsonMap, curr_dir: &mut String) {
    
    loop {
        let mut inp = String::new(); 
        print!("Please, input the query(just press enter to exit the program): "); 
        let _ = io::stdout().flush();

        let _ = std::io::stdin().read_line(&mut inp);

        if inp.trim().is_empty() {
            println!("Good bye! :)");
            break; 
        } 
        
        if SpecialCommand::is_special_command(inp.trim()) {
            let special_command = SpecialCommand::get_special_command(inp.trim());
            special_command.unwrap().execute_command(curr_dir, map);
            continue;
        }

        let query = format!("{}{}{}", curr_dir, "/", inp);
        
        print!("query is {}", query);
        
        let res = map.send_query
        (query.trim());

        if res.is_some() {
            map.print_and_update_data(res, curr_dir, query);
        }
    }

}