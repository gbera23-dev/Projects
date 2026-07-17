
/*
 * File: HangmanLexicon.java
 * -------------------------
 * This file contains a stub implementation of the HangmanLexicon
 * class that you will reimplement for Part III of the assignment.
 */

import java.io.*;
import java.util.ArrayList;
import com.sun.xml.internal.fastinfoset.util.StringArray;
import acm.util.ErrorException;

public class HangmanLexicon {
	private BufferedReader reader;

	/** Returns the number of words in the lexicon. */
	public int getWordCount() {
		return 121806;
	}

	/** Returns the word at the specified index. */
	public String getWord(int index) {
		initReader();
		String m = "";
		m = list().get(index);
		closeReader();
		return m;
	}

	// This method opens the file
	private void initReader() {
		try {
			reader = new BufferedReader(new FileReader("HangmanLexicon.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// This method closes the file
	private void closeReader() {
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// This method creates the list of all words in the file
	private ArrayList<String> list() {
		ArrayList<String> string = new ArrayList<String>();
		try {
			for (int i = 0; i < getWordCount(); i++) {
				string.add(reader.readLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return string;
	}
}