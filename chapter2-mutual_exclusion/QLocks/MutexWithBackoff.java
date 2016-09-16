import java.util.concurrent.atomic.*;

public class MutexWithBackOff{
    AtomicBoolean isOccupied = new AtomicBoolean(false);
    public void lock() {
	while (true) {
	   while (isOccupied.get()) {
	   }
           if (!isOccupied.getAndSet(true)) return;
	   else {
		int timeToSleep = calculateDuration();
		Thread.sleep(timeToSleep);
	   }
        }
    }
    public void unlock() {
        isOccupied.set(false);
    }
}

