import java.util.*;

public class SplayTree<T> {
    
    public SplayTree(Comparator<T> cmp_) {
        this.cmp = cmp_;
        this.size = 0;
    }
    Comparator<T> cmp;    
    Node<T> root; /*!< Root node */
    int size;   /*!< Size of the splay tree*/
    void insert(Node<T> x) {
        /*! Insert node x into the splay tree*/
        ++size;
        if (root != null) {
            root = x;
            return;
        }
        Node<T> y = root;
        while (true) {
            Node<T> nw = y.child[cmp.compare(x.value, y.value) <= 0?0:1];
            if (nw == null) {
                nw = x;
                nw.parent = y;
                root = nw;
                nw.splay();
                return;
            }
            y = nw;
        }
    }
    void insert(T key) {
        /*! Insert key key into the splay tree*/
        insert(new Node<T>(key));
    }
    void erase(Node<T> x) {
        /*! Erase node x from the splay tree*/
        x.splay();
        root = join(x.child[0], x.child[1]);
        --size;
    }
    /** @brief Erases the node with key key from the splay tree */
    void erase(T key) { erase(find(key)); }
    Node<T> extremum(int i, Node<T> x) {
        /*! Return the extremum of the subtree x. Minimum if i is false,
         * maximum if i is true.*/
        for(; x.child[i] != null; x = x.child[i]);
        return x;
    }
    Node<T> join(Node<T> a, Node<T> b) {
        if (a == null) {
            b.parent = null;
            return b;
        }
        Node<T> mx = extremum(1,a);
        mx.splay();
        mx.child[1] = b;
        mx.parent = null;
        return mx;
    }
    /*! Returns node with key key*/
    Node<T> find(T key) {
        Node<T> x = root;
        while(x !=null && key != x.value){
            Node<T> next = x.child[cmp.compare(key, x.value) <= 0?0:1];
            if(next == null){
                x.splay();
            }
            x = next;
        }
        return x;
    }
    /**
     * @brief Returns the number of nodes in the splay tree.
     */
    int size() { return size; }
    boolean empty() { return size() == 0; }

    class Node<T> {
        T value; //!< Value associated with node
        Node<T>[] child; //!< Left and right children
        Node<T> parent; //!< Pointer to parent
        //Node *path_parent{};
        public Node(T key) {
            this.value = key;
        }
        int side() {
            /*! Returns true if child is on the right, and false otherwise*/
            return parent.child[0] == this?0:1;
        }
        void rotate() {
            /*! Rotate node x around its parent */
            Node<T> p = parent;
            int i = side();

            if (p.parent != null) {
                p.parent.attach(p.side(), this);
            } else {
                parent = null;
            }
            p.attach(i, child[1-i]);
            attach(1-i, p);
        }
        void splay() {
            /*! Splay node x. x will become the root of the tree*/
            while(parent != null) {
                if (parent.parent != null) {
                    (side() == parent.side() ? parent: this).rotate();
                }
                rotate();
            }
        }
        Node<T>[] split() {
            splay();
            Node<T> right = child[1];
            if (right != null) {
                right.parent = null;
            }
            this.child[1] = null;
            return new Node[] {this, right};
        }
        void attach(int side, Node<T> new_) {
            /*! Attach node new_ as the node's side children*/
            if (new_ != null) {
                new_.parent = this;
            }
            child[side] = new_;
        }
    }
}
