package queue;

import java.util.Arrays;

public class ArrayQueue extends AbstractQueue {
    private Object[] array = new Object[16];
    private int head;

    @Override
    public void enqueueImpl(final Object e) {
        growIfNeeded();

        array[tail()] = e;
    }

    @Override
    public Object elementImpl() {
        return array[head];
    }

    @Override
    public Object dequeueImpl() {
        final var e = array[head];
        array[head] = null;
        head = increment(head);
        return e;
    }

    // Pre:  e != null
    // Post: a[1] == e && N == N' + 1 && M(1, N', +1)
    public void push(final Object e) {
        assert null != e;

        growIfNeeded();

        array[head = decrement(head)] = e;
        ++size;
    }

    // Pre:  N > 0
    // Post: R == a[N] && Immutable
    public Object peek() {
        assert !isEmpty();
        return array[decrement(tail())];
    }

    // Pre:  N > 0
    // Post: R == a[N] && N == N' - 1 && M(1, N)
    public Object remove() {
        assert !isEmpty();

        --size;
        final int tail = tail();
        final var e = array[tail];
        array[tail] = null;
        return e;
    }

    @Override
    public void clearImpl() {
        if (head < tail()) {
            Arrays.fill(array, head, tail(), null);
        } else {
            Arrays.fill(array, head, array.length, null);
            Arrays.fill(array, 0, tail(), null);
        }

        head = 0;
    }

    private void growIfNeeded() {
        if (size == array.length) {
            final var newArray = Arrays.copyOfRange(array, head, 2 * array.length);
            System.arraycopy(array, 0, newArray, array.length - head, tail());

            head = 0;
            array = newArray;
        }
    }

    private int increment(final int index) {
        return array.length == 1 + index ? 0 : 1 + index;
    }

    private int decrement(final int index) {
        return (0 == index ? array.length : index) - 1;
    }

    private int tail() {
        final var t = head + size;
        return t < array.length ? t : t - array.length;
    }
}
