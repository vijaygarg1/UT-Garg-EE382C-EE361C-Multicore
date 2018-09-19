package q3;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class SimpleTest {

    // monkey with different directions can be executed by threads
    class ThreadMonkey implements Runnable {

        private Monkey monkey;

        private int direction;

        ThreadMonkey(Monkey monkey, int direction) {
            this.monkey = monkey;
            this.direction = direction;
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

        Thread[] threads = new Thread[1000];
        for (int i = 0; i < 1000; ++i) {
            // all thread monkeys have one same direction
            threads[i] = new Thread(new ThreadMonkey(monkey, 1));
            threads[i].start();
            // assert at any time the number of monkeys on the rope is <= 3
            Assert.assertTrue(monkey.getNumMonkeysOnRope() <= 3);
            Assert.assertTrue(monkey.getNumMonkeysOnRope() >= 0);
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
            threads[i] = new Thread(new ThreadMonkey(monkey, (int) Math.round(Math.random())));
            threads[i].start();
            Assert.assertTrue(monkey.getNumMonkeysOnRope() <= 3);
            Assert.assertTrue(monkey.getNumMonkeysOnRope() >= 0);
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

        // you need to make sure if Kong is start, the other monkey must stop and clear the rope
        for (int i = 0; i < 1000; ++i) {
            if (i == kongThreadID) threads[i] = new Thread(new ThreadMonkey(monkey, -1));
            else threads[i] = new Thread(new ThreadMonkey(monkey, (int) Math.round(Math.random())));
            threads[i].start();
            Assert.assertTrue(monkey.getNumMonkeysOnRope() <= 3);
            Assert.assertTrue(monkey.getNumMonkeysOnRope() >= 0);
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

}
