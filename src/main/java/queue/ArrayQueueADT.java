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
public class ArrayQueueADT {
    private Object[] array = new Object[16];
    private int head, size;

    // Pre: true
    // Post: R != null && R is unique && R.N == 0
    public static ArrayQueueADT create() {
        return new ArrayQueueADT();
    }

    // Pre:  q != null && e != null
    // Post: a[N] == e && N == N' + 1 && M(1, N')
    public static void enqueue(final ArrayQueueADT q, final Object e) {
        assert null != q && null != e;

        growIfNeeded(q);

        q.array[tail(q)] = e;
        ++q.size;
    }

    // Pre:  q != null && N > 0
    // Post: R == a[1] && Immutable
    public static Object element(final ArrayQueueADT q) {
        assert null != q && !isEmpty(q);
        return q.array[q.head];
    }

    // Pre:  q != null && N > 0
    // Post: R == a'[1] && N == N' - 1 && M(2, N', -1)
    public static Object dequeue(final ArrayQueueADT q) {
        assert null != q && !isEmpty(q);

        final var e = q.array[q.head];
        q.array[q.head] = null;
        q.head = increment(q, q.head);
        --q.size;
        return e;
    }

    // Pre:  q != null && e != null
    // Post: a[1] == e && N == N' + 1 && M(1, N', +1)
    public static void push(final ArrayQueueADT q, final Object e) {
        assert null != q && null != e;

        growIfNeeded(q);

        q.array[q.head = decrement(q, q.head)] = e;
        ++q.size;
    }

    // Pre:  q != null && N > 0
    // Post: R == a[N] && Immutable
    public static Object peek(final ArrayQueueADT q) {
        assert null != q && !isEmpty(q);
        return q.array[decrement(q, tail(q))];
    }

    // Pre:  q != null && N > 0
    // Post: R == a[N] && N == N' - 1 && M(1, N)
    public static Object remove(final ArrayQueueADT q) {
        assert null != q && !isEmpty(q);

        --q.size;
        final var e = q.array[tail(q)];
        q.array[tail(q)] = null;
        return e;
    }

    // Pre:  q != null
    // Post: R == N && Immutable
    public static int size(final ArrayQueueADT q) {
        assert null != q;
        return q.size;
    }

    // Pre:  q != null
    // Post: R == (N == 0) && Immutable
    public static boolean isEmpty(final ArrayQueueADT q) {
        assert null != q;
        return 0 == q.size;
    }

    // Pre:  q != null
    // Post: N == 0
    public static void clear(final ArrayQueueADT q) {
        assert null != q;

        if (0 == q.size) {
            return;
        }

        if (q.head < tail(q)) {
            Arrays.fill(q.array, q.head, tail(q), null);
        } else {
            Arrays.fill(q.array, q.head, q.array.length, null);
            Arrays.fill(q.array, 0, tail(q), null);
        }

        q.head = q.size = 0;
    }

    private static void growIfNeeded(final ArrayQueueADT q) {
        if (q.size == q.array.length) {
            final var newArray = Arrays.copyOfRange(q.array, q.head, 2 * q.array.length);
            System.arraycopy(q.array, 0, newArray, q.array.length - q.head, tail(q));

            q.head = 0;
            q.array = newArray;
        }
    }

    private static int increment(final ArrayQueueADT q, final int index) {
        return q.array.length == 1 + index ? 0 : 1 + index;
    }

    private static int decrement(final ArrayQueueADT q, final int index) {
        return (0 == index ? q.array.length : index) - 1;
    }

    private static int tail(final ArrayQueueADT q) {
        final var t = q.head + q.size;
        return t < q.array.length ? t : t - q.array.length;
    }
}
