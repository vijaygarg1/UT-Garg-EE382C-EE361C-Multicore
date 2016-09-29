class MRSW {
    int n = 0;
    SRSW V[] = null;// value written for reader i
    SRSW Comm[][] = null; // communication between readers
    int seqNo = 0;
    public MRSW(int readers, int initVal) {
        n = readers;
        V = new SRSW[n];
        for (int i = 0; i < n; i++)
            V[i].setValue(initVal, 0);
        Comm = new SRSW[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                Comm[i][j].setValue(initVal, 0);
    }
    public int getValue(int r) {//reader r reads
        //read your own register
        SRSW tsv = V[r]; // tsv is local
        
        // find the value with the largest timestamp
        for (int i = 0; i < n; i++)
            if (Comm[i][r].getTS() > tsv.getTS())
                tsv = Comm[i][r];
        
        // inform other readers
        for (int i = 0; i < n; i++) {
            Comm[r][i].setValue(tsv);
        }
        return tsv.getValue();
    }
    public void setValue(int x) {// accessed by the writer
        // write the value with a larger timestamp
        seqNo++;
        for (int i = 0; i < n; i++)
            V[i].setValue(x, seqNo);
    }
}
