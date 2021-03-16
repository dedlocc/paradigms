package queue;

public class LinkedQueue extends AbstractQueue {
    private Node head, tail;

    @Override
    public void enqueueImpl(final Object e) {
        final var node = new Node(e);

        if (null == tail) {
            head = node;
        } else {
            tail.next = node;
        }

        tail = node;
    }

    @Override
    public Object elementImpl() {
        return head.value;
    }

    @Override
    public Object dequeueImpl() {
        final var node = head;
        head = node.next;
        if (null == head) {
            tail = null;
        }
        return node.value;
    }

    @Override
    public void clearImpl() {
        head = tail = null;
    }

    private static class Node {
        private Node next;
        private final Object value;

        public Node(final Object value) {
            this.value = value;
        }
    }
}
