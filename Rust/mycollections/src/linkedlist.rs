
use std::boxed::Box; 

/*wrapping box struct into Option allows us to simulate node's
next element being nullptr*/

struct Node {
    val: i32, 
    next: Option<Box<Node>> 
}


pub struct Linkedlist {
    head: Node, 
    len: usize 
}


impl Linkedlist {

    pub fn add_elem(&mut self, val: i32) {
        let new_node = Box::new(Node{val:val, next:Option::None});
        
        let mut tail = &mut self.head;

        while !tail.next.is_none() {
            tail = tail.next.as_deref_mut().unwrap();
        }

        tail.next = Option::Some(new_node);
        
        self.len = self.len + 1; 
    }

    pub fn remove_elem(&mut self, idx: i32) {

        let mut curr_node = &mut self.head;

        let mut tmp_idx = idx; 

        // we need to stand on the previous element of the node to be deleted 
        while tmp_idx > 0 {
            curr_node = curr_node.next.as_mut().unwrap();
            
            tmp_idx = tmp_idx - 1; 
        }

        let elem = curr_node.next.take().unwrap().next.take();

        curr_node.next = elem; 

        self.len = self.len - 1; 
    }

    pub fn get_elem(&self, idx: i32) -> i32 {
        let mut curr_node = &self.head;
        let mut tmp_idx = idx; 

        while tmp_idx >= 0 {
            curr_node = curr_node.next.as_deref().unwrap();
            tmp_idx = tmp_idx - 1; 
        }
        curr_node.val
    }

    pub fn get_size(&self) -> usize {
        self.len
    }

    pub fn new() -> Linkedlist {
        Linkedlist {
            head: Node {
                val: -1, 
                next: Option::None
            }, 
            len: 0
        }
    }

}