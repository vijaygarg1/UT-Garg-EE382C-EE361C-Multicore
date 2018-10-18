package queue;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class LockQueue implements MyQueue {
    private ReentrantLock enqLock = new ReentrantLock();
    private ReentrantLock deqLock = new ReentrantLock();

    private Node tail;
    private Node head;

    public LockQueue() {
        head = new Node(null);
        tail = head;
    }
  
    public boolean enq(Integer value) {
        if (value == null) {
            return false;
        }
        enqLock.lock();
        try {
            Node n = new Node(value);
            tail.next = n;
            tail = n;
        } finally {
            enqLock.unlock();
        }
        return true;
    }
  
    public Integer deq() {
        Integer retval = null;
        deqLock.lock();
        try {
            if (head.next != null) {
                retval = head.next.value;
                head = head.next;
            }
        } finally {
            deqLock.unlock();
        }
        return retval;
    }
    public boolean find(Integer value) {
        Node curr = head;
        while (curr.next != null) {
            if (curr.next.value.equals(value)) {
                return true;
            }
            curr = curr.next;
        }
        return false;
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
