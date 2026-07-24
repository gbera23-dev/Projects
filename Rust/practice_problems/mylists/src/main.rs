
pub mod mylist;
use crate::mylist::linkedlist::Linkedlist;
use crate::mylist::List;

fn main() {
    let mut llst: Linkedlist<i32> = Linkedlist::new();
    llst.add_elem(12);
    llst.add_elem(13); 
    llst.add_elem(14);
    llst.add_elem(15);
    println!("size is {}", llst.get_size());
    for i in 0..4 {
        println!("elem at idx {} is {}", i, llst.get_elem(i).unwrap());
    }

    let _ = llst.remove_elem(3);

    println!("size is {}", llst.get_size());
    for i in 0..3 {
        println!("elem at idx {} is {}", i, llst.get_elem(i).unwrap());
    }

    while llst.get_size() > 0 {
        let _ = llst.remove_last();
        println!("size is {}", llst.get_size());
    }
}
