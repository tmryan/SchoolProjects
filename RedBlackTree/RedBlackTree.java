package sjsu.ryan.cs146.project2;

/**
 * Red-Black Tree class using generic data types. Contains
 * an inner RBTreeNode class for storing data, which 
 * implements Comparable for comparing objects. This means
 * that the chosen data type of an object from this class
 * must implement Comparable as well.
 * 
 * @author Thomas Ryan
 *
 */

public class RedBlackTree<T> {
	private RBTreeNode<T> root;
	
	public RedBlackTree() {
		root = null;
	}

	public RBTreeNode<T> insert(T data) {
		// New red node
		RBTreeNode<T> node = new RBTreeNode<T>(false);
		RBTreeNode<T> prev = null;		
		
		node.data = data;
		
		if(root == null) {
			root = node;
		} else {
			prev = search(root, data);

			if(prev.compareTo(data) < 0) {
				if(prev.right != null) {	
					prev.right.prev = node;
					prev.right = node;
				} else {
					prev.right = node;
				}
			} else if(prev.compareTo(data) > 0) {
				if(prev.left != null) {
					prev.left.prev = node;
					prev.left = node;					
				} else {
					prev.left = node;
				}
			}
			
			node.prev = prev;
		}
		
		fixTree(node);
		
		return node;
	}

	public RBTreeNode<T> getSuccessor(RBTreeNode<T> node) {
		RBTreeNode<T> successor = node;
	
		if(node != null && node.right != null) {
			successor = node.right;
			
			while(successor.left != null) {
				successor = successor.left;
			}
		}
		
		return successor;
	}
	
	public RBTreeNode<T> getPredecessor(RBTreeNode<T> node) {
		RBTreeNode<T> predecessor = node;
		
		if(node != null && node.left != null) {
			predecessor = node.left;
			
			while(predecessor.right != null) {
				predecessor = predecessor.right;
			}
		}
	 
		return predecessor;
	}
	
	// Prints elements of tree in preorder
	public void printTree(RBTreeNode<T> node) {
		if(node != null) {
			System.out.println(node.data);
			printTree(node.left);
			printTree(node.right);
		}
	}
	
	public void inorderPrint(RBTreeNode<T> node) {
		if(node != null) {
			inorderPrint(node.left);
			System.out.println(node.data);
			inorderPrint(node.right);
		}
	}
	
	public void postorderPrint(RBTreeNode<T> node) {
		if(node != null) {
			postorderPrint(node.left);
			postorderPrint(node.right);
			System.out.println(node.data);
		}
	}
	
	// Searches for a node with key data
	public RBTreeNode<T> search(RBTreeNode<T> node, T data) {
		int compare = 0;
		
		if(node != null) {
			compare = node.compareTo(data);
			if(compare < 0 && node.right != null) {
				return search(node.right, data);
			} else if(compare > 0 && node.left != null) {
				return search(node.left, data);
			} else {
				return node;
			}
		}
		
		return node;
	}
	
	public boolean isLeftChild(RBTreeNode<T> node) {
		boolean isLeft = false;
			
		if(node != root && node.prev.left == node) {
			isLeft = true;
		}
		
		return isLeft;
	}
	
	public RBTreeNode<T> getRoot() {
		return root;
	}
	
	///////////////////
	// RBTree Methods
	////////////////
		
	public RBTreeNode<T> getSibling(RBTreeNode<T> node) {
		RBTreeNode<T> sibling = null;
		
		if(node != null && node != root) {
			if(node.data.equals(node.prev.left.data)) {
				sibling = node.prev.right;
			} else {
				sibling = node.prev.left;
			}
		}
		
		return sibling;
	}
	
	public RBTreeNode<T> getAunt(RBTreeNode<T> node) {
		RBTreeNode<T> aunt = null;
		
		if(node != root && node.prev != root) {
			if(node.prev.prev.left != null && node.prev.data.equals(node.prev.prev.left.data)) {
				aunt = node.prev.prev.right;
			} else {
				aunt = node.prev.prev.left;
			}
		}
		
		return aunt;
	}
	
	public RBTreeNode<T> getGrandparent(RBTreeNode<T> node) {
		RBTreeNode<T> gParent = null;
		
		if(node != root && node.prev != root) {
			gParent = node.prev.prev;
		}
		
		return gParent;
	}
	
	public void rotateLeft(RBTreeNode<T> node) {
		if(node != null && node.right != null) {
			if(node != root) {
				if(isLeftChild(node)) {
					node.prev.left = node.right;
				} else {
					node.prev.right = node.right;
				}
			
				node.right.prev = node.prev;
				node.prev = node.right;
	
				if(node.right.left != null) {
					node.prev.left.prev = node;
					node.right = node.right.left;
				} else {
					node.right = null;
				}
		
				node.prev.left = node;
			} else {	
				// Update root
				root = node.right;
				node.prev = root;
				root.prev = null;
				
				if(node.right.left != null) {
					root.left.prev = node;
					node.right = root.left;	
				} else {
					node.right = null;
				}
				
				root.left = node;
			} 
		}
		// else don't rotate
	}
	
