package stack;

import org.junit.Assert;
import org.junit.Test;
import queue.LockFreeQueue;
import queue.LockQueue;
import queue.MyQueue;

public class StackTest {


    class ThreadQ1 implements Runnable {

        private MyQueue q;

        ThreadQ1(MyQueue q) {
            this.q = q;
        }

        // implement run method for thread
        public void run() {
            q.enq(3);
        }
    }
    class ThreadQ2 implements Runnable {

        private MyQueue q;

        ThreadQ2(MyQueue q) {
            this.q = q;
        }

        // implement run method for thread
        public void run() {
            q.enq(5);
        }
    }
    class ThreadQ3 implements Runnable {

        private MyQueue q;
        private volatile int value;

        ThreadQ3(MyQueue q) {
            this.q = q;
        }

        // implement run method for thread
        public void run() {
            value = q.deq();
        }
        public int getValue() {
            return value;
        }
    }
    @Test
    public void testLFSingleEnq() {
        LockFreeQueue q = new LockFreeQueue();
        q.enq(5);
        Assert.assertTrue(q.find(5));
    }
    @Test
    public void testLFDoubleEnq() {
        LockFreeQueue q = new LockFreeQueue();
        q.enq(5);
        q.enq(3);
        Assert.assertTrue(q.find(5));
        Assert.assertTrue(q.find(3));
    }
    @Test
    public void testLFSingleEnqDeq() {
        LockFreeQueue q = new LockFreeQueue();
        q.enq(5);
        Assert.assertTrue(q.find(5));
        Assert.assertEquals(5, (int) q.deq());
    }
    @Test
    public void testLFDoubleEnqDeq() {
        LockFreeQueue q = new LockFreeQueue();
        q.enq(5);
        q.enq(3);
        Assert.assertEquals(5, (int)q.deq());
        Assert.assertFalse(q.find(5));
        Assert.assertTrue(q.find(3));
    }
    @Test
    public void testLFDoubleEnqConc() {
        LockFreeQueue q = new LockFreeQueue();
        Thread a = new Thread(new ThreadQ1(q));
        Thread b = new Thread(new ThreadQ2(q));
        a.start(); b.start();
        try {
            a.join();
            b.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int val1 = q.deq();
        Assert.assertTrue(val1 == 5 || val1 == 3);
        int val2 = q.deq();
        Assert.assertTrue(val2 == 5 || val2 == 3);
    }
    @Test
    public void testLFDoubleEnqDeqConc() {
        LockFreeQueue q = new LockFreeQueue();
        Thread a = new Thread(new ThreadQ1(q));
        Thread b = new Thread(new ThreadQ2(q));
        a.start(); b.start();

        try {
            a.join();
            b.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ThreadQ3 q3c = new ThreadQ3(q);
        ThreadQ3 q3d = new ThreadQ3(q);
        Thread c = new Thread(q3c);
        Thread d = new Thread(q3d);
        c.start(); d.start();

        try {
            c.join();
            d.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int val1 = q3c.getValue();
        Assert.assertTrue(val1 == 5 || val1 == 3);
        int val2 = q3d.getValue();
        Assert.assertTrue(val2 == 5 || val2 == 3);
    }

    @Test
    public void testLSingleEnq() {
        LockQueue q = new LockQueue();
        q.enq(5);
        Assert.assertTrue(q.find(5));
    }
    @Test
    public void testLDoubleEnq() {
        LockQueue q = new LockQueue();
        q.enq(5);
        q.enq(3);
        Assert.assertTrue(q.find(5));
        Assert.assertTrue(q.find(3));
    }
    @Test
    public void testLSingleEnqDeq() {
        LockQueue q = new LockQueue();
        q.enq(5);
        Assert.assertTrue(q.find(5));
        Assert.assertEquals(5, (int) q.deq());
    }
    @Test
    public void testLDoubleEnqDeq() {
        LockQueue q = new LockQueue();
        q.enq(5);
        q.enq(3);
        Assert.assertEquals(5, (int)q.deq());
        Assert.assertFalse(q.find(5));
        Assert.assertTrue(q.find(3));
    }

    @Test
    public void testLDoubleEnqConc() {
        LockQueue q = new LockQueue();
        Thread a = new Thread(new ThreadQ1(q));
        Thread b = new Thread(new ThreadQ2(q));
        a.start(); b.start();
        try {
            a.join();
            b.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int val1 = q.deq();
        Assert.assertTrue(val1 == 5 || val1 == 3);
        int val2 = q.deq();
        Assert.assertTrue(val2 == 5 || val2 == 3);
    }
    @Test
    public void testLDoubleEnqDeqConc() {
        LockQueue q = new LockQueue();
        Thread a = new Thread(new ThreadQ1(q));
        Thread b = new Thread(new ThreadQ2(q));
        a.start(); b.start();

        try {
            a.join();
            b.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ThreadQ3 q3c = new ThreadQ3(q);
        ThreadQ3 q3d = new ThreadQ3(q);
        Thread c = new Thread(q3c);
        Thread d = new Thread(q3d);
        c.start(); d.start();

        try {
            c.join();
            d.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int val1 = q3c.getValue();
        Assert.assertTrue(val1 == 5 || val1 == 3);
        int val2 = q3d.getValue();
        Assert.assertTrue(val2 == 5 || val2 == 3);
    }

}
