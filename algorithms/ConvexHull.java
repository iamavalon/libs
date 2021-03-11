import java.io.*;
import java.util.*;

//https://cses.fi/problemset/task/2195/

public class ConvexHull extends PrintWriter {
    
    class Point{
        long x;
        long y;
        
        public Point(long x_, long y_) {
            x = x_;
            y = y_;
        }
        
        long cross(Point other) {
            return x * other.y - y * other.x;
        }
        
        Point minus(Point other) {
            return new Point(x - other.x, y - other.y);
        }
        
        long triangle(Point b, Point c) {
            return (b.minus(this)).cross(c.minus(this));
        }
    }
    
    
    private void solve()  {
        int n = sc.nextInt();
        Point[] p = new Point[n];
        for(int i = 0; i < n; i++) {
            p[i] = new Point(sc.nextLong(), sc.nextLong());
        }
    
        Arrays.sort(p, Comparator.comparing((Point q) -> q.x).thenComparing(q -> q.y));
        Point[] hull = new Point[2*n+2];
        int SP = 0;
        for(int rep = 0; rep < 2; rep++) {
            for(int i = 0; i < n; i++) {
                while(SP > 1 && hull[SP-2].triangle(hull[SP-1], p[i]) > 0) {
                    SP--;
                }
                hull[SP++] = p[i];
            }
            SP--;
            for(int i = 0; i < n/2; i++) { Point temp = p[i]; p[i] = p[n-1-i]; p[n-1-i] = temp;}
        }
        println(SP);
        for(int i = 0; i < SP; i++) {
            println(hull[i].x + " " + hull[i].y);
        }
    }
    
//  Main() throws FileNotFoundException { super(new File("output.txt")); }
//  InputReader sc = new InputReader(new FileInputStream("test_input.txt"));
  ConvexHull() { super(System.out); }
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
                try {ConvexHull solution = new ConvexHull(); solution.solve(); solution.flush();} 
                catch (Exception e) {e.printStackTrace(); System.exit(1);}
                System.err.println((System.nanoTime()-start)/1E9);
            }
        }, "1", 1 << 27).start();
 
    }
    
}
