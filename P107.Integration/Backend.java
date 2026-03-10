
/*
 * Alexander Kim
 * 2/11/2026
 * Project 3 CS400
*/
import java.util.*;
import java.io.*;

public class Backend implements BackendInterface {

	// global tree implementation for GameRecord objects using
	// IterableSortedCollection to handle sorting and filtering by score
	//
	private IterableSortedCollection<GameRecord> tree;

	// global integers for getAndSetRange function
	private Integer currentLow;
	private Integer currentHigh;

	// global string for filterTime in applyAndSetFilter function
	private String filterTime;

	// constructor of Backend object with initial variables and tree
	public Backend(IterableSortedCollection<GameRecord> tree) {
		this.tree = tree;
		this.currentLow = null;
		this.currentHigh = null;
		this.filterTime = null;
	}

	// uses tree.insert to add a record in BST according to score
	public void addRecord(GameRecord record) {
		tree.insert(record);

	}

	// parses csv and calls addRecord to add new data to Backend
	public void readData(String filename) throws IOException {
		// Create a new BufferReader from FileReader to improve speed
		BufferedReader br = new BufferedReader(new FileReader(filename));

		// read in columns from first line of CSV
		String headerLine = br.readLine();
		// split by commas
		String[] header_entries = headerLine.split(",");
		// declaration of column indices for each header element
		int nameIdx = -1, continentIdx = -1, scoreIdx = -1, max_healthIdx = -1, damage_takenIdx = -1,
				damage_givenIdx = -1, collectablesIdx = -1, levelIdx = -1, completion_timeIdx = -1;
		// loop through header row and set column indices based on string comparison
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
			if (nameIdx == -1 || continentIdx == -1 || scoreIdx == -1 || damage_takenIdx == -1 ||
						damage_givenIdx == -1 || completion_timeIdx == -1) {
					br.close();
					throw new IOException("CSV is missing one more required columns");
						}
		String entry;
		// 1. loop through each row and create a new game record
		// 2. use addRecord to add new data to Backend
		while ((entry = br.readLine()) != null) {
			try {
				String[] elements = entry.split(",");
			GameRecord record = new GameRecord(elements[nameIdx],
					GameRecord.Continent.valueOf(elements[continentIdx]),
					Integer.parseInt(elements[scoreIdx]),
					Integer.parseInt(elements[damage_takenIdx]),
					Integer.parseInt(elements[damage_givenIdx]),
					elements[completion_timeIdx]);
			this.addRecord(record);
			} catch (Exception e){
				//skip rows with missing columns, bad values, etc.
			}

		}
		// close buffer
		br.close();
	}

	// Takes low and high as integer parameters to return names of records within
	// the specified range
	public List<String> getAndSetRange(Integer low, Integer high) {
		// set global range variables
		currentHigh = high;
		currentLow = low;

		// if low is passed, create a dummy record for setIteratorMin
		// to include records with a score above low
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

		// create list that will hold the names of records in the range
		List<String> rangeNames = new ArrayList<>();
		// create iterator to include records if and only if they are in tree range
		Iterator<GameRecord> tree_it = tree.iterator();
		// parse iterator and add records to rangeNames
		while (tree_it.hasNext()) {
			GameRecord record = tree_it.next();
			// filterTime check for applyAndSetFilter function
			// uses compareTime to check if competion time is less than filterTime
			if (filterTime == null || compareTime(filterTime, record.getCompletionTime()) < 0) {

				rangeNames.add(record.getName());
			}
		}

		return rangeNames;
	}

	// Takes a string for completion time and calls getAndSetRange to get names of
	// records
	// within the global specified range and completion time less than specified
	public List<String> applyAndSetFilter(String time) {

		List<String> filterNames = new ArrayList<>();
		this.filterTime = time;
		// call getAndSetRange after setting global filterTime
		filterNames = getAndSetRange(currentLow, currentHigh);
		return filterNames;

	}

	// helper function for getAndSetRange that compares time from records in Backend
	// with global filterTime by hours, then minutes, then seconds and returns the
	// difference as an integer.
	// returns zero if the two times are equal or positive integer if recordTime is greater than time
	private int compareTime(String time, String recordTime) {
		try{
		// split time by colon as shown in csv
		String[] targetTime = time.split(":");
		// create integers for hours, minutes, seconds and set their times from
		// targetTime
		int targetHours = Integer.parseInt(targetTime[0]);
		int targetMins = Integer.parseInt(targetTime[1]);
		int targetSecs = Integer.parseInt(targetTime[2]);

		String[] recordSplit = recordTime.split(":");
		int recordHours = Integer.parseInt(recordSplit[0]);
		int recordMins = Integer.parseInt(recordSplit[1]);
		int recordSecs = Integer.parseInt(recordSplit[2]);
		// check difference in hours, then minutes if hours are the same, then seconds
		// if minute are the same
		if (targetHours != recordHours) {
			return recordHours - targetHours;
		} else if (targetMins != recordMins) {
			return recordMins - targetMins;
		}
		return recordSecs - targetSecs;
		} catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
			System.err.println("could not parse string: " + e.getMessage());
			return 0;
		}

	}

	// returns a string of the top ten record names with fewest damage taken.
	public List<String> getTopTen() {
		// list of record names to be returned
		List<String> topTenNames = new ArrayList<>();
		// list of records that fall under global range and filterTime constraints
		List<GameRecord> validRecords = new ArrayList<>();

		// call filterNames to get initial list of record names within global range and
		// filterTime
		List<String> filterNames = applyAndSetFilter(filterTime);
		Set<String> filterSet = new HashSet<>(filterNames);

		// add records to validRecords if their name is in the filterNames list
		for (GameRecord record : tree) {
			if (filterSet.contains(record.getName())) {
				//TKANESHIRO's filter set recommendation
				validRecords.add(record);
			}
		}
		// count up to 10 while there are valid records and append to list of
		// topTenNames //
		for (int i = 0; i < 10 && !validRecords.isEmpty(); i++) {
			int minIdx = 0;
			// find index of record in validRecords with min damage taken
			for (int j = 1; j < validRecords.size(); j++) {
				if (validRecords.get(j).getDamageTaken() < validRecords.get(minIdx).getDamageTaken()) {
					minIdx = j;
				}
			}
			// add the name of the record with min damage taken to return list
			// of top ten names
			topTenNames.add(validRecords.get(minIdx).getName());
			// remove record whose name was just added to prevent sampling with replacement
			validRecords.remove(minIdx);
		}

		return topTenNames;
	}

}
