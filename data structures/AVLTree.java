
class AVLTree<E extends Comparable<E>> {
	
	TreeNode<E> root;
	
	public E findLessOrEqualTo(E data) {
        return this.findLessOrEqualTo(root, data);
    }
    
    private E findLessOrEqualTo(TreeNode<E> node, E data){
        E res = null;
        if(node == null) return res;
        if(data.compareTo(node.data) < 0) {
            res = findLessOrEqualTo(node.left, data);
        }else {
            res = node.data;
            if(data.compareTo(node.data) > 0) {
                E comp = findLessOrEqualTo(node.right, data);
                if(comp != null) res = comp; 
            }
        }
        return res;
    }
    
    public E largest() {
        TreeNode<E> node = root;
        if(node == null) return null;
        while(node.right != null) {
            node = node.right;
        }
        return node.data;
    }
	
	public int countLessOrEqualTo(E data) {
		count = 0;
		this.countLessOrEqualTo(root, data);
		return count;
	}
	
	int count;
	
	private void countLessOrEqualTo(TreeNode<E> node, E data){
		if(node == null) return;
		if(data.compareTo(node.data) < 0) {
			countLessOrEqualTo(node.left, data);
		}else {
			count += getSize(node.left) + node.count;
			if(data.compareTo(node.data) > 0) countLessOrEqualTo(node.right, data);
		}
	}
	
	public long sumSmallestK(int k) {
		sum = 0;
		this.sumSmallestK(root, k);
		return sum;
	}
	
	long sum;
	
	private void sumSmallestK(TreeNode<E> node, int k){
		if(node == null) return;
		if(k <= getSize(node.left)) {
			sumSmallestK(node.left,k);
		}else if(k <= getSize(node.left) + node.count) {
			sum += getSum(node.left) + ((long)node.data)*(k-getSize(node.left));			
		} else {
			sum += getSum(node.left) + ((long)node.data)*Math.min((k-getSize(node.left)), node.count);
			sumSmallestK(node.right, k-(getSize(node.left) + node.count));
		}
	}
	
	private int getSize(TreeNode<E> node) {
		if(node == null) return 0;
		else return node.size;
	}
	
	public void insert(E data) {
		root = insert(root, data);
	}
	
	private TreeNode<E> insert(TreeNode<E> node, E data){
		if(node == null) {
			node = new TreeNode<E>(data);
		}else {
			if(data.compareTo(node.data) < 0) {
				node.left = insert(node.left, data);
			}else if(data.compareTo(node.data) > 0) {
				node.right = insert(node.right, data);
			} else {
				node.count++;
			}
			node.size = getSize(node.left)+getSize(node.right)+node.count;
			node.sum = getSum(node.left)+getSum(node.right)+node.count*((long)node.data);
		}
		node = checkAVL(node);
		return node;
	}
	
	private long getSum(TreeNode<E> node) {
		if(node == null) return 0L;
		else return node.sum;
	}
	
	public void remove(E data) {
		remove(root, data);
	}
	
	private TreeNode<E> remove(TreeNode<E> node, E data){
        if(data.compareTo(node.data) < 0){
            node.left = remove(node.left, data);
        }else if(data.compareTo(node.data) > 0){
            node.right = remove(node.right, data);
        }else {
            if(node.left == null && node.right == null) {
                node = null;
            }else if(node.left == null) {
                node = node.right;
            }else if(node.right == null) {
                node = node.left;
            }else{
                node.right = minimum(node.right);
                node.data = successor;
            }
        }
        node = (node != null)? checkAVL(node) : node;
        return node;
    }
    

    E successor;
    
    TreeNode<E> minimum(TreeNode<E> node) {
        if(node.left == null) {
            successor = node.data;
            node = node.right;
        }else {
            node.left = minimum(node.left);
            node = checkAVL(node);
        }
        return node;
    }

	private TreeNode<E> checkAVL(TreeNode<E> node){
		if(node != null) {
			int balance = getBalance(node);
			if(balance < -1 || balance > 1){
				node = rotate(node, balance);
			}
			update(node);
		}
		return node;
	}

	private TreeNode<E> rotate(TreeNode<E> node, int balance){
		if(balance < -1) {
			if(getBalance(node.left) <= 0) {
				node = rotateLeftLeft(node);
			}else {
				node = rotateLeftRight(node);
			}
		}else {
			if(getBalance(node.right) >= 0) {
				node = rotateRightRight(node);
			}else {
				node = rotateRightLeft(node);
			}
		}
		return node;
	}
	
	private TreeNode<E> rotateRightLeft(TreeNode<E> node) {
		TreeNode<E> pivot = node.right;
		TreeNode<E> root = pivot.left;
		pivot.left = root.right;
		node.right = root.left;
		root.right = pivot;
		root.left = node;
		update(pivot);
		update(node);
		return root;
	}

	private TreeNode<E> rotateRightRight(TreeNode<E> node) {
		TreeNode<E> root = node.right;
		node.right = root.left;
		root.left = node;
		update(node);
		return root;
	}

	private TreeNode<E> rotateLeftRight(TreeNode<E> node) {
		TreeNode<E> pivot = node.left;
		TreeNode<E> root = pivot.right;
		pivot.right = root.left;
		node.left = root.right;
		root.left = pivot;
		root.right = node;
		update(pivot);
		update(node);
		return root;
	}

	private TreeNode<E> rotateLeftLeft(TreeNode<E> node) {
		TreeNode<E> root = node.left;
		node.left = root.right;
		root.right = node;
		update(node);
		return root;
	}
	
	
	private void update(TreeNode<E> node) {
		if(node != null) {
			node.height = Math.max(getHeight(node.left), getHeight(node.right))+1;
			node.size = getSize(node.left)+getSize(node.right)+node.count;
			node.sum = getSum(node.left)+getSum(node.right)+node.count*((long)node.data);
		}
	}

	private int getBalance(TreeNode<E> node) {
		return getHeight(node.right) - getHeight(node.left);
	}
	
	private int getHeight(TreeNode<E> node) {
		return node != null? node.height : -1;
	}
		
}

class TreeNode<E extends Comparable<E>>{
	E data;
	int height;
	int size;
	int count;
	long sum;
	TreeNode<E> left;
	TreeNode<E> right;
	
	public TreeNode(E data){
		this.data = data;
		this.count = 1;
		this.size = 1;
		this.sum = (long)data;
	}
}