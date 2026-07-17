
/*
 * File: NameSurferEntry.java
 * --------------------------
 * This class represents a single entry in the database.  Each
 * NameSurferEntry contains a name and a list giving the popularity
 * of that name for each decade stretching back to 1900.
 */

import acm.util.*;
import java.util.*;

public class NameSurferEntry implements NameSurferConstants {
	private String name;
	private int[] ranks;

	public NameSurferEntry(String line) {
		StringTokenizer tokens = new StringTokenizer(line);
		name = tokens.nextToken();
		ranks = new int[11];
		for (int i = 0; i < 11; i++) {
			ranks[i] = Integer.parseInt(tokens.nextToken());
		}
	}

	// This method returns the String, which contains ranks of each decade
	public String getRanks() {
		String s = "";
		for (int i = 0; i < ranks.length; i++) {
			s = s + ranks[i] + " ";
		}
		return s;
	}

	// This method makes us able to return the particular name
	public String getName() {
		return name;
	}

	// Returns the rank of the particular name after n decades passed
	public int getRank(int decade) {
		return ranks[decade];
	}

	// Turns information into string for conveniency
	public String toString() {
		String entry = name + " [";
		for (int i = 0; i < ranks.length - 1; i++) {
			int t = ranks[i];
			entry = entry + Integer.toString(t) + " ";
		}
		entry = entry + Integer.toString(ranks[ranks.length - 1]) + "]";
		return entry;
	}
}
