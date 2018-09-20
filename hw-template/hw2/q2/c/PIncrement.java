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

    public static void main(String args[]) {
        // Time
        long start1 = System.nanoTime();
        parallelIncrement(0, 1);
        long end1 = System.nanoTime();
        System.out.println("1 Thread (ns): " + (end1 - start1));

        long start2 = System.nanoTime();
        parallelIncrement(0, 2);
        long end2 = System.nanoTime();
        System.out.println("2 Threads (ns): " + (end2 - start2));

        long start4 = System.nanoTime();
        parallelIncrement(0, 4);
        long end4 = System.nanoTime();
        System.out.println("4 Threads (ns): " + (end4 - start4));

        long start8 = System.nanoTime();
        parallelIncrement(0, 8);
        long end8 = System.nanoTime();
        System.out.println("8 Threads (ns): " + (end8 - start8));
    }

}
