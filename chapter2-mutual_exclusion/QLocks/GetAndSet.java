import java.util.concurrent.atomic.*;

public class GetAndSet implements MyLock {
    AtomicBoolean isOccupied = new AtomicBoolean(false);
    public void lock() {
	while (isOccupied.getAndSet(true)) {
	    Thread.yield();
	   // skip();
        }
    }
    public void unlock() {
        isOccupied.set(false);
    }
}

