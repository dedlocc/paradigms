package queue;

import java.util.Arrays;

/*
 * Model: [a1, a2, a3, ..., aN]
 *
 * Inv:
 *     N >= 0
 *     for each i in [1, N], a[i] != null
 *
 * M(x, y, k = 0): for each i in [x, y], a[i + k] == a'[i]
 * Immutable: N == N' && M(1, N)
 */
public class ArrayQueueModule {
    private static Object[] array = new Object[16];
    private static int head, size;

    // Pre:  e != null
    // Post: a[N] == e && N == N' + 1 && M(1, N')
    public static void enqueue(final Object e) {
        assert null != e;

        growIfNeeded();

        array[tail()] = e;
        ++size;
    }

    // Pre:  N > 0
    // Post: R == a[1] && Immutable
    public static Object element() {
        assert !isEmpty();
        return array[head];
    }

    // Pre:  N > 0
    // Post: R == a'[1] && N == N' - 1 && M(2, N', -1)
    public static Object dequeue() {
        assert !isEmpty();

        final var e = array[head];
        array[head] = null;
        head = increment(head);
        --size;
        return e;
    }

    // Pre:  e != null
    // Post: a[1] == e && N == N' + 1 && M(1, N', +1)
    public static void push(final Object e) {
        assert null != e;

        growIfNeeded();

        array[head = decrement(head)] = e;
        ++size;
    }

    // Pre:  N > 0
    // Post: R == a[N] && Immutable
    public static Object peek() {
        assert !isEmpty();
        return array[decrement(tail())];
    }

    // Pre:  N > 0
    // Post: R == a[N] && N == N' - 1 && M(1, N)
    public static Object remove() {
        assert !isEmpty();

        --size;
        final var e = array[tail()];
        array[tail()] = null;
        return e;
    }

    // Pre:  true
    // Post: R == N && Immutable
    public static int size() {
        return size;
    }

    // Pre:  true
    // Post: R == (N == 0) && Immutable
    public static boolean isEmpty() {
        return 0 == size;
    }

    // Pre:  true
    // Post: N == 0
    public static void clear() {
        if (0 == size) {
            return;
        }

        if (head < tail()) {
            Arrays.fill(array, head, tail(), null);
        } else {
            Arrays.fill(array, head, array.length, null);
            Arrays.fill(array, 0, tail(), null);
        }

        head = size = 0;
    }

    private static void growIfNeeded() {
        if (size == array.length) {
            final var newArray = Arrays.copyOfRange(array, head, 2 * array.length);
            System.arraycopy(array, 0, newArray, array.length - head, tail());

            head = 0;
            array = newArray;
        }
    }

    private static int increment(final int index) {
        return array.length == 1 + index ? 0 : 1 + index;
    }

    private static int decrement(final int index) {
        return (0 == index ? array.length : index) - 1;
    }

    private static int tail() {
        final var t = head + size;
        return t < array.length ? t : t - array.length;
    }
}
