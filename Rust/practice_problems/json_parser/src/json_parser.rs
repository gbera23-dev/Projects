
use std::collections::HashMap;
use std::io; 
use std::io::Write;

const STR_SPLIT: &str = "\"";
const START_JSON: &str = "{";
const END_JSON: &str = "}"; 
const START_JSON_LIST: &str = "["; 
const END_JSON_LIST: &str = "]";
const KEY_VAL_SEPARATOR: &str = ":";
const SPLIT_QUERY_SYMBOL: &str = "/";
const LIST_ELEMS_SEPARATOR: &str = ","; 
const SPECIAL_COMMAND_LS: &str = "ls";
const SPECIAL_COMMAND_CD: &str = "cd"; 
const SPECIAL_COMMAND_QU: &str = "qu";
 
 #[derive(Debug)]
pub enum JsonVal {
    StringVal(Option<String>), 
    MapVal(Option<HashMap<String, JsonVal>>),
    ListVal(Option<Vec<JsonVal>>)
}

pub struct JsonMap {
    internal_map: HashMap<String, JsonVal>
}

 #[derive(Debug)]
pub enum SpecialCommand {
    Cd(String),
    Ls,
    Query
}

impl SpecialCommand {

    pub fn execute_command(self, curr_dir_path: &mut String, map: &JsonMap) -> () {
        match self {
            SpecialCommand::Cd(val) => {curr_dir_path.clear(); curr_dir_path.push_str(&val)}, 
            SpecialCommand::Ls => {println!("Your current directory: {}", curr_dir_path)},
            SpecialCommand::Query => {let res = map.send_query(curr_dir_path); 
                map.print_and_update_data(res, curr_dir_path, curr_dir_path.clone());}
        }
    }

    pub fn is_special_command(command: &str) -> bool {
        println!("=={}==", command);
        let lower_command = command.to_lowercase();
        lower_command.starts_with("ls") || lower_command.starts_with("cd") 
        || lower_command.starts_with("qu")
    }

    pub fn get_special_command(str: &str) -> Option<SpecialCommand> {
        let str = str.to_lowercase();
        match &str[0..2] {
            SPECIAL_COMMAND_CD => {Option::Some(SpecialCommand::Cd(String::from(&str[3..str.len()])))}, 
            SPECIAL_COMMAND_LS => {Option::Some(SpecialCommand::Ls)},
            SPECIAL_COMMAND_QU => {Option::Some(SpecialCommand::Query)},
            &_ => {Option::None}
        }
    }

}


impl JsonMap {
    
    pub fn send_query(&self, query: &str) -> Option<Vec<String>> {
        
        let m = &self.internal_map; 

        let mut curr_val = Option::<&JsonVal>::None; 

        for str in query.split(SPLIT_QUERY_SYMBOL) {
            if str.trim().is_empty(){continue}
            
            if curr_val.is_none() {
                curr_val = m.get(str); 
                continue; 
            }
            if curr_val.unwrap().is_map_val() {
                curr_val = curr_val.unwrap().extract_map_val().as_ref().unwrap().get(str);
            }
        }
        
        if curr_val.is_none() {return Option::None};

        let curr = curr_val.unwrap();
        if curr.is_string_val() {
            return Option::Some(vec![curr.extract_string_val().clone().unwrap()]);
        } 
        else if curr.is_map_val() {
            return self.handle_map_res(curr);
        }
        else {
            return self.handle_list_res(curr);
        }
    }

    pub fn print_and_update_data(&self, res: Option<Vec<String>>, curr_dir: &mut String, query: String) {
        let len: usize = res.as_ref().unwrap().len();
            if len == 1 {
            print!("In summary, single value was retrieved\nvalue: {:?}\n",
            res.as_ref().unwrap());
            }
            else {
            print!("In summary, {} values were retrieved\nvalues: {:?}\n", len,
            res.as_ref().unwrap());
            }
            let _ = io::stdout().flush();

            curr_dir.clear();
            curr_dir.push_str(&query.replace("\n", ""));
    }

    fn handle_list_res(&self, curr: &JsonVal) -> Option<Vec<String>> {
        let mut sol = Vec::new(); 

        let curr_list = curr.extract_list_val().as_ref().unwrap();

        for key in curr_list {
            sol.push(key.to_str());
        }
        Option::Some(sol)
    }

    fn handle_map_res(&self, curr: &JsonVal) -> Option<Vec<String>>{
        let mut sol = Vec::new(); 

        let curr_map = curr.extract_map_val().as_ref().unwrap();

        for key in curr_map.keys() {
            sol.push(format!("{}{}", key.clone(), "..."));
        }
        Option::Some(sol)
    }
}


impl JsonVal {
    pub fn extract_string_val(&self) -> &Option<String> {
        match self {
            JsonVal::MapVal(_) => {&Option::None}, 
            JsonVal::StringVal(val) => {&val},
            JsonVal::ListVal(_) => {&Option::None}
        }
    }

