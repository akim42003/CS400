/*
 * Alexander Kim
 * Backend Tests
 */

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class BackendTests {

	/*
	 * Tests addRecord and getAndSetRange
	 */
	@Test
	public void roleTest1() {
		Tree_Placeholder tree = new Tree_Placeholder();

		Backend backend = new Backend(tree);
		// test record
		GameRecord record = new GameRecord("player1", GameRecord.Continent.ASIA, 45000, 50, 100, "100:00:00");
		backend.addRecord(record);
		// check if record was successfully added
		assertNotNull(tree.lastAddedGameRecord);

		// test getAndSetRange
		List<String> testNames = backend.getAndSetRange(40000, 48000);
		// throws an exception if the following names aren't included leading to failure
		assertTrue(testNames.contains("v0idt3mp0"));
		assertTrue(testNames.contains("speedRoyalty"));
		assertFalse(testNames.contains("xXxgamer47xXx"));
	}

	/*
	 * Tests readData with try-catch exception clause
	 */
	@Test
	public void roleTest2() {
		Tree_Placeholder tree = new Tree_Placeholder();
		Backend backend = new Backend(tree);
		// if data cannot be read, fail exception is thrown with message
		try {
			backend.readData("records.csv");
		} catch (Exception e) {
			fail("readData threw an exception:");
		}
		// ensure a record was actually added
		assertNotNull(tree.lastAddedGameRecord);

	}

	/*
	 * Tests applyAndSetFilter and getTopTen by setting a score range
	 * and applying a completion filterTIme and manually checking list of
	 * namesTopTen
	 */
	@Test
	public void roleTest3() {
		Tree_Placeholder tree = new Tree_Placeholder();
		Backend backend = new Backend(tree);
		// reset global range to whole Backend
		backend.getAndSetRange(null, null);
		// set filter for input time
		List<String> filtered = backend.applyAndSetFilter("700:00:00");
		assertTrue(filtered.contains("v0idt3mp0"));
		assertTrue(filtered.contains("speedRoyalty"));
		assertFalse(filtered.contains("xXxgamer47xXx"));
		// check that getTopTen returns correct amount of records with correct names for
		// the given filterTime
		List<String> topTen = backend.getTopTen();
		assertEquals(2, topTen.size());
		assertEquals("speedRoyalty", topTen.get(0));
		assertEquals("v0idt3mp0", topTen.get(1));

	}

	/**
	 * integration test that submits one recurd through Frontend submit command and
	 * verifies its output
	 * using show output from Backend
	 */
	@Test
	public void integrationTest1() {

		InterableSortedCollection<GameRecord> tree = new RBTreeIterable();
		Backend backend = new Backend(tree);
		Scanner scanner = new Scanner("unused");
		Frontend frontend = new Frontend(scanner, backend);

		frontend.processSingleCommand("submit testPlayer ASIA 50000 60 120 100:30:60");

		List<String> results = backend.getAndSetRange(null, null);
		assertTrue(results.contains("testPlayer"),
				"record submitted via frontend should be retrieved from backend");

	}

	/*
	 * Integration test that loads records from records.csv through Frontend submit
	 * multiple and verifies
	 * the output with Backend
	 */
	@Test
	public void integrationTest2() {

		IterableSortedCollection<GameRecord> tree = new RBTreeIterable();
		Backend backend = new Backend(tree);

		backend.readData("records.csv");

		TextUITester tester = new TextUITester("submit multiple records.csv\nshow 100\n quit\n");
		Scanner scanner = new Scanner(System.in);
		Frontend frontend = new Frontend(scanner, backend);

		frontend.runCommandLoop();

		String output = tester.checkOutput();

		assertTrue(output.contains("Records loaded from file"), "output should confirm CSV loaded");
		assertFalse(output.contains("Error"), "errors should not occur unless CSV loaded incorrectly");

	}
	/*
	 * integration test that loads csv and sets a score range through frontend
	 * verifies only records in the range are shown using backend for filtering
	 */

	@Test
	public void integrationTest3() {
		IterableSortedCollection<GameRecord> tree = new RBTreeIterable();
		Backend backend = new Backend(tree);

		backend.readData("records.csv");

		TextUITester tester = new TextUITester("score 49000 to 50000\nshow 100 \nquit \n");
		Scanner scanner = new Scanner(System.in);
		Frontend frontend = new Frontend(scanner, backend);

		frontend.runCommandLoop();

		String output = tester.checkOutput();

		List<String> rangeResults = backend.getAndSetRange(49000, 50000);

		for (String name : rangeResults) {
			asertTrue(output.contains(name),
					"all records in backend range should appear in frontend show output");
		}

	}

	/**
	 * integration test that applies a time filter and uses show least damage to
	 * check if getTopTen returns
	 * the correct results after filtering via backend.
	 */
	@Test
	public void integrationTest4() {
		IterableSortedCollection<GameRecord> tree = new RBTreeIterable();
		Backend backend = new Backend(tree);

		backend.readData("records.csv");

		TextUITester tester = new TextUITester("score 49000 to 50000\nshow 100 \nquit \n");
		Scanner scanner = new Scanner(System.in);
		Frontend frontend = new Frontend(scanner, backend);

		frontend.runCommandLoop();

		String output = tester.checkOutput();

		List<String> topTen = backend.getTopTen();
		assertTrue(topTen.size() <= 10);

		for (String name : topTen) {
			assertTrue(output.contains(name));
		}

	}

}
