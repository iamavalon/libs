import java.io.*;
import java.util.*;

//https://cses.fi/problemset/task/1079/
//generates binomial coefficients C(n,k) given n in a mod for all k
//O(n)
 
public class BinomialCoefficients extends PrintWriter {
    
    int MX = 1000000;
    long[] fac = new long[MX+1];
    long[] fac_inv = new long[MX+1];
    long[] inv = new long[MX+1];
    
    long M = 1000000007L;
 
    long C(int n, int k) {
        long  c = (fac_inv[k] * fac_inv[n - k]) % M;
        c = (c * fac[n]) % M;
        return c;
    }
 
    private void solve()  {
        build_fac();
        int n = sc.nextInt();
        for(int i = 0; i < n; i++) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            println(C(a,b));
        }
    }
    
    void build_fac() {
        inv[1] = 1L;
        for(int i = 2; i <= MX; i++) 
            inv[i] = M - (M/i) * inv[(int)(M%i)] % M;
        fac[0] = 1L;
        fac_inv[0] = 1L;
        for(int i = 1; i <= MX; i++) {
            fac[i] = (fac[i-1]*i)%M;
            fac_inv[i] = (fac_inv[i-1]*inv[i])%M;
        }
    }
    
//  Main() throws FileNotFoundException { super(new File("output.txt")); }
//  InputReader sc = new InputReader(new FileInputStream("test_input.txt"));
  BinomialCoefficients() { super(System.out); }
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
                try {BinomialCoefficients solution = new BinomialCoefficients(); solution.solve(); solution.flush();} 
                catch (Exception e) {e.printStackTrace(); System.exit(1);}
                System.err.println((System.nanoTime()-start)/1E9);
            }
        }, "1", 1 << 27).start();
 
    }
    
}