
mod json_parser; 

fn main() {
    let _ = json_parser::parse_json(
        String::from("
        {
        \"hello1\" : \"hello2\", 
        \"jonni\" : {\"a\" : \"b\"} 
        }
        ".trim()));
}
