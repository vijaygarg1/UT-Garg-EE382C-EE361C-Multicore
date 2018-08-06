import java.util.concurrent.atomic.*;

public class GetAndGetAndSet implements MyLock {
    AtomicBoolean isOccupied = new AtomicBoolean(false);
    public void lock() {
	while (true) {
	   while (isOccupied.get()) {
	   }
           if (!isOccupied.getAndSet(true)) return;
        }
    }
    public void unlock() {
        isOccupied.set(false);
    }
}

