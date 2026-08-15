
use console_gui::Direction;
use console_gui::Point;
use console_gui::Panel; 
use std::thread::sleep;
use std::time::Duration; 
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

fn run_cool_simulation(panel: &mut Panel) {
    panel.display();
    //let's get a point in each horizon.
    let mut points: Vec<Point> = Vec::new();  

    let mut w = 0; 

    for i in 0..panel.get_height() {
        if w>=panel.get_width() {
            w=0;
        }
        points.push(Point::get_new_point(w, i));
        w=w+1; 
    } 

    for  p in points.iter_mut() {
        panel.add_point(p);
    }

    panel.display(); 
    loop {
        for p in points.iter_mut() {
            panel.move_point(p, Direction::RIGHT);
        }

        panel.display(); 
        sleep(Duration::from_millis(30));
    }
}

fn main() {
    let mut panel = console_gui::get_new_panel(20, 20);
    run_cool_simulation(&mut panel);
}