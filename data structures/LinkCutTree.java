//based on 
//1) https://codeforces.com/blog/entry/80383
//2) https://github.com/brunomaletta/Biblioteca/tree/master/Codigo/Grafos/LCT
//3) https://codeforces.com/blog/entry/75885
//4) https://codeforces.com/blog/entry/67637

public class LinkCutTree {
        
        int n;
        int m;
        
        public LinkCutTree(int n_, int m_){
            n = n_;
            m = m_;
            t  = new Node[n+m];
        }

        class Node{
            int[] ch;
            int p;
            int edge_id;
            int delete_time;
            int earlier_delete_time;
            int rev;
            
            public Node(int delete_time_) {
                delete_time = delete_time_;
                earlier_delete_time = delete_time;
                rev = 0;
                p = -1;
                ch = new int[] {-1,-1};
            }
            
            public Node() {
                delete_time = 1<<30;
                earlier_delete_time = delete_time;
                rev = 0;
                p = -1;
                ch = new int[] {-1,-1};
            }
        }
        
        Node[] t;

        void make_tree_edge(int u, int delete_time) { 
            t[u] = new Node(delete_time); 
        }
        
        void make_tree(int u) { 
            t[u] = new Node(); 
        }
        
        void prop(int x) {
            if(t[x].rev == 1) {
                int temp = t[x].ch[0]; t[x].ch[0] = t[x].ch[1]; t[x].ch[1] = temp;              
                for(int i = 0; i < 2; i++) if(t[x].ch[i] != -1) t[t[x].ch[i]].rev ^= 1;
            }
            t[x].rev = 0;
        }
        
        void update(int x) { 
            t[x].earlier_delete_time = t[x].delete_time;
            for (int i = 0; i < 2; i++) if (t[x].ch[i] != -1) {
                prop(t[x].ch[i]);
                t[x].earlier_delete_time = Math.min(t[x].earlier_delete_time, t[t[x].ch[i]].earlier_delete_time);
            }
        }
        
        boolean is_root(int x) {
            return t[x].p==-1 || ( t[t[x].p].ch[0] != x && t[t[x].p].ch[1] != x ); 
        }
        
        int dir(int x) {
            return t[t[x].p].ch[0] == x?0:1;
        }

        void rotate(int x) {
            int y = t[x].p; 
            int z = t[y].p;
            int dx = dir(x);
            if(!is_root(y)) t[z].ch[dir(y)] = x;        
            t[x].p = z;
            
            
            t[y].ch[dx] = t[x].ch[dx^1];
            if(t[y].ch[dx] != -1) t[t[y].ch[dx]].p = y;
            
            t[x].ch[dx^1] = y;
            t[y].p = x;
            update(y);
            update(x);
        }
        
        void splay(int x) {
            while(!is_root(x)) {
                int y = t[x].p;
                int z = t[y].p;
                if (!is_root(y)) prop(z);
                prop(y);
                prop(x);
                if(!is_root(y)) rotate(dir(x) == dir(y) ? y : x);
                rotate(x);
            }
            prop(x);
        }
        
        int access(int x) {
            int u = x;
            int v = -1;
            for(;u != -1; v = u, u = t[u].p) {
                splay(u);
                t[u].ch[1] = v;
                update(u);
            }
            splay(x);
            return v;
        }
        
        void reroot(int u) {
            access(u);
            t[u].rev ^= 1;
        }
        
        int query(int u, int v) {
            reroot(u);
            access(v);
            return t[v].earlier_delete_time;
        }
        
        int sz;
        
        int add_edge(int u, int v, int delete_time) {
            int id = n + sz++;
            make_tree_edge(id, delete_time);
            link(u, id); link(id, v);
            return id;
        }
        
        void link(int u, int v) {
            reroot(u);
            access(v);
            t[u].p = v;
        }
        
        void remove_edge(int u, int v, int id) {
            cut(u, id); cut(id, v);
        }
        
        void cut(int u, int v) {
            reroot(u);
            access(v);
            t[v].ch[0] = -1;
            t[u].p = -1;
        }
        
        int lca(int u, int v) {
            if (u == v) return u;
            access(u); int ret = access(v); 
            return t[u].p != -1 ? ret : -1;
        }
    }