

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
    fn get_instance() -> Self {
        StupidCalculator 
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
    fn get_instance() -> Self {
        NormalCalculator 
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
    
    fn get_instance() -> Self {
        SmartCalculator 
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
    fn get_instance() -> Self; 
}

pub fn get_calculator<T: Computable> (dumbness: Dumbness) -> T {
    match dumbness {
        Dumbness::Stupid => T::get_instance(), 
        Dumbness::Normal => T::get_instance(),
        Dumbness::Smart =>  T::get_instance()
    }
}