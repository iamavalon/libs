#include <iostream>
#include <vector>
#include <stack>
#include <set>

//block-cut tree implementation
//O(n) preprocess + O(log n) for each query to verify if removing vertex u changes connectivity of vertice v to w
//https://cses.fi/problemset/task/1705/

using namespace std;

const int MxN = 100100;
const int MxM = 200100;
const int MxP = 17;

vector<int> adj1[MxN];
vector<int> adj2[MxM];

int timer = 0;
int tin[MxM];
int tout[MxM];
int t[MxM][MxP];

int SP;
int st[MxM][2];
bool visited[MxN];
int low[MxN];
int comp[MxN];
set<int>* cut_point[MxN];
int cnt_comp;



int n;
int m;

void dfs1(int u, int p) {
    visited[u] = true;
    tin[u] = low[u] = timer++;
    int children = 0;
    for (int v : adj1[u]) {
        if (!visited[v]) {
            children++;
            st[SP][0] = u; st[SP][1] = v; SP++;
            dfs1(v, u);
            low[u] = min(low[u], low[v]);
            if ((p != -1 && low[v] >= tin[u]) || (p == -1 && children > 1)) {
                if (cut_point[u] == nullptr) cut_point[u] = new set<int>();
                while (true) {
                    SP--; int x = st[SP][0]; int y = st[SP][1];
                    for (int t : {x, y}) {
                        comp[t] = cnt_comp;
                        if (cut_point[t] != nullptr) cut_point[t]->insert(cnt_comp);
                    }
                    if (x == u && y == v) break;
                }
                cnt_comp++;
            }
        }
        else if (p != v && tin[v] < low[u]) {
            st[SP][0] = u; st[SP][1] = v; SP++;
            low[u] = tin[v];
        }
    }
}


void cut_block() {
    timer = 0;
    fill(comp, comp + n, -1);
    fill(tin, tin + n, -1);
    fill(low, low + n, -1);

    dfs1(0, -1);
    if (SP > 0) {
        while (SP > 0) {
            SP--; int x = st[SP][0]; int y = st[SP][1];
            for (int t : {x, y}) {
                comp[t] = cnt_comp;
                if (cut_point[t] != nullptr) cut_point[t]->insert(cnt_comp);
            }
        }
        cnt_comp++;
    }
    for (int u = 0; u < n; u++) {
        if (cut_point[u] != nullptr) comp[u] = cnt_comp++;
    }
}

void build() {
    for (int u = 0; u < n; u++) {
        if (cut_point[u] != nullptr) {
            for (int v : *cut_point[u]) {
                adj2[comp[u]].push_back(v);
                adj2[v].push_back(comp[u]);
            }
        }
    }
}


void dfs2(int u, int p) {
    tin[u] = timer++;
    t[u][0] = p;
    for (int i = 1; i < MxP; i++) {
        t[u][i] = t[t[u][i - 1]][i - 1];
    }
    for (int v : adj2[u]) if (v != p) dfs2(v, u);
    tout[u] = timer;
}

void dfs2() {
    timer = 0;
    dfs2(0, 0);
}

bool is_ancestor(int u, int v) {
    return tin[u] <= tin[v] && tout[v] <= tout[u];
}

int lca(int u, int v) {
    if (is_ancestor(u, v)) return u;

    if (is_ancestor(v, u)) return v;

    for (int i = MxP - 1; i >= 0; i--) {
        if (!is_ancestor(t[u][i], v)) {
            u = t[u][i];
        }
    }
    return t[u][0];
}

int main() {
    ios_base::sync_with_stdio(0); cin.tie(0); cout.tie(0);

    int q; cin >> n >> m >> q;
    for (int i = 0; i < m; i++) {
        int u, v; cin >> u >> v; u--, v--;
        adj1[u].push_back(v);
        adj1[v].push_back(u);
    }

    cut_block();

    build();

    dfs2();

    for (int i = 0; i < q; i++) {
        int a, b, c; cin >> a >> b >> c; a--, b--, c--;
        if (a == c || b == c) cout << "NO" << "\n";
        else if (comp[a] == comp[b]) cout << "YES" << "\n";
        else if (cut_point[c] != nullptr) {
            int aa = comp[a];
            int bb = comp[b];
            int cc = comp[c];
            int lca_ab = lca(aa, bb);
            if (is_ancestor(cc, aa) && lca_ab == lca(bb, cc)) {
                cout << "NO" << "\n";
            }
            else if (is_ancestor(cc, bb) && lca_ab == lca(aa, cc)) {
                cout << "NO" << "\n";
            }
            else cout << "YES" << "\n";
        }
        else cout << "YES" << "\n";
    }
}