
/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import java.awt.Font;
import acm.graphics.*;

public class HangmanCanvas extends GCanvas {
	GLabel label = new GLabel("");
	GPoint point;
	int m;

	/** Resets the display so that only the scaffold appears */
	public void reset() {
		m = 0;
		removeAll();
		addGallow();
	}

	/**
	 * Updates the word on the screen to correspond to the current state of the
	 * game. The argument string shows what letters have been guessed so far;
	 * unguessed letters are indicated by hyphens.
	 */
	public void displayWord(String word) {
		remove(label);
		label.setLabel(word);
		label.setFont(new Font("Arial", Font.PLAIN, 30));
		label.setLocation(getWidth() / 2 - BEAM_LENGTH, 420);
		add(label);
	}

	/**
	 * Updates the display to correspond to an incorrect guess by the user.
	 * Calling this method causes the next body part to appear on the scaffold
	 * and adds the letter to the list of incorrect guesses that appears at the
	 * bottom of the window.
	 */
	public void noteIncorrectGuess(String notInWord) {
		m++;
		GLabel missingletters = new GLabel(notInWord);
		missingletters.setFont(new Font("Arial", Font.PLAIN, 20));
		missingletters.setLocation(getWidth() / 2 - BEAM_LENGTH, 450);
		add(missingletters);
		addPart(m);
		if (notInWord.length() == 14) {
			addPart(8);
		}
	}

	// This method allows us to add the gallow
	private void addGallow() {
		GLine line;
		double m = getWidth() / 2;
		double n = getHeight() / 2 - 190;
		point = new GPoint(m, n);
		line = new GLine(m, n, m, n - ROPE_LENGTH);
		add(line);
		line = new GLine(m, n - ROPE_LENGTH, m - BEAM_LENGTH, n - ROPE_LENGTH);
		add(line);
		line = new GLine(m - BEAM_LENGTH, n - ROPE_LENGTH, m - BEAM_LENGTH, n - ROPE_LENGTH + SCAFFOLD_HEIGHT);
		add(line);
	}

	// This method returns the body part
	private void addPart(int i) {
		if (i == 1) {
			addHead();
		} else if (i == 2) {
			addBody();
		} else if (i == 3) {
			addLeftHand();
		} else if (i == 4) {
			addRightHand();
		} else if (i == 5) {
			addLeftLeg();
		} else if (i == 6) {
			addRightLeg();
		} else if (i == 7) {
			addLeftFoot();
		} else if (i == 8) {
			addRightFoot();
		}
	}

	// This method adds the head
	private void addHead() {
		GOval head = new GOval(2 * HEAD_RADIUS, 2 * HEAD_RADIUS);
		head.setLocation(point);
		head.move(-HEAD_RADIUS, 0);
		add(head);
	}

	// This method adds the body
	private void addBody() {
		GLine line = new GLine(100, 100, 100, 100 + BODY_LENGTH);
		line.setLocation(point);
		line.move(0, 2 * HEAD_RADIUS);
		add(line);
	}

	// This method adds left hand
	private void addLeftHand() {
		GLine line;
		line = new GLine(0, 1, UPPER_ARM_LENGTH, 1);
		line.setLocation(point);
		line.move(0, 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD);
		add(line);
		line = new GLine(0, 1, 0, LOWER_ARM_LENGTH);
		line.setLocation(point);
		line.move(UPPER_ARM_LENGTH, 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD);
		add(line);
	}

	// This method adds right hand
	private void addRightHand() {
		GLine line;
		line = new GLine(0, 1, -UPPER_ARM_LENGTH, 1);
		line.setLocation(point);
		line.move(0, 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD);
		add(line);
		line = new GLine(0, 1, 0, LOWER_ARM_LENGTH);
		line.setLocation(point);
		line.move(-UPPER_ARM_LENGTH, 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD);
		add(line);
	}

	// This method adds left leg
	private void addLeftLeg() {
		GLine line;
		line = new GLine(0, 1, 2 * HIP_WIDTH, 1);
		line.setLocation(point);
		line.move(-HIP_WIDTH, 2 * HEAD_RADIUS + BODY_LENGTH);
		add(line);
		line = new GLine(0, 0, 0, LEG_LENGTH);
		line.setLocation(point);
		line.move(-HIP_WIDTH, 2 * HEAD_RADIUS + BODY_LENGTH);
		add(line);
	}

	// This method adds right leg
	private void addRightLeg() {
		GLine line = new GLine(0, 0, 0, LEG_LENGTH);
		line.setLocation(point);
		line.move(+HIP_WIDTH, 2 * HEAD_RADIUS + BODY_LENGTH);
		add(line);
	}

	// This method adds left foot
	private void addLeftFoot() {
		GLine line = new GLine(0, 1, -FOOT_LENGTH, 1);
		line.setLocation(point);
		line.move(-HIP_WIDTH, 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH);
		add(line);
	}

	// This method adds right foot
	private void addRightFoot() {
		GLine line = new GLine(0, 1, +FOOT_LENGTH, 1);
		line.setLocation(point);
		line.move(+HIP_WIDTH, 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH);
		add(line);
	}

	/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 36;
	private static final int BODY_LENGTH = 144;
	private static final int ARM_OFFSET_FROM_HEAD = 28;
	private static final int UPPER_ARM_LENGTH = 72;
	private static final int LOWER_ARM_LENGTH = 44;
	private static final int HIP_WIDTH = 36;
	private static final int LEG_LENGTH = 108;
	private static final int FOOT_LENGTH = 28;
}
