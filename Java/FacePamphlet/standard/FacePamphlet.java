
/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;

import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

public class FacePamphlet extends Program implements FacePamphletConstants {
	/* BUTTONS */
	private JButton add;
	private JButton delete;
	private JButton lookup;
	private JButton changestatus;
	private JButton changepicture;
	private JButton addfriend;
	/* TXTFIELDS */
	private JTextField uppertxtfield;
	private JTextField lefttxtfield1;
	private JTextField lefttxtfield2;
	private JTextField lefttxtfield3;
	/* FacePamphletProfile */
	private FacePamphletProfile prof;
	private FacePamphletProfile currentProf;
	/* FacePamphletDatabase */
	private FacePamphletDatabase database;
	/* FacePamphletCanvas */
	private FacePamphletCanvas canvas;

	// This method is responsible for initialization
	public void init() {
		drawAllNorthSide();
		drawAllWestSide();
		addActionListeners(this);
		database = new FacePamphletDatabase();
		canvas = new FacePamphletCanvas();
		setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		add(canvas);
	}

	// This method governs all the interactions
	public void actionPerformed(ActionEvent e) {
		/* UPPER INTERACTIONS */
		String uppertxt = uppertxtfield.getText();
		String lefttxt1 = lefttxtfield1.getText();
		String lefttxt2 = lefttxtfield2.getText();
		String lefttxt3 = lefttxtfield3.getText();
		boolean emptytxt = uppertxt.equals("");
		boolean emptytxt1 = lefttxtfield1.getText().equals("");
		boolean emptytxt2 = lefttxtfield2.getText().equals("");
		boolean emptytxt3 = lefttxtfield3.getText().equals("");
		if ((e.getSource().equals(add) && !emptytxt) || e.getSource().equals(uppertxtfield)) {
			addButtonInteractions(uppertxt);
		} else if (e.getSource().equals(delete) && !emptytxt) {
			deleteButtonInteractions(uppertxt);
		} else if (e.getSource().equals(lookup) && !emptytxt) {
			lookupButtonInteractions(uppertxt);
		}
		/* LEFT INTERACTIONS */
		else if ((e.getSource().equals(changestatus) || e.getSource().equals(lefttxtfield1)) && !emptytxt1) {
			changeStButtonInteractions(lefttxt1);
		} else if ((e.getSource().equals(changepicture) || e.getSource().equals(lefttxtfield2)) && !emptytxt2) {
			changeImButtonInteractions(lefttxt2);
		} else if ((e.getSource().equals(addfriend) || e.getSource().equals(lefttxtfield3)) && !emptytxt3) {
			addFrButtonInteractions(lefttxt3);
		}
	}

	// This method adds addFriend button interactions
	private void addFrButtonInteractions(String lefttxt3) {
		FacePamphletProfile friendsFriend = database.getProfile(lefttxt3);
		if (currentProf != null) {
			if (database.containsProfile(lefttxt3)) {
				if (currentProf.getName().equals(friendsFriend.getName())) {
					canvas.displayProfile(currentProf);
					canvas.showMessage("you cannot add yourself");
				} else {
					if (database.hasFriend(friendsFriend, currentProf.getName())) {
						canvas.showMessage("Friend is already in the friend list");
					} else {
						currentProf.addFriend(lefttxt3);
						canvas.displayProfile(currentProf);
						canvas.showMessage(lefttxt3 + " added as a friend!");
						friendsFriend.addFriend(currentProf.getName());
					}
				}
			} else {
				canvas.showMessage("No such person exists");
			}
		} else {
			canvas.showMessage("Please choose the profile you want to add friends to");
		}
	}

	// This method is responsible for changeImage Button interactions
	private void changeImButtonInteractions(String lefttxt2) {
		if (currentProf == null) {
			canvas.displayProfile(currentProf);
			canvas.showMessage("Please choose the profile to change picture");
		} else {
			GImage image = null;
			try {
				image = new GImage(lefttxt2);
				/*****/
			} catch (ErrorException ex) {
			}
			if (image == null) {
				canvas.displayProfile(currentProf);
				canvas.showMessage("This is not the valid filename");
			} else {
				currentProf.setImage(image);
				canvas.displayProfile(currentProf);
				canvas.showMessage("Picture updated");
			}
		}
	}

	// This method is responsible for changeStatus Button interactions
	private void changeStButtonInteractions(String lefttxt1) {
		if (currentProf != null) {
			currentProf.setStatus(lefttxt1);
			database.addProfile(currentProf);
			canvas.displayProfile(currentProf);
			canvas.showMessage("Status updated to " + currentProf.getStatus());
		} else {
			canvas.displayProfile(currentProf);
			canvas.showMessage("Please choose the profile to change status");
		}
	}

	// This method is responsible for lookUp Button interactions
	private void lookupButtonInteractions(String uppertxt) {
		prof = database.getProfile(uppertxt);
		if (prof != null) {
			currentProf = prof;
			canvas.displayProfile(currentProf);
			canvas.showMessage("Displaying " + prof.getName());
		} else {
			currentProf = null;
			canvas.displayProfile(currentProf);
			canvas.showMessage("profile with name " + uppertxt + " does not exist");
		}
	}

	// This method is responsible for delete button interactions
	private void deleteButtonInteractions(String uppertxt) {
		prof = new FacePamphletProfile(uppertxt);
		if (database.containsProfile(uppertxt)) {
			database.deleteProfile(uppertxt);
			currentProf = null;
			canvas.displayProfile(currentProf);
			canvas.showMessage("Profile of " + uppertxt + " deleted");
		} else {
			currentProf = null;
			canvas.displayProfile(currentProf);
			canvas.showMessage("profile with name " + uppertxt + " does not exist");
		}
	}

	// This method is responsible for add button interactions
	private void addButtonInteractions(String uppertxt) {
		if (uppertxt.equals("")) {
			canvas.showMessage("input the real profile name");
		} else {
			prof = database.getProfile(uppertxt);
			if (prof == null) {
				prof = new FacePamphletProfile(uppertxt);
				database.addProfile(prof);
				currentProf = prof;
				canvas.displayProfile(currentProf);
				canvas.showMessage("New profile created");
			} else {
				currentProf = prof;
				canvas.displayProfile(currentProf);
				canvas.showMessage("Profile already exists");
			}
		}
	}

	// This method adds texts everything on the NORTH side
	private void drawAllNorthSide() {
		uppertxtfield = new JTextField(TEXT_FIELD_SIZE);
		add(new JLabel("Name"), NORTH);
		add(uppertxtfield, NORTH);
		addNorthButtons();
		uppertxtfield.addActionListener(this);
	}

	// This method adds buttons to NORTH side
	private void addNorthButtons() {
		add(add = new JButton("Add"), NORTH);
		add(delete = new JButton("Delete"), NORTH);
		add(lookup = new JButton("Lookup"), NORTH);
	}

	// This method draws all the WEST side
	private void drawAllWestSide() {
		lefttxtfield1 = new JTextField(TEXT_FIELD_SIZE);
		lefttxtfield2 = new JTextField(TEXT_FIELD_SIZE);
		lefttxtfield3 = new JTextField(TEXT_FIELD_SIZE);
		add(lefttxtfield1, WEST);
		add(changestatus = new JButton("Change Status"), WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		add(lefttxtfield2, WEST);
		add(changepicture = new JButton("Change Picture"), WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		add(lefttxtfield3, WEST);
		add(addfriend = new JButton("Add Friend"), WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		lefttxtfield1.addActionListener(this);
		lefttxtfield2.addActionListener(this);
		lefttxtfield3.addActionListener(this);
	}
}
