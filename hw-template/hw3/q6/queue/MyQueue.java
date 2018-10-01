// You do NOT need to modify this file
// The interface of the MyQueue
// You should implement LockQueue and LockFreeQueue by extending this class
package queue;
public interface MyQueue {
	// return true if successfully enqueue a new value
    public boolean enq(Integer value);
    public Integer deq();
}
