
//Edmonds-Karp Max flow
//sample problem: https://cses.fi/problemset/task/1694/
// O(E^2*V)

import java.util.ArrayDeque;


public class Network {
    
    
    Edge[][] adj;
    boolean[] marked;
    Edge[] ST;
 
    int n;
 
    int s;
    int t;
 
    public Network(int n, int s, int t) {
        this.n = n;
        adj = new Edge[n][n];
        ST = new Edge[n];
        this.s = s;
        this.t = t;
    }
 
    void insertEdge(int from, int to, int cap) {
        Edge edge = new Edge(from, to, cap);
        adj[from][to] = edge;
        adj[to][from] = edge;
    }
 
    void bfs(int s) {
        ArrayDeque<Integer> queue = new ArrayDeque<Integer>();
        queue.add(s);
        marked[s] = true;
        while(!queue.isEmpty()) {
            int u = queue.remove();
            for (Edge edge : adj[u]) {
                if(edge == null) continue;
                int v = edge.other(u);
                if (edge.capRto(v) > 0) {
                    if (!marked[v]) {
                        marked[v] = true;
                        ST[v] = edge;
                        if (v == t) {
                            return;
                        }
                        queue.add(v);
                    }
                }
            }
        }
    }
 
    boolean pathExists() {
        marked = new boolean[n];
        bfs(s);
        return marked[t];
    }
 
    int maxFlow() {
        int maxFlow = 0;
        while (pathExists()) {
            int bottle = Integer.MAX_VALUE;
            for (int v = t; v != s; v = ST[v].other(v)) {
                bottle = Math.min(bottle, ST[v].capRto(v));
            }
 
            for (int v = t; v != s; v = ST[v].other(v)) {
                ST[v].addFlowRTo(v, bottle);
            }
            maxFlow = maxFlow + bottle;
        }
        return maxFlow;
    }
 
}
 
 
class Edge {
    public int v;
    public int w;
    public int cap;
    public int flow;
    Edge next;
 
    public Edge(int u, int v, int cap) {
        this.v = u;
        this.w = v;
        this.cap = cap;
    }
 
    public boolean from(int v) {
 
 
        return this.v == v;
    }
 
    public int other(int v) {
        return from(v) ? this.w : this.v;
    }
 
    public int capRto(int v) {
        return from(v) ? flow : cap - flow;
    }
 
    public void addFlowRTo(int v, int d) {
        flow += from(v) ? -d : d;
    }
}