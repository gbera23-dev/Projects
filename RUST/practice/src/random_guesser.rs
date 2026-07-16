
use rand::random;
use std::io;


fn main() {
    let i: u16 = (random::<u16>() % 100) + 1; 
    loop {
        let res = play_a_round(i);
        println!("{}", res); 
        if res == "correct" {
            break; 
        }
    }
}


fn play_a_round(i: u16) -> String {
    println!("Please, input your guess: ");

    let mut s = String::new();

    let _i = io::stdin().read_line(&mut s);

    let guess : u16 = s.trim().parse::<u16>().unwrap();

    return if guess < i {String::from("too low")} 
    else if guess > i {String::from("too high")}
    else {String::from("correct")}; 
}


