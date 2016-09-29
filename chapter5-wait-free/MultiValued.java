class MultiValued {
    int n = 0;
    boolean A[] = null;
    public MultiValued(int maxVal, int initVal) {
        n = maxVal;
        A = new boolean[n];
        for (int i = 0; i < n; i++) A[i] = false;
        A[initVal] = true;
    }
    public int getValue() {
        int j = 0;
        while (!A[j]) j++; // forward scan
        int v = j;
        for (int i = j - 1; i >= 0; i--) // backward scan
            if (A[i]) v = i;
        return v;
    }
    public void setValue(int x) {
        A[x] = true;
        for (int i = x - 1; i >= 0; i--)
            A[i] = false;
    }
}

