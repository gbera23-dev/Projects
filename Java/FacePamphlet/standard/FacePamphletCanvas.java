
/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */

import acm.graphics.*;
import java.awt.*;
import java.util.*;

public class FacePamphletCanvas extends GCanvas implements FacePamphletConstants {
	// GLabels
	private GLabel message;
	private GLabel name;
	private GLabel status;
	// GRect
	private GRect rectimage;

	public FacePamphletCanvas() {
	}

	// This method displays strings on the down side of the panel
	public void showMessage(String msg) {
		if (message != null) {
			remove(message);
		}
		message = new GLabel(msg);
		message.setFont(MESSAGE_FONT);
		message.setLocation(getWidth() / 2 - message.getWidth() / 2, getHeight() - BOTTOM_MESSAGE_MARGIN);
		add(message);
	}

	// This method is responsible for displaying all the profile features on the
	// canvas
	public void displayProfile(FacePamphletProfile profile) {
		if (profile != null) {
			removeAll();
			displayName(profile);
			displayImage(profile);
			displayStatus(profile);
			displayFriends(profile);
			displayImage(profile);
			showMessage("Displaying " + profile.getName());
		} else {
			removeAll();
			showMessage("Please choose the current profile");
		}
	}

	// This method displays name on canvas
	private void displayName(FacePamphletProfile profile) {
		name = new GLabel(profile.getName());
		name.setFont(PROFILE_NAME_FONT);
		name.setColor(Color.BLUE);
		name.setLocation(LEFT_MARGIN, TOP_MARGIN + name.getAscent());
		add(name);
	}

	// This method displays image on canvas
	private void displayImage(FacePamphletProfile profile) {
		rectimage = new GRect(1, 1);
		rectimage.setSize(IMAGE_WIDTH, IMAGE_HEIGHT);
		rectimage.setLocation(LEFT_MARGIN, name.getY() + IMAGE_MARGIN);
		add(rectimage);
		if (profile.getImage() == null) {
			GLabel noImage = new GLabel("No Image");
			noImage.setFont(PROFILE_IMAGE_FONT);
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
		if (profile.getStatus().equals("")) {
			status = new GLabel("No current status");
			status.setFont(PROFILE_STATUS_FONT);
			status.setLocation(LEFT_MARGIN,
					rectimage.getY() + STATUS_MARGIN + rectimage.getHeight() + status.getAscent());
			add(status);
		} else {
			status = new GLabel(profile.getName() + " is" + " " + profile.getStatus());
			status.setFont(PROFILE_STATUS_FONT);
			status.setLocation(LEFT_MARGIN,
					rectimage.getY() + STATUS_MARGIN + rectimage.getHeight() + status.getAscent());
			add(status);
		}
	}

	// This method displays friend list on canvas
	private void displayFriends(FacePamphletProfile profile) {
		int k = 0;
		Iterator<String> it = profile.getFriends();
		GLabel friends = new GLabel("Friends:");
		friends.setFont(PROFILE_FRIEND_LABEL_FONT);
		friends.setLocation(getWidth() / 2, rectimage.getY());
		add(friends);
		while (it.hasNext()) {
			k++;
			GLabel friend = new GLabel((String) it.next());
			friend.setFont(PROFILE_FRIEND_FONT);
			friend.setLocation(friends.getX(), friends.getY() + friends.getAscent() * k);
			add(friend);
		}
	}
}
