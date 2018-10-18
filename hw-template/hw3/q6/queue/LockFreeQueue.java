package queue;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class LockFreeQueue implements MyQueue {
    private AtomicStampedReference<Node> head;
    private AtomicStampedReference<Node> tail;
// you are free to add members

    public LockFreeQueue() {
        // implement your constructor here
        Node n = new Node(null);
        head = new AtomicStampedReference<>(n, 0);
        tail = new AtomicStampedReference<>(n, 0);
    }

    public boolean enq(Integer value) {
        // implement your enq method here
        Node n = new Node(value);
        int[] tailHolder = new int[1];
        Node temp_tail;
        while (true) {
            temp_tail = tail.get(tailHolder);
            int[] nextHolder = new int[1]; Node next = temp_tail.next.get(nextHolder);
            if (temp_tail == tail.getReference()) {
                if (next == null) {
                    // Enqueue done
                    if (temp_tail.next.compareAndSet(next, n, nextHolder[0], nextHolder[0] + 1)) {
                        break;
                    }
                } else {
                    // Helper
                    tail.compareAndSet(temp_tail, next, tailHolder[0], tailHolder[0] + 1);
                }
            }
        }
        tail.compareAndSet(temp_tail, n, tailHolder[0], tailHolder[0] + 1);
        return true;
    }

    public Integer deq() {
        // implement your deq method here
        Integer retval;
        int[] headHolder = new int[1];
        Node temp_head;
        while (true) {
            temp_head = head.get(headHolder);
            int[] tailHolder = new int[1]; Node temp_tail = tail.get(tailHolder);
            int[] nextHolder = new int[1]; Node next = temp_head.next.get(nextHolder);
            if (temp_head == head.getReference()) {
                if (temp_head == temp_tail) {
                    if (next == null) {
                        return null;
                    }
                    tail.compareAndSet(temp_tail, next, tailHolder[0], tailHolder[0] + 1);
                } else {
                    retval = next.value;
                    if (head.compareAndSet(temp_head, next, headHolder[0], headHolder[0] + 1)) {
                        break;
                    }
                }
            }
        }
        return retval;
    }

    // FOR TESTING
    public boolean find(Integer value) {
        Node curr = head.getReference();
        while (curr.next != null && curr.next.getReference() != null) {
            if (curr.next.getReference().value.equals(value)) {
                return true;
            }
            curr = curr.next.getReference();
        }
        return false;
    }

    protected class Node {
        public Integer value;
        public AtomicStampedReference<Node> next;

        public Node(Integer x) {
            value = x;
            next = new AtomicStampedReference<>(null, 0);
        }
    }
}
