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

		GameRecord record = new GameRecord("player1", GameRecord.Continent.ASIA, 45000, 50, 100, "100:00:00");
		backend.addRecord(record);

		assertNotNull(tree.lastAddedGameRecord);

		List<String> testNames = backend.getAndSetRange(40000, 48000);
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

		try {
			backend.readData("records.csv");
		} catch (Exception e) {
			fail("readData threw an exception:");
		}

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

		backend.getAndSetRange(null, null);
		List<String> filtered = backend.applyAndSetFilter("700:00:00");
		assertTrue(filtered.contains("v0idt3mp0"));
		assertTrue(filtered.contains("speedRoyalty"));
		assertFalse(filtered.contains("xXxgamer47xXx"));

		List<String> topTen = backend.getTopTen();
		assertEquals(2, topTen.size());
		assertEquals("speedRoyalty", topTen.get(0));
		assertEquals("v0idt3mp0", topTen.get(1));

	}
}
