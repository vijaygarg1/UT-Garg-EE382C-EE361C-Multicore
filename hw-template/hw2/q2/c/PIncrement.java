package q2.c;

public class PIncrement implements Runnable{
    private static final int OPERATIONS = 120000;

    private static int count;
    private int incrementBy;
    private AndersonLock aLock;

    private PIncrement(int incrementBy, AndersonLock aLock){
        this.incrementBy = incrementBy;
        this.aLock = aLock;
    }

    public static int parallelIncrement(int c, int numThreads) {

        PIncrement.count = c;
        AndersonLock anderson = new AndersonLock(numThreads);
        Thread[] threadList = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threadList[i] = new Thread(new PIncrement(OPERATIONS / numThreads, anderson));
            threadList[i].start();
        }
        for (int i = 0;i < numThreads; i++) {
            try {
                threadList[i].join();
            } catch (InterruptedException e){}
        }
        return count;
    }

    @Override
    public void run() {
        for (int i = 0; i < incrementBy; i++){
            aLock.lock();
            count++;
            aLock.unlock();
        }
    }



}
