import java.util.*;

//suffix array + LCP
//TODO change to a more independent class style code
 
public class SuffixArray {
    

    int n;
    int[] lcp;
    int[] sa;
    int classes = 27;
    
    void solve(String input)  {
        
        
        String s = input+"$";
        n = s.length();
        sa = new int[n];
        int[] cl = new int[n];
        int[] cnt = new int[Math.max(n, classes)];
        int[] sa_temp = new int[n];
        int[] cl_temp = new int[n];
        for(int i = 0; i < n-1; i++) {
            cl[i] = s.charAt(i) - 'a' + 1;
        }
        for(int l = 1; l < n; l*=2) {
            Arrays.fill(cnt, 0, classes, 0);
            
            for(int i = 0; i < n; i++) cnt[cl[i]]++;            
            for(int i = 1; i < classes; i++) cnt[i] += cnt[i-1];            
            for(int i = n-1; i >= 0; i--) sa_temp[--cnt[cl[(i+l)%n]]] = i;
            
            Arrays.fill(cnt, 0, classes, 0);
            for(int i = 0; i < n; i++) cnt[cl[i]]++;            
            for(int i = 1; i < classes; i++) cnt[i] += cnt[i-1];            
            for(int i = n-1; i >= 0; i--) sa[--cnt[cl[sa_temp[i]]]] = sa_temp[i];
            classes = 0;
            
            for(int i = 0; i < n; i++) {
                if(i > 0 && (cl[sa[i]] != cl[sa[i-1]] || cl[(sa[i]+l)%n] != cl[(sa[i-1]+l)%n])) classes++;
                cl_temp[sa[i]] = classes;
            }
            classes++;
            cl = Arrays.copyOf(cl_temp, n);
        }
        int[] rank = new int[n];
        for(int i = 0; i < n; i++) rank[sa[i]] = i;
        
        int k = 0;
        lcp = new int[n]; lcp[n-1] = -1;
        for(int i = 0; i < n; i++) {
            if(rank[i] == n-1) {
                k = 0;
                continue;
            }
            int j = sa[rank[i]+1];
            while(i + k < n && j + k < n && s.charAt(i+k) == s.charAt(j+k)) k++;
            lcp[rank[i]] = k;
            if(k > 0) k--;
        }

    }
    
    
    
    
}


