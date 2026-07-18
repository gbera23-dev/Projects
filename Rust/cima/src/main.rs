
use std::fs; 
use std::collections::HashMap;
use std::vec;
use std::process::exit; 

const TAPE_SIZE: usize = 100; 
const MORE_THAN_INPUT_LEN_DISPLAY_SIZE: usize = 5;  


fn populate_hashmap(map: &mut HashMap<i32, ((u8, i32),(u8, i32))>, index: usize, token: &str) {

    let mut it = token.split("|").enumerate();

    let v1 = it.nth(0).unwrap().1;
    let s1 = it.nth(0).unwrap().1;
    let v2 = it.nth(0).unwrap().1;
    let s2 = it.nth(0).unwrap().1;

    map.insert(index.try_into().unwrap(), 
    ((v1.parse::<u8>().unwrap(), s1.parse::<i32>().unwrap()), 
    (v2.parse::<u8>().unwrap(), s2.parse::<i32>().unwrap())));

}

fn build_map(contents: &str) -> HashMap<i32, ((u8, i32), (u8, i32))> {

    let mut map: HashMap<i32, ((u8, i32), (u8, i32))> = HashMap::new(); 
    
    for (index, token) in contents.split("=").enumerate() {
        if index == 0 {continue;}
        populate_hashmap(&mut map, index, token); 
    }

    map
}

fn alter_tape(tape: &mut Vec<u8>, curr_state: &mut i32, 
    curr_idx: &mut usize, curr_state_info: &((u8, i32), (u8, i32))) {

        let curr_tape_val = tape.get(*curr_idx).unwrap(); 

        let next = if *curr_tape_val == 0 {curr_state_info.0} 
        else {curr_state_info.1};

        *curr_state = next.1;
        let wlr = next.0; 
        if wlr == 0 {
            tape[*curr_idx] = 0; 
            *curr_idx -= 1; 
        }

        else if wlr == 1 {
            tape[*curr_idx] = 0; 
            *curr_idx += 1; 
        }
        else if wlr == 10 {
            tape[*curr_idx] = 1; 
            *curr_idx -= 1; 
        }

        else if wlr == 11 {
            tape[*curr_idx] = 1; 
            *curr_idx += 1; 
        }
}


fn run_simulation(map: HashMap<i32, ((u8, i32), (u8, i32))>, input: &str) {
    let mut curr_state: i32 = 1; //1 is a starting state for a turing machine 
    let mut curr_idx: usize = 0; 

    let mut tape: Vec<u8> = vec![0; TAPE_SIZE];

    for (index, ch) in input.bytes().enumerate() {
        tape[index] = ch-48;
    }

    loop {
        let curr_state_info: &((u8, i32), (u8, i32)) = 
        map.get(&curr_state.try_into().unwrap()).unwrap();
        alter_tape(&mut tape, &mut curr_state, &mut curr_idx, curr_state_info);
         println!("curr_state: {}, tape_index: {}, tape: {:?}", curr_state, curr_idx, 
        &tape[0..input.len() + MORE_THAN_INPUT_LEN_DISPLAY_SIZE]);
        
        if curr_state==0 || curr_state==-1 {
            println!("Turing machine halted in {} state", if curr_state==0 {"reject"} 
            else {"accept"});
            break; 
        }
    }

}

fn main() {
    if std::env::args().len()!=3 { 
        println!("Two argument must be provided!"); 
        exit(-1);
    }

    let file_name: String = std::env::args().nth(1).unwrap(); 

    if !file_name.ends_with(".tm") {
        println!("File name is not valid!..");
        exit(-1); 
    }

    let contents: String = fs::read_to_string(file_name).unwrap();

    let trimmed_contents = contents.trim(); 

    let map: HashMap<i32, ((u8, i32), (u8, i32))> = build_map(trimmed_contents);

    let input = std::env::args().nth(2).unwrap();

    run_simulation(map, &input); 
}