
/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */

import acm.graphics.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.*;

public class FacePamphletCanvas extends GCanvas implements FacePamphletConstants, ComponentListener {
	// GLabels
	private GLabel message;
	private GLabel name;
	private GLabel status;
	// GRect
	private GRect rectimage;
	// GOval
	private GOval ovalaccess;
	// profile
	private FacePamphletProfile prof;
	// doubles
	double width = 603;
	// message
	String msg1;

	public FacePamphletCanvas() {
		addComponentListener(this);
	}

	// This method displays strings on the down side of the panel
	public void showMessage(String msg) {
		int i = (int) ((getWidth() / width) * 18);
		msg1 = msg;
		if (message != null) {
			remove(message);
		}
		message = new GLabel(msg);
		message.setFont("Dialog-" + i);
		message.setLocation(getWidth() / 2 - message.getWidth() / 2, getHeight() - BOTTOM_MESSAGE_MARGIN);
		add(message);
	}

	// This method is responsible for displaying all the profile features on the
	// canvas
	public void displayProfile(FacePamphletProfile profile) {
		prof = profile;
		if (profile != null) {
			removeAll();
			GImage image = new GImage("backGround.jpg");
			image.setLocation(0, 0);
			image.setSize(getWidth(), getHeight());
			add(image);
			displayName(profile);
			displayImage(profile);
			displayStatus(profile);
			displayFriends(profile);
			displayImage(profile);
			displayAccesebility(profile);
			if (message == null) {
				showMessage("Displaying " + profile.getName());
			} else {
				showMessage(msg1);
			}
		} else {
			removeAll();
			GImage image = new GImage("backGround.jpg");
			image.setLocation(0, 0);
			image.setSize(getWidth(), getHeight());
			add(image);
			if (message == null) {
				showMessage("Please choose the current profile");
			} else {
				showMessage(msg1);
			}
		}
	}

	// This method updates the screen for resizing
	public void update() {
		removeAll();
		displayProfile(prof);
	}

	// This method displays name on canvas
	private void displayName(FacePamphletProfile profile) {
		int i = (int) ((getWidth() / width) * 24);
		name = new GLabel(profile.getName());
		name.setFont("Dialog-" + i);
		name.setColor(Color.white);
		name.setLocation(LEFT_MARGIN, TOP_MARGIN + name.getAscent());
		add(name);
	}

	// This method displays image on canvas
	private void displayImage(FacePamphletProfile profile) {
		int i = (int) ((getWidth() / width) * 24);
		rectimage = new GRect(1, 1);
		rectimage.setSize((getWidth() / width) * IMAGE_WIDTH, (getWidth() / width) * IMAGE_HEIGHT);
		rectimage.setLocation(LEFT_MARGIN, name.getY() + IMAGE_MARGIN);
		add(rectimage);
		if (profile.getImage() == null) {
			GLabel noImage = new GLabel("No Image");
			noImage.setFont("Dialog-" + i);
			double x = rectimage.getX() + rectimage.getWidth() / 2 - noImage.getWidth() / 2;
			double y = rectimage.getY() + rectimage.getHeight() / 2 + noImage.getAscent() / 2;
			noImage.setLocation(x, y);
			add(noImage);
		} else {
			GImage image = profile.getImage();
			image.setLocation(rectimage.getX(), rectimage.getY());
			image.setSize(rectimage.getSize());
			add(image);
			remove(rectimage);
		}
	}

	// This method displays status on canvas
	private void displayStatus(FacePamphletProfile profile) {
		int i = (int) ((getWidth() / width) * 16);
		if (profile.getStatus().equals("")) {
			status = new GLabel("No current status");
			status.setColor(Color.red);
			status.setFont("Dialog-" + i + "-bold");
			status.setLocation(LEFT_MARGIN,
					rectimage.getY() + STATUS_MARGIN + rectimage.getHeight() + status.getAscent());
			add(status);
		} else {
			status = new GLabel(profile.getStatus());
			status.setColor(Color.red);
			status.setFont("Dialog-" + i + "-bold");
			status.setLocation(LEFT_MARGIN,
					rectimage.getY() + STATUS_MARGIN + rectimage.getHeight() + status.getAscent());
			add(status);
		}
	}

	// This method displays friend list on canvas
	private void displayFriends(FacePamphletProfile profile) {
		int i = (int) ((getWidth() / width) * 16);
		int k = 0;
		Iterator<String> it = profile.getFriends();
		GLabel friends = new GLabel("Friends:");
		friends.setFont("Dialog-" + i + "-bold");
		friends.setLocation(getWidth() / 2, rectimage.getY());
		add(friends);
		while (it.hasNext()) {
			k++;
			GLabel friend = new GLabel((String) it.next());
			friend.setFont("Dialog-" + i);
			friend.setLocation(friends.getX(), friends.getY() + friends.getAscent() * k);
			add(friend);
		}
	}

	// This method shows red or green circle at the down - right corner of the
	// picture and tells us whether user is online or not
	private void displayAccesebility(FacePamphletProfile profile) {
		double i = (getWidth() / width);
		ovalaccess = new GOval(1, 1);
		ovalaccess.setSize(i * 20, i * 20);
		ovalaccess.setLocation(rectimage.getX() + rectimage.getWidth() - ovalaccess.getWidth() / 2,
				rectimage.getY() + rectimage.getHeight() - ovalaccess.getHeight() / 2);
		if (profile.getAccesebility()) {
			ovalaccess.setFillColor(Color.green);
		} else {
			ovalaccess.setFillColor(Color.red);
		}
		ovalaccess.setFilled(true);
		add(ovalaccess);
	}
	//This methods are important for resizing
	@Override
	public void componentHidden(ComponentEvent arg0) {
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		GImage image = new GImage("backGround.jpg");
		image.setLocation(0, 0);
		image.setSize(getWidth(), getHeight());
		add(image);
		if (msg1 != null) {
			update();
		}
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
	}
}
