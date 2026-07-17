
/*
 * File: BreakoutEXTENSION.java
 * -------------------
 * Name: GIGA BERADZE
 * Section Leader: KONSTANTINE ENDELADZE
 * 
 * This file will eventually implement the game of Breakout.
 */
/* !!!!!!!!!!!!!!!!! MUST READ!
 * THIS IS THE EXTENSION OF THE GAME BREAKOUT.
 * INSTEAD OF ONE PADDLE, WE HAVE TWO PADDLES, WHICH ARE CONTROLLED BY TWO PLAYERS
 * SETUP, PURPOSE AND RULES OF THE GAME:
 * SETUP: WE HAVE BRICKS AT THE CENTRE OF THE SCREEN,IDENTICAL TWO BALLS AND TWO PADDLES.
 * THERE ARE TWO DIFFERENT WORLDS SEPERATED BY BRICKS.
 * FIRST ONE IS THE WORLD OF PLAYER 1, AND SECOND IS THE WORLD OF PLAYER 2.
 * BALLS ARE REFLETING OFF OF WALLS, ARE DESTROYING THE BRICKS AND ALSO CAN INTERACT WITH 
 * EACH OTHER.
 * PURPOSE: THE PURPOSE OF THE GAME IS FOR A PLAYER TO DESTROY ANOTHER PLAYER. GAME WILL
 * NOT END UNTIL ONE OF THE PLAYER DIES, SO THEY HAVE NO OTHER CHOICE BUT TO END EACH OTHER.
 * RULES: EACH PLAYER HAS THREE LIVES. IF THOSE LIVES ARE ALL LOST, PLAYER LOSES, 
 * PLAYER LOSES IF HE IS THE FIRST ONE TO LOSE ALL LIVES. GAME ENDS EXACTLY, WHEN ONE 
 * OF THE PLAYER LOSES ALL THREE LIVES, AND THE WINNER WILL BE THE ONE, WHICH STAYS ALIVE.
 * GAME COULD BE LOST VIA ERROR OF A PLAYER.
 * IF PLAYERS ARE EQUALLY STRONG AND THEY HAVE managed TO BREAK ALL THE BRICKS,
 * THEN WE WILL REACH THE PHASE OF SHOWDOWN, IN WHICH, PLAYERS WILL NEED TO PLAY WITH SMALLER PADDLES, WITHOUT BRICKS.
 * IN THIS CASE, POINT WILL BE GIVEN TO ONE WHO SURVIVES.
 * PLAYER LOSES THE LIFE, IF ITS OWN BALL GETS INTO HIS TERRITORY. 
 * MISSING OPPONENT'S BALL WILL NOT REDUCE THE QUANTITY OF YOUR LIVES
 * GAME CONTINUES FOR 5 ITERATIONS.
 * FINAL WINNER WILL BE A ONE, WHO WILL REACH 5 POINTS FIRST.
 * IN ORDER TO START THE GAME, BOTH PLAYERS MUST BE READY.
 * AND BEING READY MEANS THAT THEY HAVE TO PRESS KEY ASSOCIATED TO MOVING RIGHT.
 * FOR PLAYER 1, WE HAVE LEFT AND RIGHT ARROWS.
 * FOR PLAYER 2, WE HAVE LETTER A AND LETTER D.
 * THIS GAME WILL ALSO FORCE YOU TO TALK TO SOMEBODY AND GET FRIENDS, IF YOU DO NOT HAVE ONE. 
 * TWO PLAYERS ARE NEEDED FOR THE GAME TO BE PLAYABLE :)
 * GOOD LUCK AND HAVE FUN! 
 * */
