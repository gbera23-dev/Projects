
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
    coords: Vec<(usize, usize)>,
    point_ref: Option<String> 
}

impl PanelObject for Point {

    fn set_coords(&mut self, v: Vec<(usize, usize)>) {
        self.coords=v;
    }

    fn get_coords(&mut self) -> &mut Vec<(usize, usize)> {
        &mut self.coords
    }

    fn set_reference(&mut self) {
        self.point_ref = Option::Some(generate_ref());
    }

    fn get_reference(&self) -> Option<&String> {
        self.point_ref.as_ref()
    }
}

pub trait PanelObject {
    fn set_coords(&mut self, v: Vec<(usize, usize)>);
    fn get_coords(&mut self) -> &mut Vec<(usize, usize)>;
    fn set_reference(&mut self);
    fn get_reference(&self) -> Option<&String>; 
}

pub enum Direction {
    LEFT, RIGHT, UP, DOWN 
}

impl Point {
    pub fn get_new_point(x: usize, y: usize) -> Point {
        Point{ coords:vec![(x, y)],point_ref: Option::Some(generate_ref())}
    }
}


impl Panel {

    pub fn add(&mut self, p: &mut dyn PanelObject) {
        let s = &mut self.inner_rep;


        for (x, y) in p.get_coords().iter() { 
            self.place_symbol((*x, *y), PLACED_POINT_BYTE_REP);
        }

        let refer = p.get_reference(); 
        //if point reference is not known, set new auto generated one 
        if refer.is_none() {
            p.set_reference();
        }

        self.point_refs.insert(p.get_reference().as_mut().unwrap().clone());
    }


    pub fn erase_point(&mut self, p: &mut dyn PanelObject) {

        for (x, y) in p.get_coords().iter() { 
            self.place_symbol((*x, *y), EMPTY_CELL_BYTE_REP);
        }

        self.point_refs.remove(p.get_reference().unwrap());
    }

    pub fn move_obj(&mut self, p: &mut dyn PanelObject, dir: Direction) {
    
        if !self.point_refs.contains(p.get_reference().unwrap()) {
            println!("such point is not added to a panel!");
            return; 
        }

        println!("moving point with ref: {}", p.get_reference().unwrap());


        let v = p.get_coords();

        let new_v = move_points(self.width, self.height, 
            v, dir);

        for i in 0..v.len() {
            let older = v[i]; 
            self.place_symbol(older, EMPTY_CELL_BYTE_REP);
        }
        for i in 0..new_v.len() {
            let new = new_v[i];
            self.place_symbol(new, PLACED_POINT_BYTE_REP);
        }
        p.set_coords(new_v);
    }

    fn place_is_occupied(&mut self, x: usize, y: usize) -> bool {
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
pub fn generate_ref() -> String { 
    let mut generated = String::new();
    for _ in 0..REF_SIZE { 
        generated.push_str(&rand::random_range(0..10).to_string());
    }
    return generated;
}


fn move_points(width: usize, height: usize, 
    v: &mut Vec<(usize, usize)>, dir: Direction) -> Vec<(usize ,usize)> {
    let mut new_v: Vec<(usize, usize)> = Vec::new();

    for i in 0..v.len() {

        let mut new_coords: (usize, usize) = v[i];
        match dir {
                Direction::LEFT => {
                    new_coords.0= if new_coords.0==0 {width-1} else {new_coords.0-1}
                }, 
                Direction::RIGHT => {
                    new_coords.0= if new_coords.0==width-1 {0} else {new_coords.0+1}
                }, 
                Direction::DOWN => {
                    new_coords.1= if new_coords.1==height-1 {0} else {new_coords.1+1}
                }, 
                Direction::UP => {
                    new_coords.1= if new_coords.1==0 {height-1} else {new_coords.1-1}
                }
            }
            new_v.push(new_coords);
    }
    new_v
} 