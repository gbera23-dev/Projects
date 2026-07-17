
/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */ 
 /* NOTE!!!
  * This is the extension of FacePamphlet program. 
  * Have fun exploring!
  */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.*;
import javax.swing.*;

public class FacePamphlet extends Program implements FacePamphletConstants {
	/* BUTTONS */
	private JButton delete;
	private JButton changestatus;
	private JButton changepicture;
	private JButton addfriend;
	private JButton login;
	private JButton register;
	private JButton logout;
	private JButton searchButton;
	private JButton getBack;
	/* TXTFIELDS */
	private JTextField uppertxtfield1;
	private JTextField uppertxtfield2;
	private JTextField lefttxtfield1;
	private JTextField lefttxtfield2;
	private JTextField lefttxtfield3;
	private JTextField searchtxtfield;
	/* FacePamphletProfile */
	private FacePamphletProfile prof;
	private FacePamphletProfile currentProf;
	/* FacePamphletDatabase */
	private FacePamphletDatabase database;
	/* FacePamphletCanvas */
	private FacePamphletCanvas canvas;
	/* JLabels */
	private JLabel loginlab;
	private JLabel passwordlab;
	private JLabel proflab;

	// This method is responsible for initialization
	public void init() {
		drawNorthSide();
		drawAllWestSide();
		logged(false);
		addActionListeners(this);
		database = new FacePamphletDatabase();
		canvas = new FacePamphletCanvas();
		add(canvas);
	}

	// This method governs all the interactions
	public void actionPerformed(ActionEvent e) {
		/* UPPER INTERACTIONS */
		String uppertxt1 = uppertxtfield1.getText();
		String uppertxt2 = uppertxtfield2.getText();
		String lefttxt1 = lefttxtfield1.getText();
		String lefttxt2 = lefttxtfield2.getText();
		String lefttxt3 = lefttxtfield3.getText();
		String searchtxt = searchtxtfield.getText();
		boolean emptytxt1 = lefttxt1.equals("");
		boolean emptytxt2 = lefttxt2.equals("");
		boolean emptytxt3 = lefttxt3.equals("");
		boolean searchemptytxt = searchtxt.equals("");
		if (e.getSource().equals(register)) {
			registerButtonInteractions(uppertxt1, uppertxt2);
			uppertxtfield1.setText("");
			uppertxtfield2.setText("");
		} else if (e.getSource().equals(login) || e.getSource().equals(uppertxtfield1)
				|| e.getSource().equals(uppertxtfield2)) {
			loginButtonInteractions(uppertxt1, uppertxt2);
			uppertxtfield1.setText("");
			uppertxtfield2.setText("");
		}

		else if (e.getSource().equals(delete)) {
			deleteButtonInteractions();
			uppertxtfield1.setText("");
		} else if ((e.getSource().equals(searchButton) || e.getSource().equals(searchtxtfield)) && !searchemptytxt) {
			searchButtonInteractions(searchtxt);
			searchtxtfield.setText("");
		}
		/* LEFT INTERACTIONS */
		else if ((e.getSource().equals(changestatus) || e.getSource().equals(lefttxtfield1)) && !emptytxt1) {
			changeStButtonInteractions(lefttxt1);
			lefttxtfield1.setText("");
		} else if ((e.getSource().equals(changepicture) || e.getSource().equals(lefttxtfield2)) && !emptytxt2) {
			changeImButtonInteractions(lefttxt2);
			lefttxtfield2.setText("");
		} else if ((e.getSource().equals(addfriend) || e.getSource().equals(lefttxtfield3)) && !emptytxt3) {
			addFrButtonInteractions(lefttxt3);
			lefttxtfield3.setText("");
		} else if (e.getSource().equals(logout)) {
			canvas.displayProfile(null);
			logged(false);
			canvas.showMessage("you have logged out of your profile");
			currentProf = null;
		} else if (e.getSource().equals(getBack)) {
			getBack.setVisible(false);
			getBackButtonInteractions();
		}
	}

	// This method adds addFriend button interactions
	private void addFrButtonInteractions(String lefttxt3) {
		FacePamphletProfile friendsFriend = database.getProfile(lefttxt3);
		if (currentProf != null) {
			getBack.setVisible(false);
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
		}
	}

