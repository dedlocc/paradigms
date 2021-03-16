package queue;

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
public interface Queue {
    // Pre:  e != null
    // Post: a[N] == e && N == N' + 1 && M(1, N')
    void enqueue(final Object e);

    // Pre:  N > 0
    // Post: R == a[1] && Immutable
    Object element();

    // Pre:  N > 0
    // Post: R == a'[1] && N == N' - 1 && M(2, N', -1)
    Object dequeue();

    // Pre:  true
    // Post: R == N && Immutable
    int size();

    // Pre:  true
    // Post: R == (N == 0) && Immutable
    boolean isEmpty();

    // Pre:  true
    // Post: N == 0
    void clear();

    // Pre:  true
    // Post: R == (exists i in [1, N] : a[i].equals(e)) && Immutable
    boolean contains(final Object e);

    // Pre:  true
    // Post: R == (i <= N') where i = min({j : a[j].equals(e)} U {N' + 1})
    //       && M(1, i - 1) && M(i + 1, N', -1)) && N == max(N', i) - 1
    boolean removeFirstOccurrence(final Object e);
}