import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class Breakoutextension extends GraphicsProgram {
	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 15;

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
	// OBJECTS
	private GRect frame;
	private GRect brick;
	private GRect paddle1;
	private GRect paddle2;
	private GOval ball1;
	private GOval ball2;
	private GLine line1;
	private GLine line2;
	private GLabel score;
	// RNG
	private RandomGenerator ranN = RandomGenerator.getInstance();
	// SPEEDS
	double Vy1 = 2;
	double Vx1 = 1.5;
	double Vy2 = 2;
	double Vx2 = 1.5;
	// INTEGERS
	int TOTAL_BRICKS = NBRICKS_PER_ROW * NBRICK_ROWS;
	int TOTAL_BHEIGHT = NBRICK_ROWS * (BRICK_HEIGHT + BRICK_SEP) - BRICK_SEP;
	int i = 0;
	int c = 0;
	int score1 = 0;
	int score2 = 0;
	// BOOLEANS
	boolean checker1;
	boolean checker2;
	boolean starter1;
	boolean starter2;
	// SOUND EFFECTS
	AudioClip gameOverClip = MediaTools.loadAudioClip("gameOver.wav");

	// RUN METHOD
	public void run() {
		// GAMEPLAY(MAIN GAME LOOP)
		while (score1 != 5 && score2 != 5) {
			INIT();
			starter1 = false;
			starter2 = false;
			while ((starter1 && starter2) == false) {
				if (starter1 == true) {
					player1Ready();
				} else if (starter2 == true) {
					player2Ready();
				}
				pause(10);
			}
			i = 0;
			int p1 = 0;
			int p2 = 0;
			while (p1 < NTURNS && p2 < NTURNS) {
				ballMotion();
				if (c == 1) {
					p1++;
					GObject heart1 = getElementAt(WIDTH - 30 - 15 * (p1 - 1) + 1, frame.getHeight() - 15 + 1);
					remove(heart1);
				}
				if (c == -1) {
					p2++;
					GObject heart2 = getElementAt(WIDTH - 30 - 15 * (p2 - 1), frame.getY() + 5);
					remove(heart2);
				}
				if (p1 == NTURNS) {
					gameOverClip.play();
					score2++;
				} else if (p2 == NTURNS) {
					gameOverClip.play();
					score1++;
				}
			}
			if (score1 > score2) {
				P1Win();
			} else {
				P2Win();
			}
		}
	}

	// This method initializes the game
	private void INIT() {
		removeAll();
		setBackground(Color.BLACK);
		score();
		addFrame();
		addBorders();
		addKeyListeners();
		setObjects();
		lifeAdder();
		remove(frame);
	}

	// This method sets important objects on the screen
	private void setObjects() {
		bricks();
		initPaddle1();
		initPaddle2();
		customizeP1();
		customizeP2();
		initball1();
		initball2();
		customizeB1();
		customizeB2();
	}

	// This method allows us to add the frame around panel to appropriately put
	// objects
	private void addFrame() {
		frame = new GRect(0, 0.7, WIDTH - 1, HEIGHT - 27);
		frame.setColor(Color.WHITE);
		frame.setFilled(false);
		frame.setFillColor(null);
		add(frame);
	}

	// This method allows us to borders
	private void addBorders() {
		addBorder1();
		addBorder2();
	}

	// This is the border located at the lower screen
	private void addBorder1() {
		line1 = new GLine(0, frame.getHeight() - 30, WIDTH - 1, frame.getHeight() - 30);
		line1.setColor(Color.WHITE);
		line1.setVisible(true);
		add(line1);
	}

	// This is the border located at the upper screen
	private void addBorder2() {
		line2 = new GLine(0, frame.getY() + 30, WIDTH - 1, frame.getY() + 30);
		line2.setColor(Color.WHITE);
		line2.setVisible(true);
		add(line2);
	}

	// This method allows us to display lives of player 1
	private GObject p1Lives(int i) {
		GRect rect1 = new GRect(1, 1, 1, 1);
		rect1.setLocation(WIDTH - 30 - 15 * i, frame.getHeight() - 15);
		rect1.setSize(10, 10);
		rect1.setFillColor(Color.BLUE);
		rect1.setFilled(true);
		return rect1;
	}

	// This method adds live indicators on the screen
	private void lifeAdder() {
		for (int i = 0; i < 3; i++) {
			add(p1Lives(i));
			add(p2Lives(i));
		}
	}

	// This method allows us to display lives of player 2
	private GObject p2Lives(int j) {
		GRect rect2 = new GRect(1, 1, 1, 1);
		rect2.setLocation(WIDTH - 30 - 15 * j, frame.getY() + 5);
		rect2.setSize(10, 10);
		rect2.setFillColor(Color.RED);
		rect2.setFilled(true);
		return rect2;
	}

	// This method shows the appropriate message, when player 1 is ready to play
	private void player1Ready() {
		GLabel label = new GLabel("READY", 1, 1);
		label.setColor(Color.GREEN);
		label.setFont(new Font("Arial", Font.PLAIN, 100));
		label.setLocation(getWidth() / 2 - label.getWidth() / 2, getHeight() - label.getY() - label.getAscent());
		add(label);
		pause(30);
		remove(label);
	}

	// This method shows the appropriate message, when player 2 is ready to play
	private double player2Ready() {
		GLabel label = new GLabel("READY", 1, 1);
		label.setColor(Color.GREEN);
		label.setFont(new Font("Arial", Font.PLAIN, 100));
		label.setLocation(getWidth() / 2 - label.getWidth() / 2, paddle2.getY() + PADDLE_HEIGHT + 100);
		add(label);
		pause(30);
		remove(label);
		return (label.getY());
	}

	// This method draws all the bricks
	private void bricks() {
		int z = 0;
		while (z < NBRICK_ROWS) {
			drawRow(z, Color.GREEN);
			z++;
		}
	}

	// This method allows us to draw bricks
	private void drawBrick(int x, double y, Color Colour) {
		brick = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
		brick.setColor(Colour);
		brick.setFillColor(Colour);
		brick.setFilled(true);
		add(brick);
	}

	// This method allows us to draw rows
	private void drawRow(int j, Color RowColour) {
		for (int i = 0; i < NBRICKS_PER_ROW; i++) {
			drawBrick(i * (BRICK_SEP + BRICK_WIDTH) + BRICK_SEP / 2,
					(getHeight()) / 2 - 50 + (BRICK_HEIGHT + BRICK_SEP) * (j - 1), RowColour);
		}
	}

	// This method initiates lower paddle
	private void initPaddle1() {
		paddle1 = new GRect(getWidth() / 2 - PADDLE_WIDTH / 2, line1.getY() - 30, PADDLE_WIDTH, PADDLE_HEIGHT);
		add(paddle1);
	}

	// This method initiates upper paddle
	private void initPaddle2() {
		paddle2 = new GRect(getWidth() / 2 - PADDLE_WIDTH / 2, line2.getY() + 30 - PADDLE_HEIGHT, PADDLE_WIDTH,
				PADDLE_HEIGHT);
		add(paddle2);
	}

	// This method customizes lower paddle
	private void customizeP1() {
		paddle1.setFillColor(Color.BLUE);
		paddle1.setFilled(true);
	}

	// This method customizes upper paddle
	private void customizeP2() {
		paddle2.setFillColor(Color.RED);
		paddle2.setFilled(true);
	}

	// This method allows us to move lower paddle, after appropriate command
	private void movePaddle1() {
		if (checker1 == true) {
			if (paddle1.getX() + paddle1.getWidth() >= getWidth()) {
				paddle1.setLocation(getWidth() - paddle1.getWidth(), paddle1.getY());
			} else {
				paddle1.move(4, 0);
			}
		} else {
			if (paddle1.getX() <= 0) {
				paddle1.setLocation(0, paddle1.getY());
			} else {
				paddle1.move(-4, 0);
			}
		}
	}

	// This method allows us to move upper paddle, after appropriate command
	private void movePaddle2() {
		if (checker2 == true) {
			if (paddle2.getX() + paddle2.getWidth() >= getWidth()) {
				paddle2.setLocation(getWidth() - paddle2.getWidth(), paddle2.getY());
			} else {
				paddle2.move(4, 0);
			}
		} else {
			if (paddle2.getX() <= 0) {
				paddle2.setLocation(0, paddle2.getY());
			} else {
				paddle2.move(-4, 0);
			}
		}
	}

	// This method initiates the blue ball
	private void initball1() {
		ball1 = new GOval(getWidth() / 2 - BALL_RADIUS, line1.getY() - 170 - 2 * BALL_RADIUS, 2 * BALL_RADIUS,
				2 * BALL_RADIUS);
		add(ball1);
	}

	// This method initiates the red ball
	private void initball2() {
		ball2 = new GOval(getWidth() / 2 - BALL_RADIUS, line2.getY() + 170 - BALL_RADIUS, 2 * BALL_RADIUS,
				2 * BALL_RADIUS);
		add(ball2);
	}

	// This method customizes the blue ball
	private void customizeB1() {
		ball1.setColor(Color.BLUE);
		ball1.setFillColor(Color.BLUE);
		ball1.setFilled(true);
	}

	// This method customizes the red ball
	private void customizeB2() {
		ball2.setColor(Color.RED);
		ball2.setFillColor(Color.RED);
		ball2.setFilled(true);
	}

	// This method returns the boolean and checks the wall contact of blue ball
	private boolean statement1() {
		if (ball1.getX() <= 0 || ball1.getX() + 2 * BALL_RADIUS >= getWidth()) {
			return true;
		} else {
			return false;
		}
	}

	// This method returns the boolean and checks the wall contact of red ball
	private boolean statement2() {
		if (ball2.getX() <= 0 || ball2.getX() + 2 * BALL_RADIUS >= getWidth()) {
			return true;
		} else {
			return false;
		}
	}

	// This method allows us to control the upper and lower paddles with
	// keyboard
	public void keyPressed(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_D) {
			checker2 = true;
			starter2 = true;
		}
		if (event.getKeyCode() == KeyEvent.VK_A) {
			checker2 = false;
		}
		if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
			checker1 = true;
			starter1 = true;
		}
		if (event.getKeyCode() == KeyEvent.VK_LEFT) {
			checker1 = false;
		}
	}

	// With this method, balls are able to move
	private void ballMotion() {
		while (true) {
			movePaddle1();
			movePaddle2();
			pause(8);
			ball1.move(Vx1, Vy1);
			ball2.move(Vx2, -Vy2);
			wallBounceB1();
			wallBounceB2();
			if (ball1.getY() + 2 * BALL_RADIUS >= line1.getY()) {
				ball1.setLocation(getWidth() / 2 - BALL_RADIUS, getHeight() / 2 + TOTAL_BHEIGHT / 2);
				paddle1.setLocation(getWidth() / 2 - PADDLE_WIDTH, paddle1.getY());
				c = 1;
				Vx1 = ranN.nextDouble(1, 2);
				int t1 = ranN.nextInt(2);
				if (t1 == 1) {
					Vx1 = -Vx1;
				}
				break;
			} else if (ball2.getY() <= line2.getY()) {
				ball2.setLocation(getWidth() / 2 - BALL_RADIUS,
						paddle2.getY() + PADDLE_HEIGHT + (paddle1.getY() - ball1.getY() - 2 * BALL_RADIUS));
				paddle2.setLocation(getWidth() / 2 - PADDLE_WIDTH / 2, line2.getY() + 30 - PADDLE_HEIGHT);
				c = -1;
				Vx2 = ranN.nextDouble(1, 2);
				int t2 = ranN.nextInt(2);
				if (t2 == 1) {
					Vx2 = -Vx2;
				}
				break;
			}
			destroyBricks1();
			destroyBricks2();
		}
	}

	// With this, blue ball can bounce off of walls
	private void wallBounceB1() {
		if (statement1()) {
			if (ball1.getX() > getWidth() / 2) {
				ball1.setLocation(ball1.getX() - 5, ball1.getY());
			} else {
				ball1.setLocation(ball1.getX() + 5, ball1.getY());
			}
			Vx1 = -Vx1;
		}
	}

	// With this, red ball can bounce off of walls
	private void wallBounceB2() {
		if (statement2()) {
			if (ball2.getX() > getWidth() / 2) {
				ball2.setLocation(ball2.getX() - 5, ball2.getY());
			} else {
				ball2.setLocation(ball2.getX() + 5, ball2.getY());
			}
			Vx2 = -Vx2;
		}
	}

	// This method destroys the brick, after blue ball touches it
	private void destroyBricks1() {
		if (getCollidingobject1() != null) {
			if (getCollidingobject1() == paddle2) {
				ball1.setLocation(ball1.getX(), ball1.getY() + paddle2.getHeight());
				Vy1 = -Vy1;
			} else if (getCollidingobject1() == paddle1) {
				ball1.setLocation(ball1.getX(), ball1.getY() - paddle2.getHeight());
				Vy1 = -Vy1;
			} else if (getCollidingobject1() == ball2) {
				Vx1 = -Vx1;
			} else if (getCollidingobject1() == line2) {
				Vy1 = -Vy1;
			} else if (getCollidingobject1() == line1) {
			} else {
				remove(getCollidingobject1());
				i++;
				if (i == TOTAL_BRICKS) {
					i = -100;
					showDown();
				}
				Vy1 = -Vy1;
			}
		}
	}

	// This method destroys the brick, after red ball touches it
	private void destroyBricks2() {
		if (getCollidingobject2() != null) {
			if (getCollidingobject2() == paddle2) {
				ball2.setLocation(ball2.getX(), ball2.getY() + paddle2.getHeight());
				Vy2 = -Vy2;
			} else if (getCollidingobject2() == paddle1) {
				ball2.setLocation(ball2.getX(), ball2.getY() - paddle2.getHeight());
				Vy2 = -Vy2;
			} else if (getCollidingobject2() == ball1) {
				Vx2 = -Vx2;
			} else if (getCollidingobject2() == line1) {
				Vy2 = -Vy2;
			} else if (getCollidingobject2() == line2) {
			} else {
				remove(getCollidingobject2());
				i++;
				if (i == TOTAL_BRICKS) {
					i = -100;
					showDown();
				}
				Vy2 = -Vy2;
			}
		}
	}

	// This method returns the collided brick for blue ball
	private GObject getCollidingobject1() {
		GObject B1p1 = getElementAt(ball1.getX(), ball1.getY());
		GObject B1p2 = getElementAt(ball1.getX(), ball1.getY() + 2 * BALL_RADIUS);
		GObject B1p3 = getElementAt(ball1.getX() + 2 * BALL_RADIUS, ball1.getY() + 2 * BALL_RADIUS);
		GObject B1p4 = getElementAt(ball1.getX() + 2 * BALL_RADIUS, ball1.getY());
		if (B1p1 == null == false) {
			return B1p1;
		} else if (B1p2 == null == false) {
			return B1p2;
		} else if (B1p3 == null == false) {
			return B1p3;
		} else if (B1p4 == null == false) {
			return B1p4;
		} else {
			return null;
		}
	}

	// This method returns the collided brick for red ball
	private GObject getCollidingobject2() {
		GObject B2p1 = getElementAt(ball2.getX(), ball2.getY());
		GObject B2p2 = getElementAt(ball2.getX(), ball2.getY() + 2 * BALL_RADIUS);
		GObject B2p3 = getElementAt(ball2.getX() + 2 * BALL_RADIUS, ball2.getY() + 2 * BALL_RADIUS);
		GObject B2p4 = getElementAt(ball2.getX() + 2 * BALL_RADIUS, ball2.getY());
		if (B2p1 == null == false) {
			return B2p1;
		} else if (B2p2 == null == false) {
			return B2p2;
		} else if (B2p3 == null == false) {
			return B2p3;
		} else if (B2p4 == null == false) {
			return B2p4;
		} else {
			return null;
		}
	}

	// This method inputs the message, if player 1 wins
	private void P1Win() {
		remove(score);
		score();
		GLabel label = new GLabel("P1 WINS!", 1, 1);
		label.setColor(Color.GREEN);
		label.setFont(new Font("Arial", Font.PLAIN, 50));
		label.setLocation(getWidth() / 2 - label.getWidth() / 2, getHeight() / 2 - label.getHeight() / 2 - 50);
		repaint();
		add(label);
	}

	// This method inputs the message, if player 2 wins
	private void P2Win() {
		remove(score);
		score();
		GLabel label = new GLabel("P2 WINS!", 1, 1);
		label.setColor(Color.GREEN);
		label.setFont(new Font("Arial", Font.PLAIN, 50));
		label.setLocation(getWidth() / 2 - label.getWidth() / 2, getHeight() / 2 - label.getHeight() / 2 - 50);
		repaint();
		add(label);
	}

	// This method gets us into SHOWDOWN
	private void showDown() {
		GLabel label = new GLabel("SHOWDOWN!", 1, 1);
		label.setColor(Color.RED);
		label.setFont(new Font("Arial", Font.PLAIN, 50));
		label.setLocation(getWidth() / 2 - label.getWidth() / 2, getHeight() / 2 - label.getHeight() / 2 - 50);
		repaint();
		add(label);
		pause(500);
		remove(label);
		paddle1.setSize(paddle1.getWidth() / 2, paddle1.getHeight());
		paddle2.setSize(paddle2.getWidth() / 2, paddle2.getHeight());
	}
    // This method allows us to show the score at the top - left screen
	private void score() {
		score = new GLabel("SCORE: " + score1 + " - " + score2, 10, 22);
		score.setColor(Color.WHITE);
		score.setFont(new Font("Arial", Font.PLAIN, 20));
		add(score);
	}
}