    pub fn extract_map_val(&self) -> &Option<HashMap<String, JsonVal>> {
        match self {
            JsonVal::MapVal(val) => {&val}, 
            JsonVal::StringVal(_) => {&Option::None},
            JsonVal::ListVal(_) => {&Option::None}
        }
    }

    pub fn extract_list_val(&self) -> &Option<Vec<JsonVal>> {
        match self {
            JsonVal::MapVal(_) => {&Option::None}, 
            JsonVal::StringVal(_) => {&Option::None},
            JsonVal::ListVal(val) => {val}
        }
    }

    pub fn is_string_val(&self) -> bool {
        match self {
            JsonVal::MapVal(_) => {false}, 
            JsonVal::StringVal(_) => {true}
            JsonVal::ListVal(_) => {false}
        }
    }
    
    pub fn is_map_val(&self) -> bool {
        match self {
            JsonVal::MapVal(_) => {true}, 
            JsonVal::StringVal(_) => {false},
            JsonVal::ListVal(_) => {false}
        }
    }

    pub fn is_list_val(&self) -> bool {
        match self {
            JsonVal::MapVal(_) => {false}, 
            JsonVal::StringVal(_) => {false},
            JsonVal::ListVal(_) => {true}
        }
    }

    pub fn to_str(&self) -> String {
        match self {
            JsonVal::MapVal(v) => {format!("{:?}", v.as_ref().unwrap())}, 
            JsonVal::StringVal(v) => {format!("{}", v.as_ref().unwrap())},
            JsonVal::ListVal(v) => {format!("{:?}", v.as_ref().unwrap())}
        }
    }

}

pub fn parse_json(mut json_data:  String) -> Result<JsonMap, String> {

    json_data = clean_json_data(json_data);

    let vec = build_json_token_vec(json_data);

    let mut is_visited = vec![false; vec.len()];

    let map = build_json_map(&vec, &mut is_visited);
    
    let mut root_map = HashMap::new(); 
    root_map.insert(String::from("root"), JsonVal::MapVal(Option::Some(map)));
    Result::Ok(JsonMap{internal_map: root_map})
}

fn build_json_map(vec: &Vec<String>,  is_visited: &mut Vec<bool>) -> HashMap<String, JsonVal> {

    let mut map = 
    HashMap::<String, JsonVal>::new();

    for (idx, str) in vec.iter().enumerate() {
        if is_visited[idx] {continue} 

        is_visited[idx] = true;

        if str==START_JSON{continue}
        if str==END_JSON{break}

        if str==KEY_VAL_SEPARATOR {
            let key = vec[idx-1].clone();

            let val = if vec[idx+1] != START_JSON && vec[idx+1] != START_JSON_LIST
             {JsonVal::StringVal(Option::Some(vec[idx+1].clone()))}

            else if vec[idx+1] == START_JSON {
                JsonVal::MapVal(Option::Some(build_json_map(&vec, is_visited)))
            }
            else {
                JsonVal::ListVal(Option::Some(build_json_list(&vec, is_visited)))
            };
            map.insert(key, val); 
        }
    }

    map
}

fn build_json_list(vec: &Vec<String>,  is_visited: &mut Vec<bool>) -> Vec<JsonVal> {
    let mut sol_vec = 
        Vec::<JsonVal>::new(); 

    for (idx, str) in vec.iter().enumerate() {
        if is_visited[idx] {continue} 

        is_visited[idx] = true;

        if str==START_JSON_LIST{continue}
        if str==END_JSON_LIST{break}

        if str==LIST_ELEMS_SEPARATOR {
        let curr_val = if vec[idx+1] != START_JSON && vec[idx+1] != START_JSON_LIST
            {JsonVal::StringVal(Option::Some(vec[idx+1].clone()))}

            else if vec[idx+1] == START_JSON {
                JsonVal::MapVal(Option::Some(build_json_map(&vec, is_visited)))
            }
            else {
                JsonVal::ListVal(Option::Some(build_json_list(&vec, is_visited)))
            };

            sol_vec.push(curr_val);
        }
    }

    sol_vec
}


fn clean_json_data(json_data: String) -> String {
    let mut cleaned_up_data = String::new(); 
    for ch in json_data.bytes().enumerate() {
        if ch.1!=b' ' {

            cleaned_up_data.push(ch.1 as char);

            if ch.1==*END_JSON.as_bytes().get(0).unwrap() || 
            ch.1==*END_JSON_LIST.as_bytes().get(0).unwrap() ||
            ch.1==*KEY_VAL_SEPARATOR.as_bytes().get(0).unwrap() ||
            ch.1==*START_JSON_LIST.as_bytes().get(0).unwrap() || 
            ch.1==*LIST_ELEMS_SEPARATOR.as_bytes().get(0).unwrap() 
            {
                cleaned_up_data.push('\"')
            }

            if ch.1==*START_JSON_LIST.as_bytes().get(0).unwrap() {
                cleaned_up_data.push_str(",\"");
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