//BST Implementation Project 1
//
import java.util.LinkedList;
import java.util.Queue;

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

		BinaryNode<T> curr = root;

		while (curr != null){
			if (find.compareTo(curr.getData())< 0){
				curr = curr.getLeft(); //comparison says go left
			}
			else if (find.compareTo(curr.getData()) > 0){
				curr = curr.getRight(); //alt inequality
			}
			else{
				return true;
			}
		}

		return false; //curr has reached end of tree
	}

	public int size(){
		// Easiest thing might be BFS and count

		int size = sizeHelper(root);
		
		return size;
	}

	private int sizeHelper(BinaryNode<T> node){
		if (node == null){
			return 0; 
		}
		int count = 1 + sizeHelper(node.getLeft()) + sizeHelper(node.getRight()); //init at 1 assumes root isn't null 
		//
		return count;
	}

	public boolean isEmpty(){
		
		if (root != null){
			return false;
		}
		else {
			return true;
		}
	}

	public void clear(){
		root = null;	
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
		
		boolean contain_test = tree.contains(2);
		System.out.println("Tree contains 2 test:");
		System.out.println(contain_test);
		
		System.out.println("Tree empty test:");
		System.out.println(tree.isEmpty());

		int size = tree.size();
		System.out.println("Tree size is:" + tree.size());
		System.out.println("Clearing");	
		tree.clear();
		System.out.println("Size after clear:" + tree.size());
	}

	public static void main(String[] args){
		test();
	}
}

