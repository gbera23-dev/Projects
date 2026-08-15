

#[derive(Debug)]
pub struct Panel {
    inner_rep: Vec<u8>, 
    width: usize, 
    height: usize, 
}


impl Panel {

    pub fn draw_point(&mut self, x: usize, y: usize) {
        let s = &mut self.inner_rep;
        s[x+y+self.width*y]=b'*';
    }

    pub fn point_is_occupied(&self, x: usize, y: usize) -> bool {
        let s = &self.inner_rep;
        return s[x+y+self.width*y]!=b'.'; 
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


}

pub fn get_new_panel(w: usize, h: usize) -> Box<Panel> {
    let mut v = Vec::new();
    for _ in 0..h {
        for _ in 0..w {
            v.push(b'.');
        }v.push(b'\n');
    }
    let panel = Box::new(Panel{inner_rep:v, width:w, height:h});
    return panel; 
} 