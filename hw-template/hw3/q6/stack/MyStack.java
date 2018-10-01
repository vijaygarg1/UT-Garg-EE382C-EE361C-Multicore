// You do NOT need to modify this file
// The interface of the MyStack
// You should implement LockStack and LockFreeStack by extending this class
package stack;

public interface MyStack {
	// return true if successfully push a new value
    public boolean push(Integer value);
    public Integer pop() throws EmptyStack;
}