	// This method is responsible for changeImage Button interactions
	private void changeImButtonInteractions(String lefttxt2) {
		if (currentProf == null) {
			canvas.displayProfile(currentProf);
			canvas.showMessage("Please login to change picture");
		} else {
			getBack.setVisible(false);
			GImage image = null;
			try {
				image = new GImage(lefttxt2);
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
			getBack.setVisible(false);
			canvas.showMessage("Status updated to " + currentProf.getStatus());
		} else {
			canvas.displayProfile(currentProf);
			canvas.showMessage("Please login to change status");
		}
	}

	// This method is responsible for lookUp Button interactions
	private void searchButtonInteractions(String uppertxt) {
		prof = database.getProfile(uppertxt);
		if (prof != currentProf) {
			if (prof != null) {
				prof.setAccesebility(false);
				canvas.displayProfile(prof);
				getBack.setVisible(true);
				canvas.showMessage("Showing the profile of " + prof.getName());
			} else {
				canvas.showMessage("profile with name " + uppertxt + " does not exist");
			}
		} else {
			canvas.showMessage("you cannot search yourself");
		}
	}

	// This method is responsible for showing particular buttons when user is
	// signed in or not
	private void logged(Boolean bool) {
		lefttxtfield1.setVisible(bool);
		lefttxtfield2.setVisible(bool);
		lefttxtfield3.setVisible(bool);
		searchtxtfield.setVisible(bool);
		proflab.setVisible(bool);
		searchtxtfield.setVisible(bool);
		logout.setVisible(bool);
		searchButton.setVisible(bool);
		addfriend.setVisible(bool);
		changepicture.setVisible(bool);
		changestatus.setVisible(bool);
		delete.setVisible(bool);
		login.setVisible(!bool);
		register.setVisible(!bool);
		uppertxtfield1.setVisible(!bool);
		uppertxtfield2.setVisible(!bool);
		loginlab.setVisible(!bool);
		passwordlab.setVisible(!bool);
	}

	// This method is responsible for delete button interactions
	private void deleteButtonInteractions() {
		if (currentProf != null) {
			database.deleteProfile(currentProf.getName());
			currentProf = null;
			canvas.displayProfile(currentProf);
			logged(false);
			getBack.setVisible(false);
			canvas.showMessage("your profile has been deleted");
		} else {
			canvas.showMessage("you need to be logged in to delete your account");
		}
	}

	// This method is responsible for add button interactions
	private void registerButtonInteractions(String uppertxt1, String uppertxt2) {
		if (uppertxt1.equals("") || uppertxt2.equals("")) {
			canvas.showMessage("no profile name or password");
		} else {
			prof = database.getProfile(uppertxt1);
			if (prof == null) {
				prof = new FacePamphletProfile(uppertxt1);
				database.addProfile(prof);
				prof.setprofpass(uppertxt2);
				currentProf = prof;
				currentProf.setAccesebility(true);
				canvas.displayProfile(currentProf);
				logged(true);
				proflab.setText("LOGGED IN AS: " + currentProf.getName());
				canvas.showMessage(prof.getName() + " Welcome to FacePamphlet!");
			} else {
				canvas.showMessage("profile with this name already exists");
			}
		}
	}

	// This method is responsible for login button interactions
	private void loginButtonInteractions(String uppertxt1, String uppertxt2) {
		if (uppertxt1.equals("") || uppertxt2.equals("")) {
			canvas.showMessage("no profile name or password");
		} else {
			prof = database.getProfile(uppertxt1);
			if (prof == null) {
				canvas.showMessage("Such profile does not exist");
			} else if (prof != null && (prof.getprofpass().equals(uppertxt2))) {
				proflab.setText("LOGGED IN AS: " + prof.getName());
				logged(true);
				prof.setAccesebility(true);
				canvas.showMessage("Welcome again " + prof.getName() + "!");
				currentProf = prof;
				canvas.displayProfile(currentProf);
			} else if (!prof.getprofpass().equals(uppertxt2) && prof != null) {
				canvas.showMessage("That is the wrong password. please, try again");
			}
		}
	}

	// This method is responsible for getBack button interactions
	private void getBackButtonInteractions() {
		canvas.displayProfile(currentProf);
		canvas.showMessage("You are back!");
	}

	// This method adds texts everything on the NORTH side
	private void drawNorthSide() {
		uppertxtfield1 = new JTextField(TEXT_FIELD_SIZE);
		add(loginlab = new JLabel("Name"), NORTH);
		add(uppertxtfield1, NORTH);
		uppertxtfield2 = new JTextField(TEXT_FIELD_SIZE);
		add(passwordlab = new JLabel("Password"), NORTH);
		add(uppertxtfield2, NORTH);
		uppertxtfield1.addActionListener(this);
		uppertxtfield2.addActionListener(this);
		addNorthButtons();
	}

	// This method adds buttons to NORTH side
	private void addNorthButtons() {
		add(login = new JButton("Login"), NORTH);
		add(register = new JButton("Register"), NORTH);
		add(delete = new JButton("Delete"), NORTH);
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
		add(proflab = new JLabel("LOGGED IN AS: "), WEST);
		add(searchtxtfield = new JTextField(TEXT_FIELD_SIZE), WEST);
		add(searchButton = new JButton("Search"), WEST);
		add(getBack = new JButton("Get back"), WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		getBack.setVisible(false);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		add(logout = new JButton("Log out"), WEST);
		lefttxtfield1.addActionListener(this);
		lefttxtfield2.addActionListener(this);
		lefttxtfield3.addActionListener(this);
		searchtxtfield.addActionListener(this);
	}
}
