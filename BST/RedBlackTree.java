/*
 * Alexander Kim
 * Project 4
 * CS400
 */

public class RedBlackTree<T extends Comparable<T>> extends BSTRotation<T> {

	/*
	 * Checks if a new red node in the RBT causes a red property violation
	 * by having a red parent. If this is not the case, the method terminates
	 * without making any cahnges. Else, the method reparis this violation and
	 * any additional violations generated as a result of initial repair.
	 * 
	 * @param newNode an inserted red node, or a node turned red by previous repair
	 */
	protected void ensureRedProperty(RedBlackNode<T> newNode) {
		if (newNode.getUp() == null) {
			return;
		}

		RedBlackNode<T> parent = newNode.getUp();
		if (parent.isBlackNode == true) {
			// case 1
			return;
		}
		RedBlackNode<T> grandParent = parent.getUp();
		if (grandParent == null) {
			return;
		}

		RedBlackNode<T> aunt;

		if (grandParent.getRight() == parent) {
			aunt = grandParent.getLeft();
		} else {
			aunt = grandParent.getRight();
		}

		boolean isAuntBlack = true;
		if (aunt.isBlackNode == false) {
			isAuntBlack = false;
		}

		if (isAuntBlack) {
			// case 2a and 2b with rotation

		} else {
			// case 3 recolor
		}

	}

	@Override
	public void insert(T data) throws NullPointerException {
		RedBlackNode<T> newEntry = new RedBlackNode<>(data);

		if (this.root == null) {
			// if the root is null, we just return since otherwise there
			// would be duplicates
			this.root = newEntry;
			((RedBlackNode<T>) this.root).isBlackNode = true;
			return;
		}
		if (newEntry.isBlackNode == true) {
			newEntry.flipColor();
		}

		insertHelper(newEntry, this.root);

		ensureRedProperty(newEntry);

		RedBlackNode<T> redBlackRoot = (RedBlackNode<T>) this.root;
		redBlackRoot.isBlackNode = true;

	}
}
