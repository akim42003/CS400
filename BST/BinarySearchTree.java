
//BST Implementation Project 1
//
import java.util.LinkedList;
import java.util.Queue;

public class BinarySearchTree<T extends Comparable<T>> implements SortedCollection<T> {

	protected BinaryNode<T> root;

	public BinarySearchTree() {
		root = null; // initialize root as nothing
	}

	public void insert(T data) throws NullPointerException {
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

			if (data.compareTo(curr.getData()) <= 0) {
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

		BinaryNode<T> curr = root;

		while (curr != null) {
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
		// Easiest thing might be BFS and count

		int size = sizeHelper(root);

		return size;
	}

	private int sizeHelper(BinaryNode<T> node) {
		if (node == null) {
			return 0;
		}
		int count = 1 + sizeHelper(node.getLeft()) + sizeHelper(node.getRight()); // init at 1 assumes root
												// isn't null
		//
		return count;
	}

	public boolean isEmpty() {

		if (root != null) {
			return false;
		} else {
			return true;
		}
	}

	public void clear() {
		root = null;
	}

	public boolean test1() {
		boolean pass = true;

		// Insert elements into tree
		this.insert((T) Integer.valueOf(5));
		this.insert((T) Integer.valueOf(6));
		this.insert((T) Integer.valueOf(2));
		this.insert((T) Integer.valueOf(10));

		boolean contain_test = this.contains((T) Integer.valueOf(2));
		pass = contain_test;

		pass = !this.isEmpty();

		return pass;

	}

	public boolean test2() {
		boolean pass = true;

		this.insert((T) "Alex");
		this.insert((T) "Ben");
		this.insert((T) "Darin");
		this.insert((T) "Arnav");
		this.insert((T) "Louis");

		boolean contain_test = this.contains((T) "Darin");
		pass = contain_test;

		pass = !this.isEmpty();
		return pass;
	}

	public boolean test3() {
		boolean pass = true;
		this.clear();
		pass = this.isEmpty();

		if (!pass) {
			return false;
		}

		return true;

	}

	public static void main(String[] args) {

		BinarySearchTree<Integer> num_tree = new BinarySearchTree<>();

		System.out.println(num_tree.test1());

		BinarySearchTree<String> string_tree = new BinarySearchTree<>();

		System.out.println(string_tree.test2());

		System.out.println(num_tree.test3());

	}
}
