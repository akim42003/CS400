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
}
