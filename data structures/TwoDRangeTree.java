import java.util.Arrays;
import java.util.Comparator;

public class TwoDRangeTree<T extends Comparable<T>> {

    boolean added;

    Point<Parse<T>> toParse(Point<T> p) {
        return new Point<Parse<T>>(new Parse<T>(p.x, p.y), new Parse<T>(p.y, p.x));
    }

    Node<Parse<T>> root;

    Point<Parse<T>>[] point;

    void build(Point<T>[] pts, int n) {
        if (n == 0)
            return;
        this.point = new Point[n];
        for (int i = 0; i < n; i++)
            this.point[i] = toParse(pts[i]);
        Arrays.sort(this.point, Comparator.comparing(pt -> pt.x));
        this.root = build(0, n - 1);
    }

    void addOrRemove(Point<T> p) {
        addOrRemove(root, toParse(p));
    }

    void addOrRemove(Node<Parse<T>> node, Point<Parse<T>> p) {
        addOrRemove2(node.next, p);
        if (!node.leaf) {
            if (p.x.compareTo(node.value) <= 0)
                addOrRemove(node.left, p);
            else
                addOrRemove(node.right, p);
        }
    }

    void addOrRemove2(Node<Parse<T>> node, Point<Parse<T>> p) {
        if (node.leaf) {
            node.count = 1 - node.count;
            added = node.count == 1;
        } else {
            if (p.y.compareTo(node.value) <= 0)
                addOrRemove2(node.left, p);
            else
                addOrRemove2(node.right, p);
            node.count = node.left.count + node.right.count;
        }
    }

    Node<Parse<T>> build(int l, int r) {
        Node<Parse<T>> node;
        if (l == r) {
            node = new Node<Parse<T>>(point[l].x, true);
        } else {
            int mid = (l + r) / 2;
            node = new Node<Parse<T>>(point[mid].x, false);
            node.left = build(l, mid);
            node.right = build(mid + 1, r);

            Point<Parse<T>>[] temp = new Point[r - l + 1];
            int i = l;
            int j = mid + 1;
            int k = 0;
            while (i <= mid && j <= r) {
                if (point[i].y.compareTo(point[j].y) <= 0)
                    temp[k++] = point[i++];
                else
                    temp[k++] = point[j++];
            }
            while (i <= mid)
                temp[k++] = point[i++];
            while (j <= r)
                temp[k++] = point[j++];
            for (k = 0; k < r - l + 1; k++)
                point[l + k] = temp[k];
        }

        node.next = build2(l, r);
        return node;
    }

    Node<Parse<T>> build2(int l, int r) {
        Node<Parse<T>> node;
        if (l == r) {
            node = new Node<Parse<T>>(point[l].y, true);
        } else {
            int mid = (l + r) / 2;
            node = new Node<Parse<T>>(point[mid].y, false);
            node.left = build2(l, mid);
            node.right = build2(mid + 1, r);
        }
        return node;
    }

    int count(T x1, T x2, T y1, T y2, T minf, T inf) {
        if (root == null)
            return 0;
        Parse<T> xx1 = new Parse<T>(x1, minf);
        Parse<T> xx2 = new Parse<T>(x2, inf);
        Parse<T> yy1 = new Parse<T>(y1, minf);
        Parse<T> yy2 = new Parse<T>(y2, inf);
        return count(xx1, xx2, yy1, yy2);
    }

    int countLD(T x, T y, T minf, T inf) {
        if (root == null)
            return 0;
        Parse<T> xx = new Parse<T>(x, inf);
        Parse<T> yy = new Parse<T>(y, inf);
        return countL(xx, yy);
    }

    int countL(Parse<T> x, Parse<T> y) {
        int count = 0;
        Node<Parse<T>> r = root;
        while (!r.leaf) {
            if (x.compareTo(r.value) >= 0) {
                count = count + countD(r.left.next, y);
                r = r.right;
            } else
                r = r.left;
        }
        if (x.compareTo(r.value) >= 0)
            count += countD(r.next, y);

        return count;
    }

    int countD(Node<Parse<T>> node, Parse<T> y) {
        Node<Parse<T>> l = node;
        Node<Parse<T>> r = node;
        int count = 0;

        while (!r.leaf) {
            if (y.compareTo(r.value) >= 0) {
                count = count + r.left.count;
                r = r.right;
            } else
                r = r.left;
        }
        if (y.compareTo(r.value) >= 0)
            count += r.count;

        return count;
    }

