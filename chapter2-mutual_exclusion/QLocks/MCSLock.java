import java.util.concurrent.atomic.*;

public class MCSLock implements MyLock {
    class QNode {
        boolean locked;
	QNode next;
	QNode() {
	  locked = true;
	  next = null;
	}
    }
    AtomicReference<QNode> tailNode = new AtomicReference<QNode>(null);
    ThreadLocal<QNode> myNode;

    public MCSLock() { 
       myNode = new ThreadLocal<QNode> () {
          protected QNode initialValue() {
               return new QNode();
          }
       };
    }
    public void lock() {
      QNode pred = tailNode.getAndSet(myNode.get());
      if (pred != null){
          myNode.get().locked = true;
	  pred.next = myNode.get();
          while (myNode.get().locked){ Thread.yield(); };
      }
    }
    public void unlock() {
	if (myNode.get().next == null) {
            if (tailNode.compareAndSet(myNode.get(), null)) return;
            while (myNode.get().next == null) { Thread.yield(); };
        }
        myNode.get().next.locked = false;
        myNode.get().next = null;
    }
}

