import java.util.ArrayList;
import java.util.List;

public class Frontend implements FrontendInterface {

	private BackendInterface backend;

	/**
	 * constructs frontend with the private backend for path retrieval
	 */
	public Frontend(BackendInterface backend) {
		this.backend = backend;
	}

	/**
	 * Returns HTML fragement with id labels and find shortest path button
	 */
	@Override
	public String generateShortestPathPromptHTML() {
		// initial html construction
		String html = "";
		html += "<label for=\"start\">Start Location:</label>\n";
		html += "<input type=\"text\" id=\"start\"/>\n";
		html += "<label for=\"end\">End Location:</label>\n";
		html += "<input type=\"text\" id=\"end\"/>\n";
		html += "<button>Find Shortest Path</button>\n";

		return html;
	}

	/**
	 * Returns an HTML fragment showing the shortest path between the start and end
	 * locations
	 */
	@Override
	public String generateShortestPathResponseHTML(String start, String end) {
		try {
			List<String> path = backend.findLocationsOnShortestPath(start, end);

			List<Double> times = backend.findTimesOnShortestPath(start, end);

			// return null for an empty path
			if (path == null || path.isEmpty()) {
				return "<p>No path found</p>";
			}

			double totalTime = 0;

			// sum up weight of the path
			for (Double t : times) {
				totalTime += t;
			}

			String html = "";

			html += "<p>Path from " + start + " to " + end + ":</p>\n";

			// ordered list of locations
			html += "<ol>\n";

			// iterate and concatenate locations to ordered list
			for (String location : path) {
				html += "<li>" + location + "</li>\n";
			}

			html += "<ol>\n";

			html += "<p>Total time: " + totalTime + " minutes</p>\n";

			return html;
		} catch (Exception e) {
			return "<p>Unable to find path</p>/n";

		}
	}

	/*
	 * returns an HTML fragment with a labeled text input for all entered locations
	 * with input from
	 * a CLosest From All button\
	 */
	@Override
	public String generateClosestLocationsFromAllPromptHTML() {
		String html = "";

		// HTML fragment for entered locations and button
		html += "<label for=\"from\">Start Location List:</label>\n";
		html += "<input type=\"text\" id=\"from\" />\n";
		html += "<button>Closest From All</button>\n";

		return html;
	}

	/*
	 * returns at HTML fragment showing the unordered list of start locations and
	 * closest
	 * reachable locations from all starts with total time traveled
	 */
	@Override
	public String generateClosestLocationsFromAllResponseHTML(String starts) {

		try {
			// Parse comma-separated start locations
			String[] startArray = starts.split(",");
			List<String> startList = new ArrayList<>();
			for (String s : startArray) {
				startList.add(s.trim());
			}

			// get closest location reachable from all starts
			String closest = backend.getClosestLocationFromAll(startList);

			// Calculate total time from all start locations to the closest location by
			// looping over the array and then looping over shortest path times
			double totalTime = 0;
			for (String s : startList) {
				List<Double> times = backend.findTimesOnShortestPath(s, closest);
				for (Double t : times) {
					totalTime += t;
				}
			}

			String html = "";
			// Unordered list of start locations
			html += "<ul>\n";
			// add locations to the unordered list
			for (String s : startList) {
				html += "<li>" + s + "</li>\n";
			}
			html += "</ul>\n";
			html += "<p>Closest location from all starts: " + closest + "</p>\n";
			html += "<p>Total time to all start locations: " + totalTime + " minutes</p>\n";

			return html;
		} catch (Exception e) {
			return "<p>Error: Unable to find closest location</p>\n";
		}
	}

}
