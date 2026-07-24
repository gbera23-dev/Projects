

pub struct StupidCalculator;
pub struct NormalCalculator;
pub struct SmartCalculator; 

impl Computable for StupidCalculator {
    fn add(&self, a: f64, b: f64) -> f64 {
        a+b+200.0
    }
    fn subtract(&self, a: f64, b: f64) -> f64 {
        a-b-200.0
    } 
    fn multiply(&self, a: f64, b: f64) -> f64 {
        a*b/200.0
    }  
    fn divide(&self, a: f64, b: f64) -> f64 {
        a/b*200.0
    } 
}

impl Computable for NormalCalculator {
    fn add(&self, a: f64, b: f64) -> f64 {
        a+b+0.1
    }
    fn subtract(&self, a: f64, b: f64) -> f64 {
        a-b+0.1
    } 
    fn multiply(&self, a: f64, b: f64) -> f64 {
        a*b+0.1
    }  
    fn divide(&self, a: f64, b: f64) -> f64 {
        a/b+0.1
    }
}

impl Computable for SmartCalculator {
    fn add(&self, a: f64, b: f64) -> f64 {
        a+b
    }
    fn subtract(&self, a: f64, b: f64) -> f64 {
        a-b
    } 
    fn multiply(&self, a: f64, b: f64) -> f64 {
        a*b
    }  
    fn divide(&self, a: f64, b: f64) -> f64 {
        a/b
    } 
}

pub enum Dumbness { 
    Stupid, 
    Normal, 
    Smart 
}

pub trait Computable {
    fn add(&self, a: f64, b: f64) -> f64; 
    fn subtract(&self, a: f64, b: f64) -> f64; 
    fn multiply(&self, a: f64, b: f64) -> f64;  
    fn divide(&self, a: f64, b: f64) -> f64;  
}

pub fn get_calculator(dumbness: Dumbness) -> Box<dyn Computable> {
    match dumbness {
        Dumbness::Stupid => Box::new(StupidCalculator), 
        Dumbness::Normal => Box::new(NormalCalculator),
        Dumbness::Smart =>  Box::new(SmartCalculator)
    }
}