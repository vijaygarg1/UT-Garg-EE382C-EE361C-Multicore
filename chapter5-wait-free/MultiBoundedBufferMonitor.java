class MultiBoundedBufferMonitor {
    final int size = 10;
    double[] buffer = new double[size];
    int inBuf = 0, outBuf = 0, count = 0;
    public synchronized void deposit(double value) {
        while (count == size) // buffer full
            Util.myWait(this);
        buffer[inBuf] = value;
        inBuf = (inBuf + 1) % size;
        count++;
        notifyAll();
    }
    public synchronized double fetch() {
        double value;
        while (count == 0) // buffer empty
            Util.myWait(this);
        value = buffer[outBuf];
        outBuf = (outBuf + 1) % size;
        count--;
        notifyAll();
        return value;
    }
}

