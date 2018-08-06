// pseudo-code for Anderson Lock
public class AndersonLock {
    AtomicInteger tailSlot = new AtomicInteger(0);
    boolean [] Available;
    ThreadLocal<Integer> mySlot ;//initialize to 0

    public AndersonLock(int n) { // constructor
     // all Available false except Available[0]
    }
    public void lock() {
      mySlot.set(tailSlot.getAndIncrement() % n);
      spinUntil(Available[mySlot]);
    }
    public void unlock() {
        Available[mySlot.get()] = false;
        Available[(mySlot.get()+1) % n] = true;
    }
}

