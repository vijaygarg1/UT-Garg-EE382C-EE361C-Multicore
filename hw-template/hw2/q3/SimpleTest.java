package q3;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class SimpleTest {
    private static ArrayList<Long> activeThreads = new ArrayList<>();
    private static int kongID = -1;

    // monkey with different directions can be executed by threads
    class ThreadMonkey implements Runnable {

        private Monkey monkey;

        private int direction;

        private int ix;

        ThreadMonkey(Monkey monkey, int direction, int ix) {
            this.monkey = monkey;
            this.direction = direction;
            this.ix = ix;
        }

        // implement run method for thread
        public void run() {
            try {
                monkey.ClimbRope(direction);  // climb rope with direction
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            monkey.LeaveRope();  // leave rope
        }
    }

    // test for one direction monkey
    @Test
    public void testOneDirectionMonkey() {

        Monkey monkey = new Monkey();
//        System.out.println("Starting");

        Thread[] threads = new Thread[1000];
        for (int i = 0; i < 1000; ++i) {
            // all thread monkeys have one same direction
            threads[i] = new Thread(new ThreadMonkey(monkey, 1, i));
            threads[i].start();
            // assert at any time the number of monkeys on the rope is <= 3
            Assert.assertTrue(monkey.getNumMonkeysOnRope() <= 3);
            Assert.assertTrue(monkey.getNumMonkeysOnRope() >= 0);
            Assert.assertTrue(noMonkeyFights());
        }

        // assert at any time the number of monkeys on the rope is <= 3
        waitThreadsFinishAndTest(monkey, threads);

    }

    // test for two directions monkey
    @Test
    public void testTwoDirectionMonkey() {

        Monkey monkey = new Monkey();

        Thread[] threads = new Thread[1000];
        for (int i = 0; i < 1000; ++i) {
            // random generate some directions 0 or 1
            threads[i] = new Thread(new ThreadMonkey(monkey, (int) Math.round(Math.random()), i));
            threads[i].start();
            Assert.assertTrue(monkey.getNumMonkeysOnRope() <= 3);
            Assert.assertTrue(monkey.getNumMonkeysOnRope() >= 0);
            Assert.assertTrue(noMonkeyFights());
        }

        waitThreadsFinishAndTest(monkey, threads);

    }


    // test for two directions monkey with Kong
    @Test
    public void testTwoDirectionMonkeyWithKong() {

        Monkey monkey = new Monkey();

        // we have 1000 monkeys
        Thread[] threads = new Thread[1000];

        // random generate the Kong, only one thread
        Random random = new Random();
        int kongThreadID = random.nextInt(1000);
        kongID = kongThreadID;

        // you need to make sure if Kong is start, the other monkey must stop and clear the rope
        for (int i = 0; i < 1000; ++i) {
            if (i == kongThreadID) threads[i] = new Thread(new ThreadMonkey(monkey, -1, i));
            else threads[i] = new Thread(new ThreadMonkey(monkey, (int) Math.round(Math.random()), i));
            threads[i].start();
            Assert.assertTrue(monkey.getNumMonkeysOnRope() <= 3);
            Assert.assertTrue(monkey.getNumMonkeysOnRope() >= 0);
            Assert.assertTrue(noMonkeyFights());
        }

        waitThreadsFinishAndTest(monkey, threads);
    }

    // helper function to wait all threads finish and check rope
    private void waitThreadsFinishAndTest(Monkey monkey, Thread[] threads) {
        for (Thread thread : threads) {
            try {
                Assert.assertTrue(monkey.getNumMonkeysOnRope() <= 3);
                Assert.assertTrue(monkey.getNumMonkeysOnRope() >= 0);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean noMonkeyFights() {
        boolean noFighting = true;
        if (Monkey.directions[0] >= 0) {
            if (Monkey.directions[1] != 2 && (Monkey.directions[0]^Monkey.directions[1]) == 1) {
                noFighting = false;
            }
            if (Monkey.directions[2] != 2 && (Monkey.directions[0]^Monkey.directions[2]) == 1) {
                noFighting = false;
            }
        }
        if (Monkey.directions[1] >= 0) {
            if (Monkey.directions[2] != 2 && (Monkey.directions[1]^Monkey.directions[2]) == 1) {
                noFighting = false;
            }
        }
        return noFighting;
    }

}
