import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 * Tests for the frontend class using Graph_Placeholder and Backend_Placeholder 
 */
public class FrontendTests {

	/**
	 * Tests Frontend generateShortestPathPromptHTML function for correct ids in the
	 * HTML fragment
	 * and button with label "Find Shortest Path"
	 */
	@Test
	public void roleTest1() {
		// initialize objects for frontend
		Graph_Placeholder graph = new Graph_Placeholder();
		Backend_Placeholder backend = new Backend_Placeholder(graph);
		Frontend frontend = new Frontend(backend);

		String html = frontend.generateShortestPathPromptHTML();

		// verify input ids
		assertTrue(html.contains("id=\"start\""));

		assertTrue(html.contains("id=\"end\""));

		// check for button label
		assertTrue(html.contains("Find Shortest Path"));
	}

	/*
	 * Checks that generateShortestPathPromptHTML gives the correct HTML for a valid
	 * path
	 */
	@Test
	public void roleTest2() {
		Graph_Placeholder graph = new Graph_Placeholder();
		Backend_Placeholder backend = new Backend_Placeholder(graph);
		Frontend frontend = new Frontend(backend);

		// declare start and end locations
		String start = "Union South";
		String end = "Weeks Hall for Geological Sciences";

		// get shortest path response with previously defined start and end
		String html = frontend.generateShortestPathResponseHTML(start, end);

		// check for containment of start and end locations
		assertTrue(html.contains(start));
		assertTrue(html.contains(end));

		assertTrue(html.contains("<ol>"));

		// check for intermediate location and cumulative length
		assertTrue(html.contains("Computer Sciences and Statistics"));
		assertTrue(html.contains("6.0"));

	}

	/*
	 * Verifies that generateClosestLocationsFromAllPromptHTML contains the requried
	 * ids and a "Closest From All" button
	 * returning HTML with an unordered list of start locations and closest location
	 */
	@Test
	public void roleTest3() {
		Graph_Placeholder graph = new Graph_Placeholder();
		Backend_Placeholder backend = new Backend_Placeholder(graph);
		Frontend frontend = new Frontend(backend);

		// test prompt HTML
		String prompt = frontend.generateClosestLocationsFromAllPromptHTML();
		assertTrue(prompt.contains("id=\"from\""), "Prompt HTML should contain an input with from id");

		assertTrue(prompt.contains("Closest From All"));

		// test response
		String starts = "Union South, Computer Sciences and Statistics";
		String response = frontend.generateClosestLocationsFromAllResponseHTML(starts);

		assertTrue(response.contains("<ul>"));
		assertTrue(response.contains("Union South"));
		assertTrue(response.contains("Computer Sciences and Statistics"));
		assertTrue(response.contains("Weeks Hall for Geological Sciences"));

	}
}
