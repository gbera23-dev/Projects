
/*
 * File: FacePamphletProfile.java
 * ------------------------------
 * This class keeps track of all the information for one profile
 * in the FacePamphlet social network.  Each profile contains a
 * name, an image (which may not always be set), a status (what 
 * the person is currently doing, which may not always be set),
 * and a list of friends.
 */

import acm.graphics.*;
import java.util.*;

public class FacePamphletProfile implements FacePamphletConstants {
	private ArrayList<String> listoffriends;
	private Iterator<String> friendsiterator;
	private String profname;
	private String profstatus;
	private GImage profimage;
	private String profpass;
	private Boolean profaccesebility;

	// This method initializes ArrayList and profile name
	public FacePamphletProfile(String name) {
		profname = name;
		listoffriends = new ArrayList<String>();
	}

	// This method returns the name associated with the profile.
	public String getName() {
		return profname;
	}

	// This method returns the profile image
	public GImage getImage() {
		return profimage;
	}

	// This method sets the image associated with the profile.
	public void setImage(GImage image) {
		profimage = image;
	}

	// This method sets the password of the user
	public void setprofpass(String pass) {
		profpass = pass;
	}

	// This method allows us to know the password of the user
	public String getprofpass() {
		return profpass;
	}

	// This method sets the accessibility of the user
	public void setAccesebility(boolean accesebility) {
		profaccesebility = accesebility;
	}

	// This method allows us to know whether user is online or not
	public boolean getAccesebility() {
		return profaccesebility;
	}

	// This method allows us to get status of the profile
	public String getStatus() {
		if (profstatus == null) {
			return "";
		} else {
			return profstatus;
		}
	}

	// This method sets the status associated with the profile.
	public void setStatus(String status) {
		profstatus = status;
	}

	// This method adds friend to the friend list of some particular profile
	public boolean addFriend(String friend) {
		if (!listoffriends.contains(friend)) {
			listoffriends.add(friend);
			return true;
		} else {
			return false;
		}
	}

	// This method removes the friend from the friend list of some particular
	// profile
	public boolean removeFriend(String friend) {
		if (listoffriends.contains(friend)) {
			listoffriends.remove(friend);
			return true;
		} else {
			return false;
		}
	}

	// This method returns an iterator over the list of friends associated with
	// the profile
	public Iterator<String> getFriends() {
		friendsiterator = listoffriends.iterator();
		return friendsiterator;
	}

	// This method returns the string representation of the whole profile
	public String toString() {
		String profile = "";
		profile = profname + " (" + getStatus() + "): " + listoffriends;
		profile = profile.replace("[", "");
		profile = profile.replace("]", "");
		return profile;
	}
}
