class MultiWriter {
    class MRSW {
        public int val = 0, ts = 0, pid = 0;
        public synchronized void setValue(int x, int seq, int id) {
            val = x;
            ts = seq;
            pid = id;
        }
    }
    int n = 0;
    MRSW V[] = null;// value written by the writer i
    public MultiWriter(int writers, int initVal) {
        n = writers;
        V = new MRSW[n];
        for (int i = 0; i < n; i++)
            V[i].setValue(initVal, 0, i);
    }
    public int getValue() {
        MRSW tsv = V[0]; // tsv is local
        for (int i = 1; i < n; i++)
            if ((tsv.ts < V[i].ts) ||
            ((tsv.ts == V[i].ts) && (tsv.pid < V[i].pid)))
                tsv = V[i];
        return tsv.val;
    }
    public void setValue(int w, int x) { // writer w
        int maxseq = V[0].ts;
        for (int i = 1; i < n; i++)
            if (maxseq < V[i].ts) maxseq = V[i].ts;
        V[w].setValue(x, maxseq + 1, w);
    }
}


