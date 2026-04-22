import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOError;
import java.io.IOException;
import java.util.List;

public class TeamTests {

	@Test
	public void testShortestPathAllLocations() throws IOException {

		Graph_Placeholder graph = new Graph_Placeholder();
		Backend backend = new Backend(graph);
		backend.loadGraphData("./europeanRail.dot");
		Frontend frontend = new Frontend(backend);

		// frontend builds response HTML for a known path
		String html = frontend.generateShortestPathResponseHTML("Paris",
				"Berlin");

		List<String> expectedPath = backend.findLocationsOnShortestPath("Paris",
				"Berlin");

		for (String location : expectedPath) {
			assertTrue(html.contains(location), "HTML shoud have backend location: " + location);
		}
	}

	@Test
	public void testShortestPathCorrectTotalTime() throws IOException {
		Graph_Placeholder graph = new Graph_Placeholder();
		Backend backend = new Backend(graph);
		backend.loadGraphData("europeanRail.dot");
		Frontend frontend = new Frontend(backend);

		List<Double> times = backend.findTimesOnShortestPath("Paris",
				"Berlin");
		double expectedTotal = 0;

		for (Double t : times) {
			expectedTotal += t;
		}

		String html = frontend.generateShortestPathResponseHTML("Paris",
				"Berlin");

		assertTrue(html.contains(String.valueOf(expectedTotal)),
				"HTML should contain matching sum of backend tiems: " + expectedTotal);

	}

	@Test
	public void testClosestFromAllResponse() throws IOException {
		Graph_Placeholder graph = new Graph_Placeholder();
		Backend backend = new Backend(graph);
		backend.loadGraphData("europeanRail.dot");
		Frontend frontend = new Frontend(backend);

		String starts = "Paris, Berlin";

		List<String> startList = List.of("Paris", "Berlin");
		String expectedClosest = backend.getClosestLocationFromAll(startList);

		String html = frontend.generateClosestLocationsFromAllResponseHTML(starts);

		assertTrue(html.contains(expectedClosest), "HTML should include: " + expectedClosest);

	}
}
