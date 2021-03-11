//min cost flow
//complexity O(max(flow * E log V, V * E))
//if there are no negative cycles bellmanford and reduce cost can be skipped
//complexity becomes O(flow * E log V) with lower constant
//sample problem: https://cses.fi/problemset/task/2131/
import java.util.*;
class MinCostFlowNetwork {
    ArrayDeque<Edge>[] adj;

    Edge[] ST;
    long[] d;

    int n;

    int s;
    int t;

    long INF = 1L<<60;

    public MinCostFlowNetwork(int n, int s, int t) {
        this.n = n;
        adj = new ArrayDeque[n];
        ST = new Edge[n];
        for (int i = 0; i < n; i++) adj[i] = new ArrayDeque<Edge>();
        this.s = s;
        this.t = t;
    }

    void insertEdge(int from, int to, int cap, long cost) {
        Edge edge = new Edge(from, to, cap, cost);
        adj[from].addFirst(edge);
        adj[to].addFirst(edge);
    }


    void bellmanFord(int s) {
        d = new long[n];
        for(int i = 0; i < n; i++) d[i] = INF;
        d[s] = 0;
        for(int i = 1; i < n; i++) {
            for(int u = 0; u < n; u++) {
                for(Edge edge : adj[u]) {
                    int v = edge.other(u);
                    if (edge.capRTo(v) > 0) d[v] = Math.min(d[v], d[u] + edge.costTo(v));
                }
            }
        }
    }

    void dijkstra(int s) {
        d = new long[n];
        for(int i = 0; i < n; i++) d[i] = INF;
        PriorityQueue<long[]> pq = new PriorityQueue<long[]>(n,Comparator.comparing(arr -> arr[1]));
        d[s] = 0;
        pq.add(new long[] {s,d[s]});
        while(!pq.isEmpty()) {
            int u = (int)pq.remove()[0];
            if(u == t) return;
            for(Edge edge : adj[u]) {
                int v = edge.other(u);
                if(edge.capRTo(v) > 0) {
                    long dist = d[u] + edge.costTo(v);
                    if(dist < d[v]) {
                        d[v] = dist;
                        ST[v] = edge;
                        pq.add(new long[] {v,d[v]});
                    }
                }
            }
        }
    }

    void reduceCost() {
        for(int u = 0; u < n; u++) for(Edge edge : adj[u]) if (edge.from(u)) edge.curCost = edge.curCost + d[u] - d[edge.other(u)];
    }

    boolean pathExists() {
        dijkstra(s);
        reduceCost();
        return d[t] < INF;
    }

    long minCostFlow(int maxFlow) {
        int flow = 0;
        bellmanFord(s);
        reduceCost();
        while (flow < maxFlow && pathExists()) {
            int bottle = 1<<30;
            for (int v = t; v != s; v = ST[v].other(v)) bottle = Math.min(bottle, ST[v].capRTo(v));
            bottle = Math.min(bottle, maxFlow-flow);
            for (int v = t; v != s; v = ST[v].other(v)) ST[v].addFlowRTo(v, bottle);
            flow = flow + bottle;
        }
        if(flow < maxFlow) return INF;
        long cost = 0;
        for(int u = 0; u < n; u++) for(Edge edge : adj[u]) if (edge.from(u)) cost = cost + edge.flow*edge.cost;
        return cost;
    }

    

}

class Edge {
    public int u;
    public int v;
    public int cap;
    public int flow;
    public long cost;
    public long curCost;

    public Edge(int u, int v, int cap, long cost) {
        this.u = u;
        this.v = v;
        this.cap = cap;
        this.cost = cost;
        this.curCost = cost;
    }

    public boolean from(int u) {
        return this.u == u;
    }

    public int other(int u) {
        return from(u) ? this.v : this.u;
    }

    public int capRTo(int u) {
        return from(u) ? flow : cap - flow;
    }

    public long costTo(int u) {
        return from(u) ? -curCost : curCost;
    }

    public void addFlowRTo(int u, long d) {
        flow += from(u) ? -d : d;
    }
}