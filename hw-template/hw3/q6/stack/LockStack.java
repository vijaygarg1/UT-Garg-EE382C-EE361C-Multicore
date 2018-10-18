package stack;

import java.util.concurrent.locks.ReentrantLock;

public class LockStack implements MyStack {
    Node top;
    ReentrantLock lock;
	
    public LockStack() {
        top = null;
        lock = new ReentrantLock();
    }

    public boolean push(Integer value) {
        lock.lock();
        try {
            Node node = new Node(value);
            node.next = top;
            top = node;
        } finally {
            lock.unlock();
        }
        return false;
    }

    public Integer pop() throws EmptyStack {
        lock.lock();
        if (top == null) {
            lock.unlock();
            throw new EmptyStack();
        }
        Node oldTop = top;
        try {
            top = top.next;
        } finally {
            lock.unlock();
        }
        return oldTop.value;
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
