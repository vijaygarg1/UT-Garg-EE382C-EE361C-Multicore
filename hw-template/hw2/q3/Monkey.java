package q3;

import java.util.concurrent.locks.ReentrantLock;

public class Monkey {
    private static class ClimbingPrivilege {
        private ReentrantLock lock;
        public int direction = -1;

        public ClimbingPrivilege() {
            lock = new ReentrantLock();
        }

        // Only called by kong
        public void lock() {
            lock.lock();
        }

        public boolean tryLock(int direction) {
            boolean lockFree = lock.tryLock();
            if (lockFree) {
                if (noOpposingMonkeys(direction)) {
                    this.direction = direction;
                } else {
                    lock.unlock();
                    lockFree = false;
                }
            }
            return lockFree;
        }

        public void unlock() {
            direction = -1;
            lock.unlock();
        }

        public int getHoldCount() {
            return lock.getHoldCount();
        }

        private boolean noOpposingMonkeys(int direction) {
            return ((Monkey.re1.direction == direction
                    || Monkey.re1.direction == -1)
                    && (Monkey.re2.direction == direction
                    || Monkey.re2.direction == -1)
                    && (Monkey.re3.direction == direction
                    || Monkey.re3.direction == -1));
        }
    }

    private static ClimbingPrivilege re1 = new ClimbingPrivilege();
    private static ClimbingPrivilege re2 = new ClimbingPrivilege();
    private static ClimbingPrivilege re3 = new ClimbingPrivilege();

    private ThreadLocal<ClimbingPrivilege> myLock;

    private static boolean kongOnRope = false;

    public Monkey() {
        myLock = ThreadLocal.withInitial(() -> new ClimbingPrivilege());
    }

    public void ClimbRope(int direction) throws InterruptedException {
        if (direction == -1) {
            try {
                re1.lock();
                re2.lock();
                re3.lock();
                myLock.set(re1);
            } catch (Exception e) {
                throw new InterruptedException();
            }
            kongOnRope = true;
        }
        else {
            while (kongOnRope) {}
            boolean available = false;
            try {
                while (!available) {
                    if (re1.tryLock(direction)) {
                        myLock.set(re1);
                        available = true;
                    } else if (re2.tryLock(direction)) {
                        myLock.set(re2);
                        available = true;
                    } else if (re3.tryLock(direction)) {
                        myLock.set(re3);
                        available = true;
                    }
                    // Without this, my computer can't handle it lol
                    Thread.sleep(5);
                }
            } catch (Exception e) {
                throw new InterruptedException();
            }
        }
    }

    public void LeaveRope() {
        if (myLock.get().direction == -1) {
            kongOnRope = false;
        }
        myLock.get().unlock();
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
        if (kongOnRope) {
            return 1;
        }
        return re1.getHoldCount() + re2.getHoldCount() + re3.getHoldCount();
    }
}
