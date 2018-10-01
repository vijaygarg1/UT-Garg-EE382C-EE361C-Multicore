package queue;

public class LockFreeQueue implements MyQueue {
// you are free to add members

  public LockFreeQueue() {
	// implement your constructor here
  }

  public boolean enq(Integer value) {
	// implement your enq method here
    return false;
  }
  
  public Integer deq() {
	// implement your deq method here
    return null;
  }
  
  protected class Node {
	  public Integer value;
	  public Node next;
		    
	  public Node(Integer x) {
		  value = x;
		  next = null;
	  }
  }
}
