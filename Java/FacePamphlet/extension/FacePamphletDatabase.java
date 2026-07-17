
/*
 * File: FacePamphletDatabase.java
 * -------------------------------
 * This class keeps track of the profiles of all users in the
 * FacePamphlet application.  Note that profile names are case
 * sensitive, so that "ALICE" and "alice" are NOT the same name.
 */

import java.util.*;

public class FacePamphletDatabase implements FacePamphletConstants {
	HashMap<String, Object> database;

	// This method initializes HashMap used as dataBase
	public FacePamphletDatabase() {
		database = new HashMap<String, Object>();
	}

	// This method adds profile to the dataBase
	public void addProfile(FacePamphletProfile profile) {
		if (database.containsKey(profile.getName())) {
			database.remove(profile.getName());
			database.put(profile.getName(), profile);
		} else {
			database.put(profile.getName(), profile);
		}
	}

	// This method returns the profile(if there is no profile, it returns null)
	public FacePamphletProfile getProfile(String name) {
		if (database.containsKey(name)) {
			return (FacePamphletProfile) database.get(name);
		} else {
			return null;
		}
	}

	// This method takes out profile from database
	public void deleteProfile(String name) {
		if (database.containsKey(name)) {
			database.remove(name);
			Iterator<Object> databasevalues = database.values().iterator();
			while (databasevalues.hasNext()) {
				FacePamphletProfile prof1 = (FacePamphletProfile) databasevalues.next();
				if (hasFriend(prof1, name)) {
					prof1.removeFriend(name);
				}
			}
		}
	}

	// This method checks whether database contains profile or not
	public boolean containsProfile(String name) {
		if (database.containsKey(name)) {
			return true;
		} else {
			return false;
		}
	}

	// This method determines whether particular profile has particular friend
	// in friend list
	public boolean hasFriend(FacePamphletProfile prof, String name) {
		Iterator<String> it = prof.getFriends();
		while (it.hasNext()) {
			String names = it.next();
			if (names.equals(name)) {
				return true;
			}
		}
		return false;
	}
}
