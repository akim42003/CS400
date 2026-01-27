
/*
 * Alexander Kim
 * CS400 Project 1
* 1/28/1016
*/
import java.util.LinkedList;
import java.util.Queue;

public class BinarySearchTree<T extends Comparable<T>> implements SortedCollection<T> {
	// Binary Search Tree Class for Project 1
	protected BinaryNode<T> root;

	// Protected root field referencing the root BinaryNode of a given BST
	public BinarySearchTree() {
		root = null; // initialize root as nothing
	}

	public void insert(T data) throws NullPointerException {
		/*
		 * insert(T data) throws the above exception when inserting null since the
		 * default reference of BinaryNode is null.
		 */
		if (data == null) {
			throw new NullPointerException("Can't insert null");
		}

		BinaryNode<T> new_node = new BinaryNode<>(data);

		if (root == null) {
			root = new_node;
			return;
		}

		BinaryNode<T> curr = root;

		while (curr != null) {
			// perform iterative DFS to place curr node
			if (data.compareTo(curr.getData()) <= 0) {
				// compare left first, else go right
				if (curr.getLeft() == null) {
					curr.setLeft(new_node);
					new_node.setUp(curr);
					return;
				} else {
					curr = curr.getLeft();
				}
			} else {
				if (curr.getRight() == null) {
					curr.setRight(new_node);
					new_node.setUp(curr);
					return;
				} else {
					curr = curr.getRight();
				}
			}

		}
	}

	public boolean contains(Comparable<T> find) {
		/*
		 * returns a bool true if find is in the tree
		 * returns false otherwise
		 */
		BinaryNode<T> curr = root;

		while (curr != null) {
			// iterative DFS
			if (find.compareTo(curr.getData()) < 0) {
				curr = curr.getLeft(); // comparison says go left
			} else if (find.compareTo(curr.getData()) > 0) {
				curr = curr.getRight(); // alt inequality
			} else {
				return true;
			}
		}

		return false; // curr has reached end of tree
	}

	public int size() {
		// recursive implementation using sizeHelper initialized at root

		int size = sizeHelper(root);

		return size;
	}

	private int sizeHelper(BinaryNode<T> node) {
		if (node == null) {
			return 0;
		}
		int count = 1 + sizeHelper(node.getLeft()) + sizeHelper(node.getRight()); // count assumes root exists
												// and recursively
												// accumulates 1 through
												// each
		// left and right subtree
		return count;
	}

	public boolean isEmpty() {
		// returns a bool true if any nodes exist
		if (root != null) {
			return false;
		} else {
			return true;
		}
	}

	public void clear() {
		// clears BST by making root node null which automatically dereferences
		// all children
		root = null;
	}

	public boolean test1() {
		/*
		 * This test uses an unbalanced tree of integers and tests multiple levels
		 * of insert starting with root and also tests contain method of a leaf.
		 * It also tests contains on nonexistent entries.
		 */
		boolean pass = true;

		// Insert elements into tree
		this.insert((T) Integer.valueOf(5));
		this.insert((T) Integer.valueOf(6));
		this.insert((T) Integer.valueOf(2));
		this.insert((T) Integer.valueOf(10));

		boolean contain_test = this.contains((T) Integer.valueOf(2));
		pass = contain_test;

		pass = !this.isEmpty();

		boolean non_existent = this.contains((T) Integer.valueOf(20));
		pass = !non_existent;

		return pass;

	}

	public boolean test2() {
		/*
		 * This method uses a tree shape where root has only a right subtree and
		 * tests the contain method with string elements on both root and an
		 * intermediate node.
		 */
		boolean pass = true;

		this.insert((T) "Alex");
		this.insert((T) "Ben");
		this.insert((T) "Darin");
		this.insert((T) "Arnav");
		this.insert((T) "Louis");

		boolean contain_test = this.contains((T) "Darin");
		pass = contain_test;

		boolean contain_test2 = this.contains((T) "Alex");
		pass = contain_test2;

		pass = !this.isEmpty();
		return pass;
	}

	public boolean test3() {
		/*
		 * test3 does not depend on the element type of the BST and simply tests
		 * if the clear and isEmpty functions work by first clearing the tree
		 * and then returning a bool if it is empty (true) or not (false).
		 */
		boolean pass = true;
		this.clear();
		pass = this.isEmpty();

		if (!pass) {
			return false;
		}

		return true;

	}

	public boolean test4() {
		/*
		 * test 4 tests duplicates and size
		 */

		this.insert((T) Integer.valueOf(7));
		this.insert((T) Integer.valueOf(4));
		this.insert((T) Integer.valueOf(10));
		this.insert((T) Integer.valueOf(12));
		this.insert((T) Integer.valueOf(5));
		this.insert((T) Integer.valueOf(2));
		this.insert((T) Integer.valueOf(11));
		this.insert((T) Integer.valueOf(10));

		int size = this.size();

		if (size == 8) {
			return true;
			// true if the function inserts duplicate
		}

		return false;
	}

	public static void main(String[] args) {
		// main function to test integer and string trees
		BinarySearchTree<Integer> num_tree = new BinarySearchTree<>();

		System.out.println(num_tree.test1());

		BinarySearchTree<String> string_tree = new BinarySearchTree<>();

		System.out.println(string_tree.test2());

		System.out.println(num_tree.test3());

		System.out.println(num_tree.test4());

	}
}
