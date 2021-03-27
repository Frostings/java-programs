import java.util.*;

interface List<T> {
    void insert(T value);
    void print();
}

public class Tree<T extends Comparable> implements List<T>{
    private Node<T> root;
    
    private class Node<T> {
        T value;
        Node<T> left;
        Node<T> right;
        
        Node(T value, Node<T> left, Node<T> right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }
        
        public void print() {
            print("", true);
        }
        
        private void print(String prefix, boolean last) {
    	    System.out.println(prefix + (last ? "┖╴" : "┠╴") + value);
    	    if (left != null) {
    		    left.print(prefix + (last ? "  " : "┃ "), right == null);
    	    } if (right != null) {
    		    right.print(prefix + (last ? "  " : "┃ "), true);
    	    }
        }
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
    
    private void insert(Node<T> node, T value) {
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
    
    public static void main(String[] args) {
		ArrayList<Integer> list = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
		Collections.shuffle(list);
		Tree<Integer> tree = new Tree<Integer>(list);
		tree.print();
	}
}
