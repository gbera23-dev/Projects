
/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.program.*;
import sun.rmi.server.ActivationGroupInit;

import java.awt.event.*;
import javax.swing.*;

public class NameSurfer extends Program implements NameSurferConstants {
	private JTextField txtfield;
	private JButton Graph;
	private JButton Clear;
	private NameSurferDataBase data;
	private NameSurferEntry entry;
	private NameSurferGraph graph;
	public void init() {
		JLabel label = new JLabel("Name");
		add(label, SOUTH);
		setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		txtfield = new JTextField(20);
		add(txtfield, SOUTH);
		Graph = new JButton("Graph");
		add(Graph, SOUTH);
		Clear = new JButton("Clear");
		add(Clear, SOUTH);
		txtfield.addActionListener(this);
		Graph.addActionListener(this);
		Clear.addActionListener(this);
		data = new NameSurferDataBase(NAMES_DATA_FILE);
		graph = new NameSurferGraph(); 
		add(graph);
	}
	//This method responds on screen interactions
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() != null) {
			if (e.getSource().equals(Clear)) {
				graph.clear();
			} else if (e.getSource().equals(Graph)) {
				data.findEntry(txtfield.getText());
				entry = data.findEntry(txtfield.getText());
				if (entry != null) {
					graph.addEntry(entry);
					graph.update();
				}
			} else {
				data.findEntry(txtfield.getText());
				entry = data.findEntry(txtfield.getText());
				if (entry != null) {
					graph.addEntry(entry);
					graph.update();
				}
			}
		}
	}
}
