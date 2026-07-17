
/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is resized.
 */

import acm.graphics.*;
import acm.util.RandomGenerator;

import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraph extends GCanvas implements NameSurferConstants, ComponentListener {
	public NameSurferGraph() {
		addComponentListener(this);
	}

	// This method clears up listOfEntries and updates the screen
	public void clear() {
		listOfEntries.removeAll(listOfEntries);
		update();
	}

	private ArrayList<NameSurferEntry> listOfEntries = new ArrayList<NameSurferEntry>();
	private Color[] colors = new Color[4];

	// This method allows us to add new entries to listOfEntries
	public void addEntry(NameSurferEntry entry) {
		listOfEntries.add(entry);
	}

	// This method updates the screen, by first removing everything and then
	// adding what is necessary
	public void update() {
		removeAll();
		addBackGround();
		for (int i = 0; i < listOfEntries.size(); i++) {
			int k = i % 4;
			addGraphics(listOfEntries.get(i), colors[k]);
		}
	}

	// This method is responsible for all the background structure
	private void addBackGround() {
		fillColors();
		addVerticalLines();
		addHorizontalLines();
		addYears();
	}

	// This method allows us to color the graphs
	private void fillColors() {
		colors[0] = Color.black;
		colors[1] = Color.blue;
		colors[2] = Color.magenta;
		colors[3] = Color.RED;
	}

	// This method allows us to add vertical lines
	private void addVerticalLines() {
		for (int i = 0; i < NDECADES; i++) {
			GLine line = new GLine(i * (getWidth() / NDECADES), 0, i * (getWidth() / NDECADES), getHeight());
			add(line);
		}
	}

	// This method allows us to add horizontal lines
	private void addHorizontalLines() {
		GLine line1 = new GLine(0, GRAPH_MARGIN_SIZE, getWidth(), GRAPH_MARGIN_SIZE);
		GLine line2 = new GLine(0, getHeight() - GRAPH_MARGIN_SIZE, getWidth(), getHeight() - GRAPH_MARGIN_SIZE);
		add(line1);
		add(line2);
	}

	// This method allows us to add years on the screen
	private void addYears() {
		for (int i = 0; i < NDECADES; i++) {
			GLabel year = new GLabel(Integer.toString(1900 + 10 * i));
			year.setLocation(i * (getWidth() / NDECADES) + 2, getHeight() - GRAPH_MARGIN_SIZE + year.getAscent());
			add(year);
		}
	}

	// This method adds labels and lines on the screen
	private void addGraphics(NameSurferEntry entry, Color color) {
		addLabels(entry, color);
		addLines(entry, color);
	}

	// This method creates labels and adds it to the screen(for graph)
	private void addLabels(NameSurferEntry entry, Color color) {
		int part = getHeight() - 2 * GRAPH_MARGIN_SIZE;
		if (entry != null) {
			for (int i = 0; i < 11; i++) {
				if (entry.getRank(i) != 0) {
					addLabel(i, entry, color, part, entry.getName() + " " + Integer.toString(entry.getRank(i)),
							GRAPH_MARGIN_SIZE + ((double) entry.getRank(i) / 1000) * part - 3);
				} else {
					addLabel(i, entry, color, part, entry.getName() + "*", getHeight() - GRAPH_MARGIN_SIZE - 3);
				}
			}
		}
	}

	// This method creates graph lines and adds it to the screen
	private void addLines(NameSurferEntry entry, Color color) {
		int part = getHeight() - 2 * GRAPH_MARGIN_SIZE;
		if (entry != null) {
			for (int i = 0; i < 10; i++) {
				if (entry.getRank(i) != 0) {
					if (entry.getRank(i + 1) != 0) {
						addLine(i, entry, color, part, GRAPH_MARGIN_SIZE + ((double) entry.getRank(i) / 1000) * part,
								GRAPH_MARGIN_SIZE + ((double) entry.getRank(i + 1) / 1000) * part);
					} else if (entry.getRank(i + 1) == 0) {
						addLine(i, entry, color, part, GRAPH_MARGIN_SIZE + ((double) entry.getRank(i) / 1000) * part,
								part + GRAPH_MARGIN_SIZE);
					}
				} else {
					if (entry.getRank(i + 1) != 0) {
						addLine(i, entry, color, part, part + GRAPH_MARGIN_SIZE,
								GRAPH_MARGIN_SIZE + ((double) entry.getRank(i + 1) / 1000) * part);
					} else {
						addLine(i, entry, color, part, part + GRAPH_MARGIN_SIZE, part + GRAPH_MARGIN_SIZE);
					}
				}
			}
		}
	}

	// This method allows us to add single colored graph line to the screen
	private void addLine(int i, NameSurferEntry entry, Color color, int part, double Y1Comp, double Y2Comp) {
		GLine line = new GLine(i * (getWidth() / 11), Y1Comp, (i + 1) * (getWidth() / 11), Y2Comp);
		line.setColor(color);
		add(line);
	}

	// This method allows us to add single colored graph lable to the screen
	private void addLabel(int i, NameSurferEntry entry, Color color, int part, String string, double YComp) {
		GLabel label = new GLabel(string);
		label.setColor(color);
		label.setLocation(i * (getWidth() / 11) + 3, YComp);
		add(label);
	}

	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		update();
	}

	public void componentShown(ComponentEvent e) {
	}
}
