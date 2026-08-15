
use crate::dumb_calculator_static::Computable;

mod dumb_calculator_static;


fn main() {
    println!("Welcome to the calculator!"); 
    let calculator = dumb_calculator_static::
    get_calculator::<dumb_calculator_static::SmartCalculator>
    (dumb_calculator_static::Dumbness::Smart);

    loop {
        
        println!("input first number(input -1 to stop): "); 
        let mut inp = String::new(); 
        let _ = std::io::stdin().read_line(&mut inp);
        if inp.trim() == "-1"{break} 
        let a = str::parse::<f64>(&inp.trim()).unwrap();

        println!("input second number: "); 
        let mut inp = String::new(); 
        let _ = std::io::stdin().read_line(&mut inp);
        let b = str::parse::<f64>(&inp.trim()).unwrap();
        
        println!("addition result is: {}", calculator.add(a, b)); 
        println!("subtraction result is: {}", calculator.subtract(a, b)); 
        println!("multiplication result is: {}", calculator.multiply(a, b)); 
        println!("division result is: {}", calculator.divide(a, b)); 
    }
}
