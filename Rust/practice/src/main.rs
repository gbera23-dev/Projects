
use std::fmt;
use std::fmt::*;


struct Satellite {
    name: String, 
    velocity: f64 // miles per second 
}

impl fmt::Display for Satellite {
    fn fmt(&self, f: &mut Formatter<'_>) -> fmt::Result {
        write!(f, "Satellite name is {} and its velocity is 
    {} miles per second", self.name, self.velocity)
    }
}




fn main() {
    let hubble = Satellite {
        name: String::from("Hubble Telescope"), 
        velocity: 4.72 
    }; 
    println!("hubble is {}", hubble); 
}