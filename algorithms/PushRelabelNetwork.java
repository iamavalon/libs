//push-relabel max flow
//O(V^3)
//sample problem https://cses.fi/problemset/task/1694

import java.util.*;

class PushRelabelNetwork {

    ArrayList<Edge>[] adj;
    int[] current;

    int n;

    int s;
    int t;

    long[] e;
    long[] h;

    void push(int u, Edge edge) {
        int v = edge.other(u);
        long min = Math.min(e[u], edge.capRto(v));
        edge.addFlowRTo(v, min);
        e[u] = e[u] - min;
        e[v] = e[v] + min;
    }

    void relabel(int u) {
        long min = 1L << 60;
        ListIterator<Edge> node = adj[u].listIterator();
        while (node.hasNext()) {
            Edge edge = node.next();
            int v = edge.other(u);
            if (edge.capRto(v) > 0 && h[v] < min) {
                min = h[v];
            }
        }
        h[u] = 1 + min;
    }

    void initializePreflow() {
        h = new long[n];
        e = new long[n];
        h[s] = n;
        ListIterator<Edge> node = adj[s].listIterator();
        while (node.hasNext()) {
            Edge edge = node.next();
            int v = edge.other(s);
            e[v] = e[v] + edge.capRto(v);
            e[s] = e[s] - edge.capRto(v);
            edge.addFlowRTo(v, edge.capRto(v));
        }
    }

    void discharge(int u) {
        while (e[u] > 0) {
            if (current[u] == adj[u].size()) {
                relabel(u);
                current[u] = 0;
            } else {
                Edge edge = adj[u].get(current[u]++);
                int v = edge.other(u);
                if (edge.capRto(v) > 0 && h[u] == h[v] + 1) {
                    push(u, edge);
                }
            }
        }
    }

    void relabelToFront() {
        initializePreflow();
        LinkedList<Integer> list = new LinkedList<Integer>();
        for (int u = 0; u < n; u++) {
            current[u] = 0;
            if (u != s && u != t)
                list.addFirst(u);
        }
        ListIterator<Integer> node = list.listIterator();
        while (node.hasNext()) {
            int u = node.next();
            long oldHeight = h[u];
            discharge(u);
            if (h[u] > oldHeight) {
                node.remove();
                node = list.listIterator();
                node.add(u);
            }
        }
    }

    public PushRelabelNetwork(int n, int s, int t) {
        this.n = n;
        this.s = s;
        this.t = t;
        current = new int[n];
        adj = new ArrayList[n];
        for (int i = 0; i < n; i++)
            adj[i] = new ArrayList<Edge>();
    }

    void insertEdge(int from, int to, int cap) {
        Edge edge = new Edge(from, to, cap);
        adj[from].add(edge);
        adj[to].add(edge);
    }

    class Edge {
        int u;
        int v;
        long cap;
        long flow;

        public Edge(int u, int v, int cap) {
            this.u = u;
            this.v = v;
            this.cap = cap;
        }

        public boolean from(int v) {
            return this.u == v;
        }

        public int other(int v) {
            return from(v) ? this.v : this.u;
        }

        public long capRto(int v) {
            return from(v) ? flow : cap - flow;
        }

        public void addFlowRTo(int v, long d) {
            flow += from(v) ? -d : d;
        }
    }

}
