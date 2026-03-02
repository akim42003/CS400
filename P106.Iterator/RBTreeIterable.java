
/**
 *Alexander Kim
 *3/4/2026
 * Iterator
 */
import java.util.Iterator;
import java.util.Stack;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;

/**
 * This class extends RedBlackTree into a tree that supports iterating over the
 * values it
 * stores in sorted, ascending order.
 */
public class RBTreeIterable<T extends Comparable<T>>
		extends RedBlackTree<T> implements IterableSortedCollection<T> {

	private Comparable<T> min = null;
	private Comparable<T> max = null;

	/**
	 * Allows setting the start (minimum) value of the iterator. When this method is
	 * called,
	 * every iterator created after it will use the minimum set by this method until
	 * this method
	 * is called again to set a new minimum value.
	 *
	 * @param min the minimum for iterators created for this tree, or null for no
	 *            minimum
	 */
	public void setIteratorMin(Comparable<T> min) {
		this.min = min;
	}

	/**
	 * Allows setting the stop (maximum) value of the iterator. When this method is
	 * called,
	 * every iterator created after it will use the maximum set by this method until
	 * this method
	 * is called again to set a new maximum value.
	 *
	 * @param max the maximum for iterators created for this tree, or null for no
	 *            maximum
	 */
	public void setIteratorMax(Comparable<T> max) {
		this.max = max;
	}

	/**
	 * Returns an iterator over the values stored in this tree. The iterator uses
	 * the
	 * start (minimum) value set by a previous call to setIteratorMin, and the stop
	 * (maximum)
	 * value set by a previous call to setIteratorMax. If setIteratorMin has not
	 * been called
	 * before, or if it was called with a null argument, the iterator uses no
	 * minimum value
	 * and starts with the lowest value that exists in the tree. If setIteratorMax
	 * has not been
	 * called before, or if it was called with a null argument, the iterator uses no
	 * maximum
	 * value and finishes with the highest value that exists in the tree.
	 */
	public Iterator<T> iterator() {
		return null;
	}

	/**
	 * Nested class for Iterator objects created for this tree and returned by the
	 * iterator method.
	 * This iterator follows an in-order traversal of the tree and returns the
	 * values in sorted,
	 * ascending order.
	 */
	protected static class TreeIterator<R extends Comparable<R>> implements Iterator<R> {

		// stores the start point (minimum) for the iterator
		Comparable<R> min = null;
		// stores the stop point (maximum) for the iterator
		Comparable<R> max = null;
		// stores the stack that keeps track of the inorder traversal
		Stack<BinaryNode<R>> stack = null;

		/**
		 * Constructor for a new iterator if the tree with root as its root node, and
		 * min as the start (minimum) value (or null if no start value) and max as the
		 * stop (maximum) value (or null if no stop value) of the new iterator.
		 * Time complexity should be O(log n).
		 *
		 * @param root root node of the tree to traverse
		 * @param min  the minimum value that the iterator will return
		 * @param max  the maximum value that the iterator will return
		 */
		public TreeIterator(BinaryNode<R> root, Comparable<R> min, Comparable<R> max) {
			this.min = min;
			this.max = max;
			this.stack = new Stack<>();
			updateStack(root);
		}

		/**
		 * Helper method for initializing and updating the stack. This method both
		 * - finds the next data value stored in the tree (or subtree) that is between
		 * start(minimum) and stop(maximum) point (including start and stop points
		 * themselves), and
		 * - builds up the stack of ancestor nodes that contain values between
		 * start(minimum) and stop(maximum) values (including start and stop values
		 * themselves) so that those nodes can be visited in the future.
		 *
		 * @param node the root node of the subtree to process
		 */
		private void updateStack(BinaryNode<R> node) {

			if (node == null) {
				return;
			}

			// case where min is declared and node argument < min so we skip and go right
			// since the tree is is monotonically increasing going right
			if (min != null && min.compareTo(node.data) > 0) {
				updateStack(node.right);
			}
			// node value >= min so push it onto stack and look for smaller children
			else {
				stack.push(node);
				updateStack(node.left);
			}
		}

		/**
		 * Returns true if the iterator has another value to return, and false
		 * otherwise.
		 */
		public boolean hasNext() {
			if (stack.isEmpty()) {
				return false;
			}
			// handles the max bound from updateStack since updateStack is called in next
			if (max != null && max.compareTo(stack.peek().data) < 0) {
				return false;
			}

			// above cases check if there's nothing else to return
			return true;
		}

		/**
		 * Returns the next value of the iterator.
		 * Amortized time complexity should be O(1).
		 * Worst case time complexity should be O(log n).
		 * Do not implement this method by linearly walking through the
		 * entire tree from the smallest element until the start bound is reached.
		 * That process should occur only once during construction of the
		 * iterator object.
		 *
		 * @throws NoSuchElementException if the iterator has no more values to return
		 */
		public R next() {
			// handles the exception when iterator has nothing left to return
			if (!hasNext()) {
				throw new NoSuchElementException("Iterator has no more values to return");
			}

			// clear from stack as node is iterated over and update stack
			BinaryNode<R> current = stack.pop();
			updateStack(current.right);
			// return data of popped off node
			return current.data;
		}

		@Test
		public void test1() {
			RBTreeIterable<Integer> tree = new RBTreeIterable<>();

		}

		@Test
		public void test2() {

			RBTreeIterable<String> tree = new RBTreeIterable<>();
		}

		@Test
		public void test3() {
			RBTreeIterable<Integer> tree = new RBTreeIterable<>();
		}
	}

}