	public void rotateRight(RBTreeNode<T> node) {
		if(node != null && node.left != null) {
			if(node != root) {
				if(isLeftChild(node)) {
					node.prev.left = node.left;
				} else {
					node.prev.right = node.left;
				}
			
				node.left.prev = node.prev;
				node.prev = node.left;
	
				if(node.left.right != null) {
					node.prev.right.prev = node;
					node.left = node.left.right;
				} else {
					node.left = null;
				}
		
				node.prev.right = node;
			} else {	
				// Update root
				root = node.left;
				node.prev = root;
				root.prev = null;
				
				if(node.left.right != null) {
					root.right.prev = node;
					node.left = root.right;	
				} else {
					node.left = null;
				}
				
				root.right = node;
			} 
		}
		// else don't rotate
	}
	
	public void fixTree(RBTreeNode<T> node) {
		RBTreeNode<T> parent = node.prev;
		RBTreeNode<T> grandParent = getGrandparent(node);
		
		// node is root, make black and done
		if(node == root) {
			node.color = true;
		} else if(parent.color) {
			// Do nothing
		} else if(!node.color && !parent.color) {
			if(getAunt(node) == null || getAunt(node).color) {
				if(isLeftChild(parent) && !isLeftChild(node)) {
					rotateLeft(parent);
					fixTree(node.prev);
				} else if(!isLeftChild(parent) && isLeftChild(node)) {
					rotateRight(parent);
					fixTree(node.prev);
				} else if(isLeftChild(parent) && isLeftChild(node)) {
					parent.color = true;
					grandParent.color = false;
					rotateRight(grandParent);
				} else if(!isLeftChild(parent) && !isLeftChild(node)) {
					parent.color = true;
					grandParent.color = false;
					rotateLeft(grandParent);
				}
			} else if(!getAunt(node).color) {
				parent.color = true;
				getAunt(node).color = true;
				grandParent.color = false;
				
				fixTree(grandParent);
			}
		}
	}
		
	///////////////
	// Node Class
	////////////
	
	// Black is True, red is False
	static class RBTreeNode<T> implements Comparable<T>{
		private boolean color;
		private T data;
		private RBTreeNode<T> prev;
		private RBTreeNode<T> left;	
		private RBTreeNode<T> right;

		/////////////////
		// Constructors
		//////////////
		
		// Creates a default node of color red
		public RBTreeNode() {
			data = null;
			color = false;
			prev = null;
			left = null;
			right = null;
		}
		
		// Allows for choice of node color on instantiation
		public RBTreeNode(boolean color) {
			data = null;
			this.color = color;
			prev = null;
			left = null;
			right = null;
		}
		
		////////////
		// Methods
		/////////
		
		public T getData() {
			return data;
		}
		
		public void setData(T data) {
			this.data = data;
		}
		
		public boolean getColor() {
			return color;
		}
		
		public void setColor(boolean color) {
			this.color = color;
		}

		public RBTreeNode<T> getPrev() {
			return prev;
		}

		public void setPrev(RBTreeNode<T> prev) {
			this.prev = prev;
		}

		public RBTreeNode<T> getLeft() {
			return left;
		}

		public void setLeft(RBTreeNode<T> left) {
			this.left = left;
		}

		public RBTreeNode<T> getRight() {
			return right;
		}

		public void setRight(RBTreeNode<T> right) {
			this.right = right;
		}

		@SuppressWarnings("unchecked")
		@Override
		public int compareTo(T o) {
			return ((Comparable<T>) this.data).compareTo(o);
		}
	}
	
	////////////
	// Visitor
	////////
	
	public static interface Visitor<T> {
		/**
			This method is called at each node.
			@param n the visited node
		*/
		void visit(RBTreeNode<T> n);
	}
	 
	public void preOrderVisit(Visitor<T> v) {
		preOrderVisit(root, v);
	}
	 
	private void preOrderVisit(RBTreeNode<T> n, Visitor<T> v) {
		if (n != null) {
			v.visit(n);
			preOrderVisit(n.left, v);
			preOrderVisit(n.right, v);
		}
	}
	
	public void inOrderVisit(Visitor<T> v) {
		inOrderVisit(root, v);
	}
	 
	 
	private void inOrderVisit(RBTreeNode<T> n, Visitor<T> v) {
		if (n != null) {
			inOrderVisit(n.left, v);
			v.visit(n);
			inOrderVisit(n.right, v);
		}
	}
	
	public void postOrderVisit(Visitor<T> v) {
		postOrderVisit(root, v);
	}
	 
	 
	private void postOrderVisit(RBTreeNode<T> n, Visitor<T> v) {
		if (n != null) {
			postOrderVisit(n.left, v);
			postOrderVisit(n.right, v);
			v.visit(n);
		}
	}
}
