import java.util.*;

interface List<T> {
	void insert(T value);
	int size();
	T get(int index);
	void print();
}

public class Tree<T extends Comparable> implements List<T>{
	private Node<T> root;
	
	private class Node<T> {
		T value;
		Node<T> left;
		Node<T> right;
		int size;
		
		Node(T value, Node<T> left, Node<T> right) {
			this.value = value;
			this.left = left;
			this.right = right;
			size = 1;
			if (left != null) size += left.size;
			if (right != null) size += right.size;
		}
		
		public void print() {
			print("", true);
		}
		
		private void print(String prefix, boolean last) {
			System.out.println(prefix + (last ? "┖╴" : "┠╴") + value + ", " + size);
			if (left != null) {
				left.print(prefix + (last ? "  " : "┃ "), right == null);
			}
			if (right != null) {
				right.print(prefix + (last ? "	" : "┃ "), true);
			}
		}
	}
	
	Tree() {
		root = null;
	}
	
	Tree(ArrayList<T> list) {
		for (T item: list) {
			insert(item);
		}
	}
	
	public void insert(T value) {
		if (root == null) {
			root = new Node<T>(value, null, null);
		} else {
			insert(root, value);
		}
	}
	
	public void print() {
		root.print();
	}
	
	public int size() {
		return root == null? 0 : root.size;
	}
	
	public T get(int index){
		if (root == null || index < 0 || index >= size()) {
			return null;
		}
		return get(index, root);
	}
	
	private void insert(Node<T> node, T value) {
		node.size++;
		if (node.value.compareTo(value) > 0) {
			if (node.left == null) {
				node.left = new Node<T>(value, null, null);
			} else {
				insert(node.left, value);
			}
		} else {
			if (node.right == null) {
				node.right = new Node<T>(value, null, null);
			} else {
				insert(node.right, value);
			}
		}
	}
	
	private T get(int index, Node<T> node){
		if(node.left == null && index == 0 || node.left != null && node.left.size == index){
			return node.value;
		}
		if(node.left != null && index < node.left.size) {
			return get(index, node.left);
		}
		return get(index - (node.size - node.right.size), node.right);
	}
	
	public static void main(String[] args) {
		ArrayList<Integer> list = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
		Collections.shuffle(list);
		Tree<Integer> tree = new Tree<Integer>(list);
		tree.print();
		for (int i = 0; i <= 12; i++) {
			System.out.println(tree.get(i));
		}
	}
}
