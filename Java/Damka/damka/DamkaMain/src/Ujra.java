import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import acm.graphics.GOval;
import acm.graphics.GRect;

public class Ujra {
	HashMap<String, String> ujraMap;
	HashMap<String, GRect> ujraMapRect;
	ArrayList<GRect> rects;
	public Ujra() {
		ujraMap = new HashMap<>();
		rects = new ArrayList<>();
		ujraMapRect = new HashMap<>();
	}

	public void add(GRect rect, int i, int j) {
		String s = Integer.toString(i) + " " + Integer.toString(j);
		ujraMap.put(s, stringize(rect));
		ArrayList<Integer> list = new ArrayList<>();
		list.add(i);
		list.add(j);
		ujraMapRect.put(list.toString(), rect);
		rects.add(rect);
	}

	public ArrayList<Integer> getLoc(GRect rect) {
		ArrayList<String> list = new ArrayList<>(ujraMap.keySet());
		for (int i = 0; i < list.size(); i++) {
			if (ujraMap.get(list.get(i)).equals(stringize(rect))) {
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

	public GRect getUjra(int m, int n) {
		ArrayList<Integer> list = new ArrayList<>();
		list.add(m);
		list.add(n);
		String s = list.toString();
		if(retrieve(s) != null)return retrieve(s);
		return null;
	}

	public GRect retrieve(String s) {
		if(ujraMapRect.get(s) != null)return (GRect) ujraMapRect.get(s);
		return null;
	}

	public String stringize(GRect rect) {
		if(rect != null) {
		String s1 = Double.toString(rect.getX());
		String s2 = Double.toString(rect.getY());
		return s1 + " " + s2; }
		return null;
	}
}
