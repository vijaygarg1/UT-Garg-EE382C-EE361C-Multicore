package q3;

import java.util.concurrent.locks.*;

public class Monkey {
    static private int count = 0;
    final private Lock lock = new ReentrantLock();
    final private Condition getOnRope  = lock.newCondition();
    final private Condition kongCond = lock.newCondition();
    static private int dir = -2;
    static private boolean kongWaiting = false;
    static private boolean kongOnBridge = false;

    public Monkey() {

    }

    public void ClimbRope(int direction) throws InterruptedException {
        lock.lock();
        try {
            while (direction == -1 && dir != direction) {
                kongWaiting = true;
                System.out.println("Kong Thread ID: " + Thread.currentThread().getId());
                kongCond.await();
            }
            if (dir == -2) {
                dir = direction;
            }
            while (count >= 3 || (dir != direction && dir != -2)) {
//                System.out.println("Monkey: " + Thread.currentThread().getId() + " is waiting to go: " + direction + ". Num monkeys on rope: " + count);
                getOnRope.await();
            }
            if (kongWaiting && dir == -1) {
                kongOnBridge = true;
            }
            count++;
        } catch (Exception e) {
            System.out.println("Error");
        } finally{
            System.out.println("Monkey: " + Thread.currentThread().getId() + " is on rope going in direction: " + direction + ". Num monkeys on rope: " + count);
            lock.unlock();
        }
    }

    public void LeaveRope() {
        lock.lock();
        try {
            count--;
            if (count == 0) {
                if (kongWaiting && !kongOnBridge) {
                    kongCond.signal();
                    dir = -1;
                } else if (kongOnBridge) {
                    dir = -2;
                    kongOnBridge = false;
                    kongWaiting = false;
                    getOnRope.signalAll();
                } else {
                    dir = -2;
                }
            }
            if (count < 3) {
                getOnRope.signalAll();
            }
        } catch (Exception e) {
            System.out.println("Error");
        } finally {
            System.out.println("Monkey: " + Thread.currentThread().getId() + " is off rope. Num monkeys on rope: " + count);
            lock.unlock();
        }
    }

    /**
     * Returns the number of monkeys on the rope currently for test purpose.
     *
     * @return the number of monkeys on the rope
     *
     * Positive Test Cases:
     * case 1: when normal monkey (0 and 1) is on the rope, this value should <= 3, >= 0
     * case 2: when Kong is on the rope, this value should be 1
     */
    public int getNumMonkeysOnRope() {
        return count;
    }

}
