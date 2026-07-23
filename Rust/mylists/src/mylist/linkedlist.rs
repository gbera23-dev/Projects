
use std::boxed::Box; 
use crate::mylist::List;

/*wrapping box struct into Option allows us to simulate node's
next element being nullptr*/

struct Node<T> {
    val: T, 
    next: Option<Box<Node<T>>> 
}


pub struct Linkedlist<T> {
    head: Node<T>, 
    len: usize 
}


impl<T: Default + Copy> Linkedlist<T> {

    pub fn new() -> Linkedlist<T> {


        Linkedlist {
            head: Node {
                val: T::default(), 
                next: Option::None
            }, 
            len: 0
        }
    }

}

impl<T> List<T> for Linkedlist<T> where T: Default + Copy {

    fn add_elem(&mut self, val: T) {
        let new_node = Box::new(Node{val:val, next:Option::None});
        
        let mut tail = &mut self.head;

        while !tail.next.is_none() {
            tail = tail.next.as_deref_mut().unwrap();
        }

        tail.next = Option::Some(new_node);
        
        self.len = self.len + 1; 
    }

    fn remove_elem(&mut self, idx: i32) -> Result<T, String> {

        //error handling
        if idx < 0 || idx >= self.len.try_into().unwrap() {
            return Result::Err(String::from("Index out of bounds!")); 
        }

        let mut curr_node = &mut self.head;

        let mut tmp_idx = idx; 

        while tmp_idx > 0 {
            curr_node = curr_node.next.as_mut().unwrap();
            tmp_idx = tmp_idx - 1; 
        }

        let val_of_deleted_node = curr_node.next.as_deref().unwrap().val;

        let elem = curr_node.next.take().unwrap().next.take();

        curr_node.next = elem; 

        self.len = self.len - 1; 
        Result::Ok(val_of_deleted_node)
    }

    fn remove_first(&mut self) -> Result<T, String> {
        self.remove_elem(0)
    }

    fn remove_last(&mut self) -> Result<T, String> {
        self.remove_elem((self.len as i32)-1)
    }

    fn get_elem(&self, idx: i32) -> Result<T, String> {

        //error handling
        if idx < 0 || idx >= self.len.try_into().unwrap() {
            return Result::Err(String::from("Index out of bounds!")); 
        }

        let mut curr_node = &self.head;
        let mut tmp_idx = idx; 

        while tmp_idx >= 0 {
            curr_node = curr_node.next.as_deref().unwrap();
            tmp_idx = tmp_idx - 1; 
        }
        Result::Ok(curr_node.val)
    }

    fn get_size(&self) -> usize {
        self.len
    }

}