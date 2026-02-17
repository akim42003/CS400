/*
 * Alexander Kim
 * Project 4
 * CS400
 */

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RedBlackTree<T extends Comparable<T>> extends BSTRotation<T> {

	/*
	 * Checks if a new red node in the RBT causes a red property violation
	 * by having a red parent. If this is not the case, the method terminates
	 * without making any changes. Else, the method repairs this violation and
	 * any additional violations generated as a result of initial repair.
	 * 
	 * @param newNode an inserted red node, or a node turned red by previous repair
	 */
	protected void ensureRedProperty(RedBlackNode<T> newNode) {
		if (newNode.getUp() == null) {
			// inserted node is root
			return;
		}

		RedBlackNode<T> parent = newNode.getUp();
		if (parent.isBlackNode == true) {
			// case 1
			return;
		}
		RedBlackNode<T> grandParent = parent.getUp();
		if (grandParent == null) {
			// parent is root
			return;
		}

		RedBlackNode<T> aunt;

		if (grandParent.getRight() == parent) {
			// find aunt node if parent is right child of grandparent
			aunt = grandParent.getLeft();
		} else {
			// alt case where parent is left child of grandparent
			aunt = grandParent.getRight();
		}

		boolean isAuntBlack = false;
		// bool to check if trinode restructuring is necessary
		if (aunt == null || aunt.isBlackNode) {
			// treat null as a "black" node
			isAuntBlack = true;
		}

		if (isAuntBlack) {
			// case 2a and 2b with rotation

			if (grandParent.getLeft() == parent && parent.getLeft() == newNode) {
				// parent-grandparent have left child relationship, parent and inserted node
				// also left child
				// right rotate
				this.rotate(parent, grandParent);
				// perform color swaps
				parent.flipColor();
				grandParent.flipColor();
			} else if (grandParent.getLeft() == parent && parent.getRight() == newNode) {
				// left rotate parent and newNode, then right rotate newNode and grandParent
				// parent-grandparent have left child relationship, parent and inserted node are
				// right child
				// requiring double rotation
				this.rotate(newNode, parent);
				this.rotate(newNode, grandParent);

				// color swaps
				newNode.flipColor();
				grandParent.flipColor();
			} else if (grandParent.getRight() == parent && parent.getRight() == newNode) {
				// left rotate parent and grandParent
				// symmetric to first if statement
				this.rotate(parent, grandParent);

				parent.flipColor();
				grandParent.flipColor();
			} else {
				// case with grandParent-parent right, parent-newNode left
				// right rotate newNode and parent, then left rotate newNode and grandParent

				this.rotate(newNode, parent);
				this.rotate(newNode, grandParent);

				newNode.flipColor();
				grandParent.flipColor();
			}

		} else {
			// case 3 recolor if aunt is red
			parent.flipColor();
			grandParent.flipColor();
			aunt.flipColor();
			// recursively check for red-black property violations starting with grandParent
			// since that's the highest node manipulated
			ensureRedProperty(grandParent);
		}

	}

	/*
	 * takes comparable data and inserts a new RedBlackNode with the data while
	 * calling ensureRedProperty to ensure
	 * validity of red black tree
	 * 
	 * @param comparable data integer or string
	 */
	@Override
	public void insert(T data) throws NullPointerException {
		// overrided insert method from BinarySearchTree
		RedBlackNode<T> newEntry = new RedBlackNode<>(data);

		if (this.root == null) {
			// if the root is null, we just return since otherwise there
			// would be duplicates
			this.root = newEntry;
			((RedBlackNode<T>) this.root).isBlackNode = true;
			return;
		}
		if (newEntry.isBlackNode == true) {
			// ensure inserted node is red to start
			newEntry.flipColor();
		}
		// call helper function from BinarySearchTree
		insertHelper(newEntry, this.root);
		// check and fix red-black violations
		ensureRedProperty(newEntry);
		// update root and ensure root is black
		RedBlackNode<T> redBlackRoot = (RedBlackNode<T>) this.root;
		redBlackRoot.isBlackNode = true;

	}

	/**
	 * tests the red aunt case where inserting 3 causes a red property violation.
	 * since aunt 20 is red, a recoloring is triggered
	 */
	@Test
	public void redAuntTest() {
		// test with red aunt
		RedBlackTree<Integer> numTree = new RedBlackTree<>();

		numTree.insert(10);

		numTree.insert(20);

		numTree.insert(5);

		numTree.insert(3);

		// check with level order traversal
		assertEquals("[ 10.b, 5.b, 20.b, 3.r ]", numTree.root.toLevelOrderString());

	}

	/**
	 * tests insertion of R from problem 2 on the quiz which requires red aunt
	 * recoloring and black parent cases.
	 */
	@Test
	public void fromQuizTest() {
		// tree from problem 2 on the quiz
		// black parent upon R insert so do nothing
		RedBlackTree<String> stringTree = new RedBlackTree<>();
		stringTree.insert("M");
		stringTree.insert("E");
		stringTree.insert("U");
		stringTree.insert("C");
		stringTree.insert("G");
		stringTree.insert("Q");
		stringTree.insert("X");
		stringTree.insert("V");
		stringTree.insert("Y");
		stringTree.insert("R");
		// check with level order traversal
		assertEquals("[ M.b, E.b, U.r, C.r, G.r, Q.b, X.b, R.r, V.r, Y.r ]",
				stringTree.root.toLevelOrderString());

	}

	/**
	 * tests black aunt case where inserting 4 creates a zig-zag shape that requires
	 * a double rotation to fix
	 */
	@Test
	public void testBlackAunt() {
		// case where aunt node is black
		RedBlackTree<Integer> numTree2 = new RedBlackTree<>();

		numTree2.insert(10);
		numTree2.insert(5);
		numTree2.insert(15);
		numTree2.insert(3);
		numTree2.insert(4);

		// check with level order traversal
		assertEquals("[ 10.b, 4.b, 15.b, 3.r, 5.r ]", numTree2.root.toLevelOrderString());

	}

	/**
	 * tests multiple recolors because inserting 1 causes a red aunt recolor that
	 * requires further recoloring
	 * higher up the tree
	 */
	@Test
	public void testCascadingRecolor() {
		// Red aunt recolor propagates a violation upward
		RedBlackTree<Integer> numTree3 = new RedBlackTree<>();
		numTree3.insert(10);
		numTree3.insert(5);
		numTree3.insert(15);
		numTree3.insert(3);
		numTree3.insert(7);
		numTree3.insert(12);
		numTree3.insert(20);
		// recolor that cascades up
		numTree3.insert(1);
		// check with level order traversal
		assertEquals("[ 10.b, 5.r, 15.b, 3.b, 7.b, 12.r, 20.r, 1.r ]",
				numTree3.root.toLevelOrderString());
	}

}
