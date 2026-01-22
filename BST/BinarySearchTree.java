//BST Implementation Project 1
//

public class BinarySearchTree<T extends Comparable<T>> implements SortedCollection<T>{

	protected BinaryNode<T> root;

	public BinarySearchTree() {
		root = null; //initialize root as nothing 
	}

	public void insert(T data) throws NullPointerException{
		if (data == null){
			throw new NullPointerException("Can't insert null");
		}

		BinaryNode<T> new_node = new BinaryNode<>(data);

		if (root == null){
			root = new_node;
			return;
		}

		BinaryNode<T> curr = root;

		while (curr != null) {

			if (data.compareTo(curr.getData()) <= 0){
				if (curr.getLeft() == null){
					curr.setLeft(new_node);
					new_node.setUp(curr);
					return;
				}
				else{
					curr = curr.getLeft();
			}
			}
			else {
				if (curr.getRight() == null){
					curr.setRight(new_node);
					new_node.setUp(curr);
					return;
					}
				else{
					curr = curr.getRight();
				}
		}


		}
	}

	public boolean contains(Comparable<T> find){
		return false;
	}

	public int size(){
		return 0;
	}

	public boolean isEmpty(){
		return false;
	}

	public void clear(){
	}
	public static void test(){
		BinarySearchTree<Integer> tree = new BinarySearchTree<>();
		System.out.println("Insert first element");
		tree.insert(5);
		tree.insert(6);
		tree.insert(2);
		tree.insert(10);
		System.out.println("Tree structure (level-order):");
		System.out.println(tree.root.toLevelOrderString());

	}

	public static void main(String[] args){
		test();
	}
}

