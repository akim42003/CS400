
/*
 * Alexander Kim
 * 2/11/2026
 * Project 3 CS400
*/
import java.util.*;
import java.io.*;

public class Backend implements BackendInterface {

	private IterableSortedCollection<GameRecord> tree;

	private Integer currentLow;
	private Integer currentHigh;

	private String filterTime;

	public Backend(Tree_Placeholder tree) {

	}

	public void addRecord(GameRecord record) {
		tree.insert(record);

	}

	public void readData(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));

		String headerLine = br.readLine();
		String[] header_entries = headerLine.split(",");
		int nameIdx = -1, continentIdx = -1, scoreIdx = -1, max_healthIdx = -1, damage_takenIdx = -1,
				damage_givenIdx = -1, collectablesIdx = -1, levelIdx = -1, completion_timeIdx = -1;

		for (int i = 0; i < header_entries.length; i++) {
			if (header_entries[i].equals("name")) {
				nameIdx = i;
			} else if (header_entries[i].equals("continent")) {
				continentIdx = i;
			} else if (header_entries[i].equals("score")) {
				scoreIdx = i;
			} else if (header_entries[i].equals("max_health")) {
				max_healthIdx = i;
			} else if (header_entries[i].equals("damage_taken")) {
				damage_takenIdx = i;
			} else if (header_entries[i].equals("damage_given")) {
				damage_givenIdx = i;
			} else if (header_entries[i].equals("collectables")) {
				collectablesIdx = i;
			} else if (header_entries[i].equals("level")) {
				levelIdx = i;
			} else if (header_entries[i].equals("completion_time")) {
				completion_timeIdx = i;
			}
		}
		String entry;

		while ((entry = br.readLine()) != null) {
			String[] elements = entry.split(",");
			GameRecord record = new GameRecord(elements[nameIdx],
					GameRecord.Continent.valueOf(elements[continentIdx]),
					Integer.parseInt(elements[scoreIdx]),
					Integer.parseInt(elements[damage_takenIdx]),
					Integer.parseInt(elements[damage_givenIdx]),
					elements[completion_timeIdx]);
			this.addRecord(record);

		}
	}

	public List<String> getAndSetRange(Integer low, Integer high) {

		return new ArrayList<>();
	}

	public List<String> applyAndSetFilter(String time) {

		return new ArrayList<>();

	}

	public List<String> getTopTen() {

		return new ArrayList<>();
	}

}
