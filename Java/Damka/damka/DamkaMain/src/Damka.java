import java.awt.Color;
import java.awt.event.MouseEvent;

import acm.graphics.GCanvas;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import acm.program.Program;

public class Damka extends Program{
	private DamkaGraphics graphics;
	public void run() {
		graphics = new DamkaGraphics();
		setSize(1000, 1000);
		add(graphics, 0, 0);
		graphics.initDisplay();
	}
}
