

mod console_gui; 
/*
GUI will have util method to return a new panel, panel
is as a struct essentially, which holds ownership of 
raw string object which will be represented as panel 
in console. 

we shall first see how to create such string, and then
slowly move forward.
*/


//let's experiment with dimensions and see how we
//can build this string. 
//basically, idea is to use dots as place holders and
//separate these dots through blanks. 
//and at the end of the dot, put endl symbol. 
//this is good. 
//now we can start building GUI module. 

fn main() {
    let mut panel = console_gui::get_new_panel(10, 10);
    panel.display();
    panel.draw_point(2, 2); 
   
}