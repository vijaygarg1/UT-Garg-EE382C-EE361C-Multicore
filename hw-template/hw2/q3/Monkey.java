package q3;

import java.util.concurrent.locks.*;

public class Monkey {

    private static boolean kongOnRope = false;
    private static int numMonkeysOnRope = 0;
    public static int directions[] = {-2, -2, -2};
    private ThreadLocal<Integer> monkeyIndex;

    private static final ReentrantLock ropeLock = new ReentrantLock();
    private static final Condition noMonkeys = ropeLock.newCondition();
    private static final Condition noKong = ropeLock.newCondition();
    private static final Condition maybeSafeToCross = ropeLock.newCondition();

    public Monkey() {
        monkeyIndex = ThreadLocal.withInitial(() -> -1);
    }

    private boolean AreOpposingMonkeys(int direction) {
        boolean areOpposers = false;
        for (int dir : directions) {
            if (!(dir == direction || dir == -2)) {
                areOpposers = true;
            }
        }
        return areOpposers;
    }

    public void ClimbRope(int direction) throws InterruptedException {
        ropeLock.lock();
        try {
            if (direction == -1) {
                kongOnRope = true;
                while (numMonkeysOnRope > 0) {
                    noMonkeys.await();
                }
            } else {
                while (kongOnRope) {
                    noKong.await();
                }
                while (AreOpposingMonkeys(direction) || numMonkeysOnRope == 3) {
                    maybeSafeToCross.await();
                }

            }
            int position = 3;  // Error will occur if not found
            for (int i = 0; i < 3; i++) {
                if (directions[i] == -2) {
                    position = i;
                    break;
                }
            }
            if (direction == -1) {
                position = 0;
            }
            try {
                directions[position] = direction;
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            monkeyIndex.set(position);
            numMonkeysOnRope++;
        } finally {
            ropeLock.unlock();
        }
    }

    public void LeaveRope() {
        ropeLock.lock();

        try {
            if (monkeyIndex.get() >= 3 || monkeyIndex.get() < 0) {
            }
            int myDirection = directions[monkeyIndex.get()];
            directions[monkeyIndex.get()] = -2;

            numMonkeysOnRope--;
            if (numMonkeysOnRope < 0) {
            }

            if (myDirection == -1) {
                kongOnRope = false;
                noKong.signalAll();
            }

            maybeSafeToCross.signal();

            if (numMonkeysOnRope == 0) {
                noMonkeys.signal();
            }
            monkeyIndex.set(-1);
        } finally {
            ropeLock.unlock();
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
        return numMonkeysOnRope;
    }

}
