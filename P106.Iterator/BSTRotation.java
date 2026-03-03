
/*
 * Alexander Kim
 * Project 2 BST Rotation
 * 2/3/2026
 * CS400
 */

public class BSTRotation<T extends Comparable<T>> extends BinarySearchTree<T> {
	/*
	 * Performs the rotation operation on the provided nodes within this tree. When
	 * the provided child is a left
	 * child of the provided parent, this method will perform a right rotation. When
	 * the provided child is a right
	 * child of the provided parent, this method will perform a left rotation.
	 * 
	 * @param child is the node being rotated from child to parent position.
	 * 
	 * @param parent is the node being rotated from parent to child position.
	 */

	protected void rotate(BinaryNode<T> child, BinaryNode<T> parent) {
		// rotation function that conditionally performs left rotation or right rotation
		// depending on the parent-child relationship
		if (parent.getRight() == child) {
			leftRotate(child, parent);
		} else {
			rightRotate(child, parent);
		}
	}

	private void leftRotate(BinaryNode<T> child, BinaryNode<T> parent) {
		// left rotation function
		//
		BinaryNode<T> grandparent = parent.getUp();

		parent.setRight(child.getLeft());
		if (child.getLeft() != null) {
			child.getLeft().setUp(parent); // update grandchild up ref to parent
		}
		// parent becomes child's left child and parent's parent ref is updated to child
		child.setLeft(parent);
		parent.setUp(child);

		// estabilish grandparent relationship
		child.setUp(grandparent);
		if (grandparent != null) {
			if (grandparent.getLeft() == parent) {
				grandparent.setLeft(child); // determine if new parent is left child then set
			} else {
				grandparent.setRight(child); // alt case for right child of grandparent
			}
		} else {
			root = child; // if grandparent is null then the new parent is just root
		}

	}

	private void rightRotate(BinaryNode<T> child, BinaryNode<T> parent) {
		// right rotation

		BinaryNode<T> grandparent = parent.getUp();

		parent.setLeft(child.getRight());
		if (child.getRight() != null) {
			child.getRight().setUp(parent);
		}

		child.setRight(parent);
		parent.setUp(child);

		child.setUp(grandparent);
		if (grandparent != null) {
			if (grandparent.getRight() == parent) {
				grandparent.setRight(child);
			} else {
				grandparent.setLeft(child);
			}
		} else {
			root = child;
		}
	}

	public boolean test1() {
		// test for int tree with left rotation
		BinaryNode<T> node5 = new BinaryNode<>((T) Integer.valueOf(5));

		BinaryNode<T> node3 = new BinaryNode<>((T) Integer.valueOf(3));
		BinaryNode<T> node7 = new BinaryNode<>((T) Integer.valueOf(7));

		root = node5;
		node5.setLeft(node3);
		node3.setUp(node5);
		node5.setRight(node7);
		node7.setUp(node5);

		// rotate w one shared child node3 child of parent
		this.rotate(node7, node5);

		String expectedLevelOrder = "[ 7, 5, 3 ]";
		String expectedInOrder = "[ 3, 5, 7 ]";

		boolean passLevelOrder = root.toLevelOrderString().equals(expectedLevelOrder);
		boolean passInOrder = root.toInOrderString().equals(expectedInOrder);

		return passLevelOrder && passInOrder;
	}

	public boolean test2() {
		// test for string tree with right rotation and 2 shared children
		BinaryNode<T> node_d = new BinaryNode<>((T) "d");
		BinaryNode<T> node_e = new BinaryNode<>((T) "e");
		BinaryNode<T> node_b = new BinaryNode<>((T) "b");
		BinaryNode<T> node_c = new BinaryNode<>((T) "c");

		root = node_d;
		node_d.setRight(node_e);
		node_e.setUp(node_d);
		node_d.setLeft(node_b);
		node_b.setUp(node_d);
		node_b.setRight(node_c);
		node_c.setUp(node_b);

		// rotate wrt parent node_d and child node_b who have shared children node_c and
		// node_e respectively
		this.rotate(node_b, node_d);

		String expectedLevelOrder = "[ b, d, c, e ]";
		String expectedInOrder = "[ b, c, d, e ]";

		boolean passLevelOrder = root.toLevelOrderString().equals(expectedLevelOrder);
		boolean passInOrder = root.toInOrderString().equals(expectedInOrder);

		return passInOrder && passLevelOrder;
	}

	public boolean test3() {
		// this test tests with 3 shard children in a larger tree
		BinaryNode<T> node10 = new BinaryNode((T) Integer.valueOf(10));
		BinaryNode<T> node5 = new BinaryNode((T) Integer.valueOf(5));
		BinaryNode<T> node15 = new BinaryNode((T) Integer.valueOf(15));
		BinaryNode<T> node3 = new BinaryNode((T) Integer.valueOf(3));
		BinaryNode<T> node7 = new BinaryNode((T) Integer.valueOf(7));
		BinaryNode<T> node6 = new BinaryNode((T) Integer.valueOf(6));
		BinaryNode<T> node8 = new BinaryNode((T) Integer.valueOf(8));

		root = node10;
		node10.setLeft(node5);
		node5.setUp(node10);
		node10.setRight(node15);
		node15.setUp(node10);
		node5.setLeft(node3);
		node3.setUp(node5);
		node5.setRight(node7);
		node7.setUp(node5);
		node7.setLeft(node6);
		node6.setUp(node7);
		node7.setRight(node8);
		node8.setUp(node7);

		this.rotate(node7, node5);

		String expectedInOrder = "[ 3, 5, 6, 7, 8, 10, 15 ]";

		boolean passInOrder = root.toInOrderString().equals(expectedInOrder);

		return passInOrder;
	}

	public boolean test4() {
		// Test with 0 shared children via a minimal tree
		BinaryNode<T> node5 = new BinaryNode<>((T) Integer.valueOf(5));
		BinaryNode<T> node7 = new BinaryNode<>((T) Integer.valueOf(7));

		root = node5;

		node5.setRight(node7); // no other children so 0 shared
		node7.setUp(node5);

		this.rotate(node7, node5);

		boolean rootCorrect = root == node7;
		boolean childCorrect = root.getLeft() == node5;
		boolean parentCorrect = node5.getUp() == node7;

		return rootCorrect && childCorrect && parentCorrect;

	}

	public static void main(String[] args) {

		// main function to test integer and string tree
		BSTRotation<Integer> num_tree = new BSTRotation<>();
		System.out.println(num_tree.test1());

		BSTRotation<String> string_tree = new BSTRotation<>();
		System.out.println(string_tree.test2());

		num_tree.clear();
		System.out.println(num_tree.test3());

		num_tree.clear();
		System.out.println(num_tree.test4());
	}
}
