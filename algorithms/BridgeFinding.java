import java.io.*;
import java.util.*;
 
//TODO: modularization
//https://cses.fi/problemset/task/2076/
 
public class BridgeFinding extends PrintWriter {
    
    int[][] adj;
    
    int n;
    
    int timer = 0;
    boolean[] cut_point;
    int[] tin;
    int[] low;
    boolean[] visited;
 
    ArrayDeque<int[]> ans;
    
    void dfs(int u, int p) {
        visited[u] = true;
        tin[u] = low[u] = timer++;
        for (int v : adj[u]) {
            if(!visited[v]){
                dfs(v, u);
                low[u] = Math.min(low[u], low[v]);
                if (low[v] > tin[u]) {
                    ans.add(new int[] {u+1,v+1});
                }
            } else if (p != v && tin[v] < low[u]) {
                low[u] = tin[v]; 
            }
        }
    }
    
    
    int[] from;
    int[] to;
    
    int[] size;
    
    void solve()  {
        n = sc.nextInt();
        int m = sc.nextInt();
        size = new int[n];
        from = new int[m];
        to = new int[m];
        for(int i = 0; i < m; i++) {
            from[i] = sc.nextInt()-1;
            to[i] = sc.nextInt()-1;
            size[from[i]]++;
            size[to[i]]++;
        } 
        adj = new int[n][];
        for(int i = 0; i < n; i++) adj[i] = new int[size[i]];
        for(int i = 0; i < m; i++) {
            adj[from[i]][--size[from[i]]] = to[i];
            adj[to[i]][--size[to[i]]] = from[i];
        }
 
        ans = new ArrayDeque<int[]>();
        tin = new int[n]; Arrays.fill(tin, -1);
        low = new int[n]; Arrays.fill(low, -1);
        visited = new boolean[n];
        dfs(0,-1);
        println(ans.size());
        for(int[] x : ans) {
            println(x[0] + " " + x[1]);
        }
    }
//  Main() throws FileNotFoundException { super(new File("output.txt")); }
//  InputReader sc = new InputReader(new FileInputStream("test_input.txt"));
  BridgeFinding() { super(System.out); }
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
                try {BridgeFinding solution = new BridgeFinding(); solution.solve(); solution.flush();} 
                catch (Exception e) {e.printStackTrace(); System.exit(1);}
                System.err.println((System.nanoTime()-start)/1E9);
            }
        }, "1", 1 << 27).start();
 
    }
    
 
}
