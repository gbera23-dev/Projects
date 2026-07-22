
pub mod linkedlist;
use crate::linkedlist::Linkedlist;

fn main() {
    let mut llst = Linkedlist::new();
    llst.add_elem(12);
    llst.add_elem(13); 
    llst.add_elem(14);
    llst.add_elem(15);
    println!("size is {}", llst.get_size());
    for i in 0..4 {
        println!("elem at idx {} is {}", i, llst.get_elem(i));
    }

    llst.remove_elem(3);

    println!("size is {}", llst.get_size());
    for i in 0..3 {
        println!("elem at idx {} is {}", i, llst.get_elem(i));
    }
}
