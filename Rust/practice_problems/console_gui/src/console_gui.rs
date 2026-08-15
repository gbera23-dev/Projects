
use std::collections::HashSet;
use rand;  


const REF_SIZE: i32 = 120; 
const PLACED_POINT_BYTE_REP: u8 = b'*';
const EMPTY_CELL_BYTE_REP: u8 = b'.'; 

#[derive(Debug)]
pub struct Panel {
    inner_rep: Vec<u8>, 
    width: usize, 
    height: usize, 
    point_refs: HashSet<String>
}

pub struct Point {
    x_coord: usize, 
    y_coord: usize,
    point_ref: Option<String> 
}

pub enum Direction {
    LEFT, RIGHT, UP, DOWN 
}

impl Point {
    pub fn get_new_point(x: usize, y: usize) -> Point {
        Point{x_coord: x, y_coord: y, point_ref: Option::Some(generate_ref())}
    }
}


impl Panel {

    pub fn add_point(&mut self, p: &mut Point) {
        let s = &mut self.inner_rep;
        let x=  p.x_coord; 
        let y=  p.y_coord;

        self.place_symbol((x, y), PLACED_POINT_BYTE_REP);

        //if point reference is not known, set new auto generated one 
        if p.point_ref.is_none() {
            p.point_ref = Option::Some(generate_ref());
        }

        self.point_refs.insert(p.point_ref.as_mut().unwrap().clone());
    }


    pub fn erase_point(&mut self, p: &mut Point) {
        if !self.point_is_occupied(p.x_coord, p.y_coord) {
            println!("This place is not occupied by point!"); 
            return; 
        } 
        self.place_symbol((p.x_coord, p.y_coord), EMPTY_CELL_BYTE_REP);
        self.point_refs.remove(p.point_ref.as_ref().unwrap());
    }

    pub fn move_point(&mut self, p: &mut Point, dir: Direction) {
    
        if !self.point_refs.contains(p.point_ref.as_ref().unwrap()) {
            println!("such point is not added to a panel!");
            return; 
        }
        println!("moving point with ref: {}", p.point_ref.as_ref().unwrap());
        let coords = (p.x_coord, p.y_coord);
        
        self.place_symbol((p.x_coord, p.y_coord), EMPTY_CELL_BYTE_REP);

        let mut new_coords: (usize, usize) = (coords.0, coords.1);
        match dir {
            Direction::LEFT => {
                new_coords.0= if new_coords.0==0 {self.width-1} else {new_coords.0-1}
            }, 
            Direction::RIGHT => {
                new_coords.0= if new_coords.0==self.width-1 {0} else {new_coords.0+1}
            }, 
            Direction::DOWN => {
                new_coords.1= if new_coords.1==self.height-1 {0} else {new_coords.1+1}
            }, 
            Direction::UP => {
                new_coords.1= if new_coords.1==0 {self.height-1} else {new_coords.1-1}
            }
        }
        p.x_coord=new_coords.0; 
        p.y_coord=new_coords.1;
        self.place_symbol(new_coords, PLACED_POINT_BYTE_REP);
    }

    pub fn point_is_occupied(&mut self, x: usize, y: usize) -> bool {
        let s = &self.inner_rep;
        return self.get_symbol((x, y))==EMPTY_CELL_BYTE_REP; 
    }

    pub fn get_width(&self) -> usize {
        return self.width; 
    }

    pub fn get_height(&self) -> usize {
        return self.height; 
    }

    pub fn display(&self) { 
        for ch in &self.inner_rep {
            print!("{}", *ch as char);
            if *ch!=b'\n' {
                print!(" ");
            }
        }
        for _ in 0..self.width {
            print!("- ");
        }println!();
    }

    fn place_symbol(&mut self, coords: (usize, usize), symbol: u8) {
        let s = &mut self.inner_rep;
        s[coords.0+coords.1+self.width*coords.1]=symbol;
    }

    fn get_symbol(&self, coords: (usize, usize)) -> u8 {
        let s = &self.inner_rep;
        s[coords.0+coords.1+self.width*coords.1]
    }

}

pub fn get_new_panel(w: usize, h: usize) -> Box<Panel> {
    let mut v = Vec::new();
    for _ in 0..h {
        for _ in 0..w {
            v.push(EMPTY_CELL_BYTE_REP);
        }v.push(b'\n');
    }
    let panel = Box::new(Panel{inner_rep:v, width:w, height:h, point_refs:HashSet::new()});
    return panel; 
} 

//reference is 120 length array of digits, there are loads of combinations, 
//so chance of collision is extremely unlikely
fn generate_ref() -> String { 
    let mut generated = String::new();
    for _ in 0..REF_SIZE { 
        generated.push_str(&rand::random_range(0..10).to_string());
    }
    return generated;
}