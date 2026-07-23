

pub mod linkedlist; 


pub trait List<T> {
    
    fn add_elem(&mut self, val: T);

    fn remove_elem(&mut self, idx: i32) -> Result<T, String>;

    fn remove_first(&mut self) -> Result<T, String>; 

    fn remove_last(&mut self) -> Result<T, String>; 
    
    fn get_elem(&self, idx: i32) -> Result<T, String>;

    fn get_size(&self) -> usize;

}