    int countRU(T x, T y, T minf, T inf) {
        if (root == null)
            return 0;
        Parse<T> xx = new Parse<T>(x, minf);
        Parse<T> yy = new Parse<T>(y, minf);
        return countR(xx, yy);
    }

    int countR(Parse<T> x, Parse<T> y) {
        Node<Parse<T>> l = root;
        int count = 0;
        while (!l.leaf) {
            if (x.compareTo(l.value) <= 0) {
                count = count + countU(l.right.next, y);
                l = l.left;
            } else
                l = l.right;
        }
        if (x.compareTo(l.value) <= 0)
            count += countU(l.next, y);

        return count;
    }

    int countU(Node<Parse<T>> node, Parse<T> y) {
        Node<Parse<T>> l = node;
        int count = 0;

        while (!l.leaf) {
            if (y.compareTo(l.value) <= 0) {
                count = count + l.right.count;
                l = l.left;
            } else
                l = l.right;
        }
        if (y.compareTo(l.value) <= 0)
            count += l.count;

        return count;
    }

    int count(Parse<T> x1, Parse<T> x2, Parse<T> y1, Parse<T> y2) {
        Node<Parse<T>> l = root;
        Node<Parse<T>> r = root;
        int count = 0;
        while (l == r && !l.leaf) {
            if (x1.compareTo(l.value) <= 0)
                l = l.left;
            else
                l = l.right;

            if (x2.compareTo(r.value) >= 0)
                r = r.right;
            else
                r = r.left;
        }
        if (l == r && l.leaf) {
            if (x1.compareTo(l.value) <= 0 && l.value.compareTo(x2) <= 0)
                count += count2(l.next, y1, y2);
        } else {

            while (!l.leaf) {
                if (x1.compareTo(l.value) <= 0) {
                    count = count + count2(l.right.next, y1, y2);
                    l = l.left;
                } else
                    l = l.right;
            }
            if (x1.compareTo(l.value) <= 0)
                count += count2(l.next, y1, y2);

            while (!r.leaf) {
                if (x2.compareTo(r.value) >= 0) {
                    count = count + count2(r.left.next, y1, y2);
                    r = r.right;
                } else
                    r = r.left;
            }
            if (x2.compareTo(r.value) >= 0)
                count += count2(r.next, y1, y2);

        }

        return count;
    }

    int count2(Node<Parse<T>> node, Parse<T> y1, Parse<T> y2) {
        Node<Parse<T>> l = node;
        Node<Parse<T>> r = node;
        int count = 0;
        while (l == r && !l.leaf) {
            if (y1.compareTo(l.value) <= 0) {
                l = l.left;
            } else
                l = l.right;

            if (y2.compareTo(r.value) >= 0) {
                r = r.right;
            } else
                r = r.left;
        }
        if (l == r && l.leaf) {
            if (y1.compareTo(l.value) <= 0 && l.value.compareTo(y2) <= 0)
                count += l.count;
        } else {

            while (!l.leaf) {
                if (y1.compareTo(l.value) <= 0) {
                    count = count + l.right.count;
                    l = l.left;
                } else
                    l = l.right;
            }
            if (y1.compareTo(l.value) <= 0)
                count += l.count;

            while (!r.leaf) {
                if (y2.compareTo(r.value) >= 0) {
                    count = count + r.left.count;
                    r = r.right;
                } else
                    r = r.left;
            }
            if (y2.compareTo(r.value) >= 0)
                count += r.count;

        }

        return count;
    }

}

class Point<T extends Comparable<T>> {
    T x;
    T y;

    public Point(T x, T y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((x == null) ? 0 : x.hashCode());
        result = prime * result + ((y == null) ? 0 : y.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Point other = (Point) obj;
        if (x == null) {
            if (other.x != null)
                return false;
        } else if (!x.equals(other.x))
            return false;
        if (y == null) {
            if (other.y != null)
                return false;
        } else if (!y.equals(other.y))
            return false;
        return true;
    }
}

class Parse<T extends Comparable<T>> implements Comparable<Parse<T>> {

    T x;
    T y;

    public Parse(T x, T y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Parse<T> other) {
        int comp = x.compareTo(other.x);
        if (comp == 0)
            comp = y.compareTo(other.y);
        return comp;
    }

}

class Node<T extends Comparable<T>> {
    T value;
    int count;
    boolean leaf;
    Node<T> left;
    Node<T> right;
    Node<T> next;

    public Node(T value, boolean leaf) {
        this.value = value;
        this.leaf = leaf;
    }
}