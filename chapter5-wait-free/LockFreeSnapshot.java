public class LockFreeSnapshot {
    int n = 0;
    SRSW[] V;
    public void LockFreeSnapshot(int initN) {
        n = initN;
        V = new SRSW[n];
    }
    public void writeLoc(int k, int x) {
        int seq = V[k].ts;
        V[k].setValue(x, seq + 1);
    }
    public SRSW[] readArray() {
        SRSW[] W = new SRSW[n];// W is local
        boolean done = false;
        while (!done) {
            for (int i = 0; i < n; i++) // copy V to W
                W[i].setValue(V[i].value, V[i].ts);
            done = true;
            // check if V has changed
            for (int i = 0; i < n; i++)
                if (W[i].ts != V[i].ts) {
                    done = false;
                    break;
                }
        }
        return W;
    }
}
