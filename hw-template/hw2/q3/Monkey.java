package q3;

import java.util.Arrays;
import java.util.concurrent.locks.*;

public class Monkey {
    private static class ClimbingPrivilege {
        final private ReentrantLock lock = new ReentrantLock();
        final Condition noOpposingMonkeys = lock.newCondition();

        public int direction = -1;

        // Only called by kong
        public void lock() {
            lock.lock();
        }

        public boolean tryLock(int direction) throws InterruptedException {
            boolean lockFree = lock.tryLock();
            if (lockFree) {
                noOpposingMonkeys.await();
                this.direction = direction;
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
    }

//    private static ClimbingPrivilege[] availableClimbSpots;

//    private ThreadLocal<ClimbingPrivilege> myLock;

    private static boolean kongOnRope = false;
    private static int numMonkeysOnRope = 0;
    private static int directions[] = {-2, -2, -2};
    private ThreadLocal<Integer> monkeyIndex;

    private static final ReentrantLock ropeLock = new ReentrantLock();
    private static final Condition noMonkeys = ropeLock.newCondition();
    private static final Condition noOpposingMonkeys = ropeLock.newCondition();
    private static final Condition notFull = ropeLock.newCondition();
    private static final Condition noKong = ropeLock.newCondition();

    static int oppWaits = 0;
    static int fullWaits = 0;

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
//        System.out.println("direction: " + direction);
        ropeLock.lock();
//        System.out.println("Entering, monkeys: " + numMonkeysOnRope);
        String reason = "new";
        try {
            if (direction == -1) {
                kongOnRope = true;
//            ropeLock.lock();
                while (numMonkeysOnRope > 0) {
                    System.out.println("KONG IS WAITING");
                    noMonkeys.await();
                    System.out.println("KONG IS DONE WAITING");
                }
            } else {
                while (kongOnRope) {
//                    System.out.println("Awaiting kong");
//                    System.out.println("Awaiting kong: " + Thread.currentThread().getId());
                    noKong.await();
                    reason = "Kong";
//                    System.out.println("kong left, numMonkeys: " + numMonkeysOnRope);
                }
                while (AreOpposingMonkeys(direction)) {
//                    System.out.println("Awaiting opposers: " + Thread.currentThread().getId());
                    noOpposingMonkeys.await();
                    reason = "Opposing";
                    System.out.println("opposers may have left. numMonkeys: " + numMonkeysOnRope);
                }
                while (numMonkeysOnRope == 3) {
//                    System.out.println("Awaiting full: " + Thread.currentThread().getId());
                    notFull.await();
                    reason = "full";
//                    System.out.println("full may not be true" + Thread.currentThread().getId());
                }

            }
            int position = 3;  // Error will occur if not found
            for (int i = 0; i < 3; i++) {
                if (directions[i] == -2) {
                    position = i;
                    break;
                }
            }
            if (position == 3) {
                System.out.println("POSITION SHOULD NOT BE 3. numMonkeys: " + numMonkeysOnRope + " reason: " + reason);
                System.out.println(Arrays.toString(directions));
                Thread.sleep(999999999);
            }
            if (direction == -1) {
                position = 0;
                System.out.println("KONG GETTING ON");
            }
            try {
                int orig = directions[position];
                directions[position] = direction;
                System.out.println("Setting direction. position: " + position + " dir: " + directions[position] + " orig: " + orig + "reason: " + reason + " " + Arrays.toString(directions));
//                System.out.println(Arrays.toString(directions));
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
//                System.out.println(Arrays.toString(directions));
            }
//            System.out.println("num Monkeys: " + numMonkeysOnRope);
//            System.out.println(Arrays.toString(directions));
            // TODO: Either direction isn't updating right, or monkeys are jumping the gun
            monkeyIndex.set(position);
//            System.out.println("Setting index");
//            System.out.println(position);
            numMonkeysOnRope++;
            System.out.println("Entered, monkeys: " + numMonkeysOnRope);
        } finally {
            ropeLock.unlock();
        }
    }

    public void LeaveRope() {
//        System.out.println("Inside LeaveRope");
        ropeLock.lock();
//        System.out.println("Leaving, monkeys: " + numMonkeysOnRope);

        try {
//            System.out.println(monkeyIndex.get());
            if (monkeyIndex.get() >= 3 || monkeyIndex.get() < 0) {
                System.out.println("BAD INDEX: " + monkeyIndex.get());
            }
            int myDirection = directions[monkeyIndex.get()];
            directions[monkeyIndex.get()] = -2;

            numMonkeysOnRope--;
            if (numMonkeysOnRope < 0) {
                System.out.println("num under 0");
            }
//            System.out.println("Num monkeys: " + numMonkeysOnRope);


            if (myDirection == -1) {
                System.out.println("Kong getting off, index: " + monkeyIndex.get() + " " + Arrays.toString(directions));
//                directions[monkeyIndex.get()] = -2;
                kongOnRope = false;
                System.out.println("SETTING KONG FALSE");
                noKong.signalAll();
            }
//            if (ropeLock.hasWaiters(noOpposingMonkeys)) {
//                noOpposingMonkeys.signal();
//            }

            notFull.signal();

             if (numMonkeysOnRope == 0) {
                System.out.println("SIGNALLING NO MONKEYS");
                noMonkeys.signal();
                noOpposingMonkeys.signal();
            }
            monkeyIndex.set(-1);


//            fullWaits--;
//            System.out.println("Signalling the next monkey to go");
//            oppWaits--;


        } finally {
//            System.out.println("Left, monkeys: " + numMonkeysOnRope);
//            System.out.println("oppwaits: " + oppWaits);
//            System.out.println("fullwaits: " + fullWaits);
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
//        if (kongOnRope) {
//            return 1;
//        }
//        return re1.getHoldCount() + re2.getHoldCount() + re3.getHoldCount();
        return numMonkeysOnRope;
    }
}
