
/*
 * File: Breakout.java
 * -------------------
 * Name: GIGA BERADZE
 * Section Leader: KONSTANTINE ENDELADZE
 * 
 * This file will eventually implement the game of Breakout.
 */
import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH = (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;

	// INSTANCE VARIABLES
	private GRect brick;
	private GRect paddle;
	private GOval ball;
	private RandomGenerator ranN = RandomGenerator.getInstance();
	double Vy = 3;
	double Vx = 0;
	int TOTAL_BRICKS = NBRICKS_PER_ROW * NBRICK_ROWS;
	int i = 0;
	int p = 0;

	// INITIALIZATION
	public void init() {
		addMouseListeners();
		bricks();
		initPaddle();
		customizeP();
		initball();
		customizeB();
	}

	// RUN METHOD
	public void run() {
		// GAMEPLAY(MAIN GAME LOOP)
		while (p < NTURNS) {
			ballMotion();
			p++;
			if (i == TOTAL_BRICKS) {
				p = 1;
				break;
			}
			if (p < 3) {
				println(NTURNS - p + " lives left!");
			}
		}
		if (p == NTURNS) {
			gameOver();
			println("YOU LOSE! :)");
		} else {
			youWin();
			println("YOU WIN");
		}
	}

	// All methods of bricks
	// This method draws all the bricks
	private void bricks() {
		int z = 0;
		while (z < NBRICK_ROWS) {
			if (z < 2) {
				drawRow(z, Color.RED);
			} else if (z > 1 && z < 4) {
				drawRow(z, Color.ORANGE);
			} else if (z > 3 && z < 6) {
				drawRow(z, Color.YELLOW);
			} else if (z > 5 && z < 8) {
				drawRow(z, Color.GREEN);
			} else {
				drawRow(z, Color.CYAN);
			}
			z++;
		}
	}

	// This method allows us to draw bricks
	private void drawBrick(int x, int y, Color Colour) {
		brick = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
		brick.setColor(Colour);
		brick.setFillColor(Colour);
		brick.setFilled(true);
		add(brick);
	}

	// This method allows us to draw rows
	private void drawRow(int j, Color RowColour) {
		for (int i = 0; i < NBRICKS_PER_ROW; i++) {
			drawBrick(BRICK_SEP / 2 + (i * (BRICK_SEP) + (i * (BRICK_WIDTH))),
					BRICK_Y_OFFSET + (BRICK_HEIGHT + BRICK_SEP) * (j - 1), RowColour);
		}
	}

	// All methods of paddle
	// This method initiates paddle
	private void initPaddle() {
		paddle = new GRect(getWidth() / 2 - PADDLE_WIDTH / 2, getHeight() - PADDLE_Y_OFFSET, PADDLE_WIDTH,
				PADDLE_HEIGHT);
		add(paddle);
	}

	// This method customizes paddle
	private void customizeP() {
		paddle.setFillColor(Color.BLACK);
		paddle.setFilled(true);
	}

	// All methods of ball
	// This method initiates ball
	private void initball() {
		ball = new GOval(getWidth() / 2 - BALL_RADIUS, getHeight() / 2 - BALL_RADIUS, 2 * BALL_RADIUS, 2 * BALL_RADIUS);
		add(ball);
	}

	// This method customizes ball
	private void customizeB() {
		ball.setFillColor(Color.BLACK);
		ball.setFilled(true);
	}

	// BOOLEAN METHODS
	// This method returns the boolean, which checks the wall contact
	private boolean statement1() {
		if (ball.getX() <= 0 || ball.getX() + 2 * BALL_RADIUS >= getWidth()) {
			return true;
		} else {
			return false;
		}
	}

	// MOTION METHODS
	// This method allows us to control the paddle
	public void mouseMoved(MouseEvent event) {
		if (event.getX() > getWidth() - PADDLE_WIDTH / 2 || event.getX() < PADDLE_WIDTH / 2) {
			if (event.getX() < PADDLE_WIDTH / 2) {
				paddle.setLocation(0, getHeight() - PADDLE_Y_OFFSET);
			} else {
				paddle.setLocation(getWidth() - PADDLE_WIDTH, getHeight() - PADDLE_Y_OFFSET);
			}
		} else {
			paddle.setLocation(event.getX() - PADDLE_WIDTH / 2, getHeight() - PADDLE_Y_OFFSET);
		}
	}

	// With this method, ball is able to move
	private void ballMotion() {
		int centerX = getWidth() / 2 - BALL_RADIUS;
		int centerY = getHeight() / 2 - BALL_RADIUS;
		int t = ranN.nextInt(2);
		if (t == 1) {
			Vx = -Vx;
		}
		while (i < TOTAL_BRICKS) {
			pause(8);
			ball.move(Vx, Vy);
			// Wall bounces
			wallBounce();
			// Losing a life
			if (ball.getY() + 2 * BALL_RADIUS >= getHeight()) {
				ball.setLocation(centerX, centerY);
				break;
			}
			// Destroying bricks
			destroyBricks();
		}
		Vy = 3;
		Vx = ranN.nextDouble(1, 3);
	}

	// With this, ball can bounce off of walls
	private void wallBounce() {
		if (statement1()) {
			if (ball.getX() > getWidth() / 2) {
				ball.setLocation(ball.getX() - 3, ball.getY());
			} else {
				ball.setLocation(ball.getX() + 3, ball.getY());
			}
			Vx = -Vx;
		}
		if (ball.getY() <= 0) {
			Vy = -Vy;
		}
	}

	// DESTROYING BRICKS
	// This method destroys the collided break
	private void destroyBricks() {
		if (getCollidingobject() != null) {
			if (getCollidingobject() == paddle) {
				ball.setLocation(ball.getX(), ball.getY() - paddle.getHeight());
				Vy = -Vy;
				if (i == 0) {
					Vx = ranN.nextDouble(1, 3);
				}
			} else {
				remove(getCollidingobject());
				i++;
				Vy = -Vy;
			}
		}
	}

	// This method returns collided brick
	private GObject getCollidingobject() {
		GObject p1 = getElementAt(ball.getX(), ball.getY());
		GObject p2 = getElementAt(ball.getX(), ball.getY() + 2 * BALL_RADIUS);
		GObject p3 = getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS);
		GObject p4 = getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY());
		if (p1 == null == false) {
			return p1;
		}
		if (p2 == null == false) {
			return p2;
		}
		if (p3 == null == false) {
			return p3;
		}
		if (p4 == null == false) {
			return p4;
		} else {
			return null;
		}
	}

	// ENDGAME METHODS
	// This method ends the game, if all lives are lost
	private void gameOver() {
		GLabel label = new GLabel("GAME OVER!", 1, 1);
		label.setColor(Color.RED);
		label.setFont(new Font("Arial", Font.PLAIN, 50));
		label.setLocation(getWidth() / 2 - label.getWidth() / 2, getHeight() / 2 - label.getHeight() / 2);
		repaint();
		add(label);
	}

	// This method ends the game, if all bricks are destroyed
	private void youWin() {
		GLabel label = new GLabel("YOU WIN!", 1, 1);
		label.setColor(Color.GREEN);
		label.setFont(new Font("Arial", Font.PLAIN, 50));
		label.setLocation(getWidth() / 2 - label.getWidth() / 2, getHeight() / 2 - label.getHeight() / 2);
		repaint();
		add(label);
	}
}
