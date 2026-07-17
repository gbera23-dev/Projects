import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

public class NameSurferDataBase implements NameSurferConstants {
	HashMap<String, String> map;
	NameSurferEntry entry;

	// We use the HashMap to store information taken from the file
	public NameSurferDataBase(String filename) {
		map = new HashMap<String, String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			try {
				while (reader.ready()) {
					String line = reader.readLine();
					entry = new NameSurferEntry(line);
					String ranks = entry.getRanks();
					map.put(entry.getName(), ranks);
				}
				reader.close();
			} catch (IOException e) {
			}
		} catch (FileNotFoundException e) {
		}
	}

	/*
	 * Returns the NameSurferEntry associated with this name, if one exists. If
	 * the name does not appear in the database, this method returns null.
	 */
	public NameSurferEntry findEntry(String name) {
		if (map.containsKey(name)) {
			NameSurferEntry entry1 = new NameSurferEntry(name + " " + map.get(name));
			return entry1;
		}
		return null;
	}
}
