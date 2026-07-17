
/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.awt.*;
import java.util.Random;
import java.applet.*;
import java.awt.event.*;

//First we write the console version of the game
//We just create the system so that when a word is written
//Computer will turn it into dashes and do the algorithm of hangman game
public class Hangman extends ConsoleProgram {
	private HangmanCanvas canvas;
	private HangmanLexicon lexicon;
	private Random number;
	String word;
	String mword;
	String hiddenWord;
	String p;
	String notInWord;
	int i;

	// This method allows us to initialize the game
	public void init() {
		canvas = new HangmanCanvas();
		lexicon = new HangmanLexicon();
		number = new Random();
		canvas.setSize(getWidth() / 2, getHeight());
		add(canvas);
	}

	public void run() {
		while (true) {
			inittext();
			while (i < 9) {
				String letter = readLine("YOUR GUESS: ");
				if (letter.isEmpty()) {
					println("Write something");
				} else {
					char l = letter.charAt(0);
					if (Character.isLowerCase(l)) {
						letter = capitalize(letter);
					}
					if (notInWord.contains(letter) || p.contains(letter)) {
						println("You have already inputted this letter!");
					} else {
						analyzeLetter(letter);
						analyzeWord(letter);
						if (stringBuilder(letter) == 1) {
							break;
						}
						if (letterChecker(letter) == false) {
							if (i == 8) {
								println("YOU ARE COMPLETELY HUNG!");
								println("WORD WAS " + "'" + mword + "'");
								canvas.noteIncorrectGuess(notInWord + letter.charAt(0));
								break;
							} else {
								notInWord(letter);
							}
						}
					}
				}
			}
			println("Restarting game...");
			pause(1000);
		}
	}

	// This method replaces dashes with the input letter, if input letter is in
	// the word
	private void analyzeWord(String letter) {
		if (letter.length() == 1) {
			char l = letter.charAt(0);
			hiddenWord = word.replace(l, '_');
			word = hiddenWord;
		}
	}

	// This method gives us dashes and letters on display
	private int stringBuilder(String letter) {
		int z = 0;
		String s = mword;
		p = "";
		if (letter.length() == 1) {
			for (int i = 0; i < word.length(); i++) {
				if (hiddenWord.charAt(i) == '_') {
					p += s.charAt(i) + "";
				} else {
					p += "_" + " ";
				}
			}
			if (noDash(p)) {
				println("YOU HAVE GUESSED THE WORD!");
				println("Word is " + mword);
				canvas.displayWord(p);
				z = 1;
			} else if (letterChecker(letter) == false) {
				letterChecker(letter);
			} else if (letter.length() == 1) {
				println("That is a valid letter!");
				println("word looks like this: " + p);
				canvas.displayWord(p);
			}
		}
		return z;
	}

	// This method checks, whether inputted letter is in the word
	private boolean letterChecker(String letter) {
		if (mword.contains(letter)) {
			return true;
		} else {
			return false;
		}
	}

	// This method allows us to determine, if we have guessed the word
	private boolean noDash(String p) {
		for (int i = 0; i < p.length(); i++) {
			if (p.charAt(i) == '_') {
				return false;
			}
		}
		return true;
	}

	// This method checks, whether input is really a letter or not
	private void analyzeLetter(String letter) {
		if (letter.length() == 1 && Character.isLetter(letter.charAt(0)) == false) {
			println("Write the letter!");
			println("try again!");
		} else if (letter.length() != 1) {
			println("Write the single letter!");
			println("try again!");
		}
	}

	// This method saves all the inputted letters, which are not in the word
	private void notInWord(String letter) {
		if (letter.length() == 1 && Character.isLetter(letter.charAt(0))) {
			println("There are no " + letter + " - s in this word!");
			println((8 - i) + " attempts left!");
			i++;
			if (notInWord.contains(letter)) {
				println(notInWord);
			} else {
				notInWord += letter + ", ";
				println(notInWord);
				canvas.noteIncorrectGuess(notInWord);
			}
		}
	}

	// This method capitalizes the input
	private String capitalize(String s) {
		if (s.length() == 1) {
			char l = s.charAt(0);
			l = (char) (l - ('a' - 'A'));
			s = "" + l;
		}
		return s;
	}

	// This method initially displays dashes on the screen
	private String dashes(String word) {
		String s = "";
		for (int i = 0; i < word.length(); i++) {
			s = s + '_' + ' ';
		}
		return s;
	}

	// This method initiates the text
	private void inittext() {
		println("WELCOME TO HANGMAN!");
		int ran = number.nextInt(lexicon.getWordCount());
		word = lexicon.getWord(ran);
		mword = word;
		p = "";
		notInWord = "";
		i = 1;
		canvas.reset();
		canvas.displayWord(dashes(word));
	}
}
