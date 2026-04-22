import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class HashTableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {
	private int capacity;
	private int size = 0;
	private List<LinkedList<Pair>> table;

	public HashTableMap(int Capacity) {
		capacity = Capacity;

		table = new LinkedList<>();

		for (int i = 0; i < capacity; i++) {
			table.add(new LinkedList<>());
		}

	}

	protected class Pair {
		public KeyType key;
		public ValueType value;

		public Pair(KeyType key, ValueType value) {
			this.key = key;
			this.value = value;
		}

		public KeyType getKey() {
			return this.key;
		}

		public ValueType getValue() {
			return this.value;
		}
	}

	/**
	 * Adds a new key,value pair/mapping to this collection.
	 * 
	 * @param key   the key of the key,value pair
	 * @param value the value that key maps to (may be null)
	 * @throws IllegalArgumentException if key already maps to a value without
	 *                                  making any changes to the table
	 * @throws NullPointerException     if key is null
	 */
	public void put(KeyType key, ValueType value) throws IllegalArgumentException {
		if (key == null) {
			throw new NullPointerException();
		}

		Pair currPair = new Pair(key, value);

		int hashKey = Math.abs(key.hashCode()) % capacity;

		LinkedList<Pair> bucket = table.get(hashKey);

		for (Pair p : bucket) {

			if (p.getKey().equals(key)) {
				throw new IllegalArgumentException();
			}
		}
		bucket.add(currPair);

		size += 1;

		if ((double) size / capacity >= 0.75) {
			this.reSize();
		}

	}

	/*
	 * resize helper function for when the ratio of size to capacity exceeds 0.75
	 */
	private void reSize() {
		List<LinkedList<Pair>> oldTable = table;

		capacity = 2 * capacity;

		List<LinkedList<Pair>> newTable = new LinkedList<>();

		for (int i = 0; i < capacity; i++) {
			newTable.add(new LinkedList<>());
		}
		table = newTable;

		size = 0;

		for (LinkedList<Pair> bucket : oldTable) {
			for (Pair p : bucket) {
				put(p.getKey(), p.getValue());

			}
		}

	}

	/**
	 * Checks whether a key maps to a value in this collection.
	 * 
	 * @param key the key to check
	 * @throws NullPointerException if key is null
	 * @return true if the key maps to a value, and false is the key doesn't
	 *         map to a value
	 */
	public boolean containsKey(KeyType key) {
		if (key == null) {
			throw new NullPointerException();
		}

		int hashKey = Math.abs(key.hashCode()) % capacity;
		LinkedList<Pair> bucket = table.get(hashKey);

		for (Pair p : bucket) {

			if (p.getKey().equals(key)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Retrieves the specific value that a key maps to.
	 * 
	 * @param key the key to look up
	 * @return the value that key maps to
	 * @throws NoSuchElementException when key is not stored in this collection
	 * @throws NullPointerException   if key is null
	 */
	public ValueType get(KeyType key) throws NoSuchElementException {
		if (key == null) {
			throw new NullPointerException();
		}

		int hashKey = Math.abs(key.hashCode()) % capacity;
		LinkedList<Pair> bucket = table.get(hashKey);

		for (Pair p : bucket) {

			if (p.getKey().equals(key)) {
				return p.getValue();
			}
		}

		throw new NoSuchElementException();

	}

	/**
	 * Remove the mapping for a key from this collection.
	 * 
	 * @param key the key whose mapping to remove
	 * @return the value that the removed key mapped to
	 * @throws NoSuchElementException when key is not stored in this collection
	 * @throws NullPointerException   if key is null
	 */
	public ValueType remove(KeyType key) throws NoSuchElementException {
		if (key == null) {
			throw new NullPointerException();
		}

		int hashKey = Math.abs(key.hashCode()) % capacity;
		LinkedList<Pair> bucket = table.get(hashKey);

		Pair toRemove = null;
		for (Pair p : bucket) {

			if (p.getKey().equals(key)) {
				toRemove = p;
			}
		}

		if (toRemove != null) {
			bucket.remove(toRemove);
			size--;

			return toRemove.getValue();
		}

		throw new NoSuchElementException();
	}

	/**
	 * Removes all key,value pairs from this collection without changing the
	 * capacity of the underlying array.
	 */
	public void clear() {
		for (int i = 0; i < capacity; i++) {
			table.set(i, new LinkedList<>());
		}

		size = 0;
	}

	/**
	 * Retrieves the number of keys stored in this collection.
	 * 
	 * @return the number of keys stored in this collection
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Retrieves this collection's capacity.
	 * 
	 * @return the size of the underlying array for this collection
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Retrieves this collection's keys.
	 * 
	 * @return a list of keys in the underlying array for this collection
	 */
	public List<KeyType> getKeys() {
		List<KeyType> KeyList = new LinkedList<>();

		for (LinkedList<Pair> bucket : table) {
			for (Pair p : bucket) {
				KeyList.add(p.getKey());
			}

		}
		return KeyList;
	}

	/**
	 * Tests basic put and get functionality: after inserting several key-value
	 * pairs, each key should retrieve its corresponding value, the size should
	 * reflect the number of insertions, and containsKey should return true for
	 * inserted keys and false for absent keys.
	 */
	@Test
	public void testPutAndGet() {
		// Create a map with small capacity and insert three pairs
		HashTableMap<String, Integer> map = new HashTableMap<>(10);
		map.put("apple", 1);
		map.put("banana", 2);
		map.put("cherry", 3);

		// Verify each key retrieves the correct value
		Assertions.assertEquals(1, map.get("apple"));
		Assertions.assertEquals(2, map.get("banana"));
		Assertions.assertEquals(3, map.get("cherry"));

		// Verify size reflects the three insertions
		Assertions.assertEquals(3, map.getSize());

		// Verify containsKey works for both present and absent keys
		Assertions.assertTrue(map.containsKey("apple"));
		Assertions.assertFalse(map.containsKey("durian"));
	}

	/**
	 * Tests the remove method: removing a key should return its value, decrement
	 * the size, and cause subsequent containsKey calls to return false. Attempting
	 * to get a removed key should throw NoSuchElementException.
	 */
	@Test
	public void testRemoveAndContains() {
		// Set up a map with two pairs
		HashTableMap<String, Integer> map = new HashTableMap<>(10);
		map.put("x", 100);
		map.put("y", 200);

		// Remove one key and verify the returned value is correct
		Integer removed = map.remove("x");
		Assertions.assertEquals(100, removed);

		// Verify size decreased and the removed key is no longer present
		Assertions.assertEquals(1, map.getSize());
		Assertions.assertFalse(map.containsKey("x"));

		// Verify that getting a removed key now throws NoSuchElementException
		Assertions.assertThrows(NoSuchElementException.class, () -> map.get("x"));

		// Verify the other key is still intact
		Assertions.assertEquals(200, map.get("y"));
	}

	/**
	 * Tests that a resize operation is triggered when the load factor reaches
	 * the 0.75 threshold. With a starting capacity of 4, inserting the 3rd key
	 * should push the load factor to 0.75 and double the capacity to 8.
	 */
	@Test
	public void testResizeTriggered() {
		// Start with capacity 4 so that 3 insertions trigger resize (3/4 = 0.75)
		HashTableMap<Integer, String> map = new HashTableMap<>(4);

		// Insert first two pairs — capacity should remain 4
		map.put(1, "one");
		map.put(2, "two");
		Assertions.assertEquals(4, map.getCapacity());

		// Third insertion pushes load factor to 0.75, triggering a resize to 8
		map.put(3, "three");
		Assertions.assertEquals(8, map.getCapacity());

		// Size should still be 3 after resize (no data lost)
		Assertions.assertEquals(3, map.getSize());
	}

	/**
	 * Tests that rehashing during resize correctly relocates all existing
	 * key-value pairs so they remain retrievable at their new bucket indices.
	 * After forcing a resize, every previously inserted key must still return
	 * its original value.
	 */
	@Test
	public void testRehashingAfterResize() {
		// Start with small capacity to force a resize quickly
		HashTableMap<Integer, String> map = new HashTableMap<>(4);

		// Insert enough pairs to trigger at least one resize
		map.put(10, "ten");
		map.put(20, "twenty");
		map.put(30, "thirty");
		map.put(40, "forty");
		map.put(50, "fifty");

		// Confirm capacity has grown beyond initial value, meaning resize happened
		Assertions.assertTrue(map.getCapacity() > 4);

		// Every key must still map to its original value after rehashing
		Assertions.assertEquals("ten", map.get(10));
		Assertions.assertEquals("twenty", map.get(20));
		Assertions.assertEquals("thirty", map.get(30));
		Assertions.assertEquals("forty", map.get(40));
		Assertions.assertEquals("fifty", map.get(50));

		// Size should equal the number of insertions, not double-counted from rehash
		Assertions.assertEquals(5, map.getSize());
	}

	/**
	 * Tests clear and getKeys: getKeys should return all inserted keys before
	 * clearing, and after calling clear, the map should be empty but retain
	 * its capacity. getKeys on a cleared map should return an empty list.
	 */
	@Test
	public void testClearAndGetKeys() {
		// Insert three pairs into a map with capacity 10
		HashTableMap<String, Integer> map = new HashTableMap<>(10);
		map.put("a", 1);
		map.put("b", 2);
		map.put("c", 3);

		// getKeys should return a list containing all three keys
		List<String> keys = map.getKeys();
		Assertions.assertEquals(3, keys.size());
		Assertions.assertTrue(keys.contains("a"));
		Assertions.assertTrue(keys.contains("b"));
		Assertions.assertTrue(keys.contains("c"));

		// Record capacity before clearing to verify it's preserved
		int capacityBefore = map.getCapacity();

		// Clear the map — all pairs should be removed but capacity unchanged
		map.clear();
		Assertions.assertEquals(0, map.getSize());
		Assertions.assertEquals(capacityBefore, map.getCapacity());

		// After clearing, getKeys should return an empty list
		Assertions.assertEquals(0, map.getKeys().size());
	}

}
