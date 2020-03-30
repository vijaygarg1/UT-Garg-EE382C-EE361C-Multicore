package q2.a;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

class Node {
    AtomicBoolean locked = new AtomicBoolean(true);
}

public class PIncrement implements Runnable {



    private static AtomicReference<Node> getTail() {
        return tail;
    }

    private static void setCount(int count) {
        PIncrement.count = count;
    }

    private static void setNumThreads(int numThreads) {
        PIncrement.numThreads = numThreads;
    }

    static final private int operations = 120000;
    static int count;
    static private int numThreads;
    static private AtomicReference<Node> tail = new AtomicReference<>(new Node());


    private ThreadLocal<Node> spinNode = new ThreadLocal<Node>() {
        @Override public Node initialValue() {
            return new Node();
        }
    };
    private ThreadLocal<Node> pred = new ThreadLocal<Node>() {
        @Override public Node initialValue() {
            return new Node();
        }
    };

    @Override
    public void run() {
        for(int i = 0; i < (operations/numThreads); i++){
            lock();
            count++;
            unlock();
        }
    }


    public static int parallelIncrement(int c, int numThreads) {
        getTail().get().locked.set(false);
        setCount(c);
        setNumThreads(numThreads);

        ArrayList<Thread> threads = new ArrayList<>();

        for(int i = 0; i < numThreads; i++){
            PIncrement p = new PIncrement();
            Thread t = new Thread(p);
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
        return count;
    }

    private void lock(){
            spinNode.get().locked.set(true);
            pred.set(tail.getAndSet(spinNode.get()));
            while(pred.get().locked.get()) {
                Thread.yield();
            }
    }

    private void unlock() {
        spinNode.get().locked.set(false);
        spinNode.set(pred.get());
    }

}
