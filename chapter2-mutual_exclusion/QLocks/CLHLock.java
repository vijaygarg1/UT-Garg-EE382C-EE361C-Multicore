import java.util.concurrent.atomic.*;
public class CLHLock implements MyLock {
    class Node {
        boolean locked;
    }
    AtomicReference<Node> tailNode;
    ThreadLocal<Node> myNode;
    ThreadLocal<Node> pred;

    public CLHLock() { 
       tailNode = new AtomicReference<Node>(new Node());
       tailNode.get().locked = false;
       myNode = new ThreadLocal<Node> () {
          protected Node initialValue() {
               return new Node();
          }
       };
       pred = new ThreadLocal<Node> ();
     
    }
    public void lock() {
      myNode.get().locked = true;
      pred.set(tailNode.getAndSet(myNode.get()));
      while(pred.get().locked) { Thread.yield(); };
    }
    public void unlock() {
        myNode.get().locked = false;
        myNode.set(pred.get()); // reusing predecessor node for future use
    }
}

