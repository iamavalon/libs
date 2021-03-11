import java.io.*;
import java.util.*;
 
//https://codeforces.com/edu/course/2/lesson/7/3/practice/contest/289392/problem/C


public class DynamicConnectivityOffline extends PrintWriter {
	

    private void solve()  {
    	int n = sc.nextInt();
    	int m = sc.nextInt();
    	LinkCutTree lct = new LinkCutTree(n,m);
    	for(int i = 0; i < n; i++) lct.make_tree(i);
    	HashMap<String, Integer> mp = new HashMap<>();
    	int[][] queries = new int[m][];
    	for(int i = 0; i < m; i++) {
    		char qt = sc.nextString().charAt(0);
    		if(qt == '?') {
    			continue;
    		} else{
        		queries[i] = new int[] {qt == '+'?1:-1, sc.nextInt()-1, sc.nextInt()-1, -1};
    			if(queries[i][1] > queries[i][2]) {int temp = queries[i][1]; queries[i][1] = queries[i][2]; queries[i][2] = temp;}
    		}
    	}
    	for(int i = m-1; i >= 0; i--) {
    		if(queries[i] == null) continue;
    		String e = queries[i][1] + " " + queries[i][2];
    		if(queries[i][0] == -1) {
    			mp.put(e, i);
    		}else {
    			queries[i][3] = mp.getOrDefault(e, m);
    		}
    	}
    	int cnt = 0;
    	for(int i = 0; i < m; i++) {
    		if(queries[i] == null) {
        		println(n-cnt);
    			continue;
    		}
    		int t = queries[i][0];
			int u = queries[i][1];
			int v = queries[i][2];
			if(t == 1) {
				if(lct.lca(u, v) >= 0) {
    				int delete_time = lct.query(u, v);
    				if(delete_time < queries[i][3]) {
        				lct.remove_edge(queries[delete_time][1], queries[delete_time][2], queries[delete_time][3]);
        				queries[delete_time][3] = -1;
        				int id = lct.add_edge(u, v, queries[i][3]);
        				if(queries[i][3] < m) queries[queries[i][3]][3] = id;
    				}
    			} else {
    				int id = lct.add_edge(u, v, queries[i][3]);
    				if(queries[i][3] < m) queries[queries[i][3]][3] = id;
    				cnt++;
    			}
			} else {
				if(queries[i][3] >= 0) {
					lct.remove_edge(u, v, queries[i][3]);
					cnt--;
				}
			}
    	}
    }
    
    
    class LinkCutTree {
    	
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
    			int temp = t[x].ch[0]; t[x].ch[0] = t[x].ch[1];	t[x].ch[1] = temp;    			
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

    
//  Solution() throws FileNotFoundException { super(new File("output.txt")); }
//  InputReader sc = new InputReader(new FileInputStream("test_input.txt"));
  DynamicConnectivityOffline() { super(System.out); }
  InputReader sc = new InputReader(System.in);
  static class InputReader {
      InputReader(InputStream in) { this.in = in; } InputStream in;
      
      private byte[] buf = new byte[16384];
      private int    curChar;
      private int    numChars;
      
 
      public int read() {
          if (numChars == -1)
              throw new InputMismatchException();
          if (curChar >= numChars) {
              curChar = 0;
              try {
                  numChars = in.read(buf);
              } catch (IOException e) {
                  throw new InputMismatchException();
              }
              if (numChars <= 0)
                  return -1;
          }
          return buf[curChar++];
      }
 
      public String nextLine() {
          int c = read();
          while (isSpaceChar(c))
              c = read();
          StringBuilder res = new StringBuilder();
          do {
              res.appendCodePoint(c);
              c = read();
          } while (!isEndOfLine(c));
          return res.toString();
      }
 
      public String nextString() {
          int c = read();
          while (isSpaceChar(c))
              c = read();
          StringBuilder res = new StringBuilder();
          do {
              res.appendCodePoint(c);
              c = read();
          } while (!isSpaceChar(c));
          return res.toString();
      }
 
      public long nextLong() {
          int c = read();
          while (isSpaceChar(c))
              c = read();
          int sgn = 1;
          if (c == '-') {
              sgn = -1;
              c = read();
          }
          long res = 0;
          do {
              if (c < '0' || c > '9')
                  throw new InputMismatchException();
              res *= 10;
              res += c - '0';
              c = read();
          } while (!isSpaceChar(c));
          return res * sgn;
      }
 
      public int nextInt() {
          int c = read();
          while (isSpaceChar(c))
              c = read();
          int sgn = 1;
          if (c == '-') {
              sgn = -1;
              c = read();
          }
          int res = 0;
          do {
              if (c < '0' || c > '9')
                  throw new InputMismatchException();
              res *= 10;
              res += c - '0';
              c = read();
          } while (!isSpaceChar(c));
          return res * sgn;
      }
 
      private boolean isSpaceChar(int c) {
          return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
      }
 
      private boolean isEndOfLine(int c) {
          return c == '\n' || c == '\r' || c == -1;
      }
  }
 
    public static void main(String[] $) {
        new Thread(null, new Runnable() {
            public void run() {
                long start = System.nanoTime();
                try {DynamicConnectivityOffline solution = new DynamicConnectivityOffline(); solution.solve(); solution.flush();} 
                catch (Exception e) {e.printStackTrace(); System.exit(1);}
                System.err.println((System.nanoTime()-start)/1E9);
            }
        }, "1", 1 << 27).start();
 
    }
    

}