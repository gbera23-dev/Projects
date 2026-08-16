
use std::collections::HashSet;
use crate::console_gui;
use crate::console_gui::PanelObject;
use crate::console_gui::generate_ref;

pub struct Rect {
    coords: Vec<(usize, usize)>, 
    obj_ref: Option<String>
}

impl Rect { 
    pub fn get_new_rect(x: usize, y: usize) -> Rect {
        Rect{ coords:vec![(x, y), (x+1, y), (x, y+1), (x+1, y+1)],
            obj_ref: Option::Some(generate_ref())}
    }
}

impl PanelObject for Rect {
    fn set_coords(&mut self, v: Vec<(usize, usize)>) {
        self.coords=v;
    }

    fn get_coords(&mut self) -> &mut Vec<(usize, usize)> {
        &mut self.coords
    }

    fn set_reference(&mut self) {
        self.obj_ref = Option::Some(generate_ref());
    }

    fn get_reference(&self) -> Option<&String> {
        self.obj_ref.as_ref()
    } 
}