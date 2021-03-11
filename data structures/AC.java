import java.util.ArrayDeque;


class AC{
    
    final int K = 26;
    final char a = 'A';
    
    int m;
    
    int[][] next;

    int[][] d;
    

    int[] sl;
    int[] ol;
    
    void insert(char[] chs) {
        int u = 0;
        for(char ch : chs) {
            int c = ch-a;
            if(next[u][c] == 0) {
                next[u][c] = m++;
            }
            u = next[u][c];
        }
        ol[u] = u;
    }
    
    void find(char[] chs) {
        int u = 0;
        //HashSet<Integer> done = new HashSet<>();
        for(char ch : chs) {
            int c = ch-a;
            u = d[u][c];
            int v = ol[u];
            while(v != 0/* && !done.contains(v)*/) {
                //done.add(v);
                //strings on v have been found save them previously if more than one ocurrence
                v = ol[sl[v]];
            }
        }
    }
    
    
    void ac() {
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.addLast(0);
        while(!queue.isEmpty()) {
            int u = queue.removeFirst();
            for(int i = 0; i < 26; i++) {
                if(next[u][i] != 0) {
                    int v = next[u][i];
                    sl[v] = d[sl[u]][i];
                    if(ol[v] == 0) ol[v] = ol[sl[u]];
                    d[u][i] = v;
                    queue.add(v);
                } else {
                    d[u][i] = d[sl[u]][i]; 
                }
            }
        }
    }
    
    
    
    
    public AC(int M) {
        next = new int[M+1][K];
        d = new int[M+1][K];
        sl = new int[M+1];
        ol = new int[M+1];
        m = 1;
    }
}