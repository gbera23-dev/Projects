import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import acm.graphics.GOval;
import acm.graphics.GRect;

public class Kenchi {
	HashMap<String, String> kenchiMap;
	HashMap<String, GOval> kenchiMapOval;

	public Kenchi() {
		kenchiMap = new HashMap<>();
		kenchiMapOval = new HashMap<>();
	}

	public void add(GOval oval, int i, int j) {
		String s = Integer.toString(i) + " " + Integer.toString(j);
		kenchiMap.put(s, stringize(oval));
		kenchiMapOval.put(s, oval);
	}

	public void empty(ArrayList<Integer> list) {
		String s = Integer.toString(list.get(0)) + " " + Integer.toString(list.get(1));
		if (kenchiMap.containsKey(s)) {
			kenchiMap.remove(s);
			kenchiMapOval.remove(s);
		}
	}

	public void setDamka(GOval kenchi) {
		kenchi.setColor(Color.yellow);
	}

	public ArrayList<Integer> getLoc(GOval oval) {
		ArrayList<String> list = new ArrayList<>(kenchiMap.keySet());
		for (int i = 0; i < list.size(); i++) {
			if (kenchiMap.get(list.get(i)).equals(stringize(oval))) {
				String t = list.get(i);
				StringTokenizer tokens = new StringTokenizer(t);
				ArrayList<Integer> loc = new ArrayList<>();
				loc.add(Integer.parseInt(tokens.nextToken()));
				loc.add(Integer.parseInt(tokens.nextToken()));
				return loc;
			}
		}
		return null;
	}

	
	public GOval getKenchi(int m, int n) {
		String t = Integer.toString(m) + " " + Integer.toString(n);
		return retrieve(t);
	}

	public GOval retrieve(String s) {
		return (GOval) kenchiMapOval.get(s);
	}

	public String stringize(GOval oval) {
		String s1 = Double.toString(oval.getX());
		String s2 = Double.toString(oval.getY());
		return s1 + " " + s2;
	}
}
