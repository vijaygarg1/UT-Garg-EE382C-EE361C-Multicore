package stack;

public class LockFreeStack implements MyStack {
// you are free to add members
	
  public LockFreeStack() {
	  // implement your constructor here
  }
	
  public boolean push(Integer value) {
	  // implement your push method here
	  return false;
  }
  
  public Integer pop() throws EmptyStack {
	  // implement your pop method here
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
