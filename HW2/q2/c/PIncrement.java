package q2.c;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PIncrement implements Runnable{

    static AtomicInteger tail;

    public static int getNumThreads() {
        return numThreads;
    }

    public static void setNumThreads(int numThreads) {
        PIncrement.numThreads = numThreads;
    }

    static int numThreads;

    int slot;

    static AtomicBoolean[] queue;

    public static AtomicInteger getTail() {
        return tail;
    }

    public static void setTail(AtomicInteger tail) {
        PIncrement.tail = tail;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        PIncrement.count = count;
    }

    static int count;

    static int operations = 120000;

    public static int parallelIncrement(int c, int numThreads){

        queue = new AtomicBoolean[numThreads]; //initialize queue of size N
        for(int i=0; i < numThreads; i++){
            queue[i] = new AtomicBoolean(false);
        }
        queue[0].set(true); //set only first thread to be able to acquire lock
        tail = new AtomicInteger();
        getTail().set(0); //set tail of queue to 0
        setNumThreads(numThreads);
        setCount(c);
        ArrayList<Thread> threads = new ArrayList<Thread>();

        for(int i = 0; i < numThreads; i++){
            Thread t = new Thread(new PIncrement());
            threads.add(t);
            t.start();
        }
        for(Thread t : threads) {
            try {
                t.join();
            }
            catch(Exception e){
                System.out.println("bad stuff");
            }
        }
        return getCount();
    }

    void lock() {
        slot = (getTail().getAndIncrement())%numThreads;
        while(!queue[slot].get());
        return;
    }
    void unlock() {
        queue[slot].set(false);
        queue[(slot+1)%numThreads].set(true);
        return;
    }

    @Override
    public void run() {
        for(int i = 0; i < (operations/numThreads); i++){
            lock();
            count++;
            unlock();
        }
        return;
    }
}
