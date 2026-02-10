
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

	public Backend(IterableSortedCollection<GameRecord> tree) {
		this.tree = tree;
		this.currentLow = null;
		this.currentHigh = null;
		this.filterTime = null;
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
		currentHigh = high;
		currentLow = low;

		if (low != null) {
			tree.setIteratorMin(new GameRecord("", GameRecord.Continent.AFRICA, low, 0, 0, "000:00:00"));
		} else {
			tree.setIteratorMin(null);
		}
		if (high != null) {
			tree.setIteratorMax(new GameRecord("", GameRecord.Continent.AFRICA, high, 0, 0, "000:00:00"));
		} else {
			tree.setIteratorMax(null);
		}

		List<String> rangeNames = new ArrayList<>();
		Iterator<GameRecord> tree_it = tree.iterator();
		while (tree_it.hasNext()) {
			GameRecord record = tree_it.next();
			if (filterTime == null || compareTime(record.getCompletionTime(), filterTime) < 0) {

				rangeNames.add(record.getName());
			}
		}

		return rangeNames;
	}

	public List<String> applyAndSetFilter(String time) {

		List<String> filterNames = new ArrayList<>();
		this.filterTime = time;

		filterNames = getAndSetRange(currentLow, currentHigh);
		return filterNames;

	}

	private int compareTime(String time, String recordTime) {
		String[] targetTime = time.split(":");
		int targetHours = Integer.parseInt(targetTime[0]);
		int targetMins = Integer.parseInt(targetTime[1]);
		int targetSecs = Integer.parseInt(targetTime[2]);

		String[] recordSplit = recordTime.split(":");
		int recordHours = Integer.parseInt(recordSplit[0]);
		int recordMins = Integer.parseInt(recordSplit[1]);
		int recordSecs = Integer.parseInt(recordSplit[2]);

		if (targetHours != recordHours) {
			return targetHours - recordHours;
		} else if (targetMins != recordMins) {
			return targetMins - recordMins;
		}
		return targetSecs - recordSecs;

	}

	public List<String> getTopTen() {
		List<String> topTenNames = new ArrayList<>();
		List<GameRecord> validRecords = new ArrayList<>();

		List<String> filterNames = applyAndSetFilter(filterTime);

		for (GameRecord record : tree) {
			if (filterNames.contains(record.getName())) {
				validRecords.add(record);
			}
		}

		for (int i = 0; i < 10 && !validRecords.isEmpty(); i++) {
			int minIdx = 0;
			for (int j = 1; j < validRecords.size(); j++) {
				if (validRecords.get(j).getDamageTaken() < validRecords.get(minIdx).getDamageTaken()) {
					minIdx = j;
				}
			}
			topTenNames.add(validRecords.get(minIdx).getName());
			validRecords.remove(minIdx);
		}

		return topTenNames;
	}

}
