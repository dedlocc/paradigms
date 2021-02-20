package search;

public final class BinarySearchSpan {
    // Pre (P): null != args && 0 < args.length &&
    //          for each i : 0 <= i < args.length, args[i] is an integer represented as a string &&
    //          for each i : 2 <= i < args.length, args[i] <= args[i - 1] (as integers)
    // Post: printed (to stdout) an index of first element of args[1:args.length-1] that is less or equal to args[0],
    //       or args.length if such an element does not exist
    //       followed by number of elements equal to x
    public static void main(final String[] args) {
        // P
        final var x = Integer.parseInt(args[0]);
        // P && x is an integer parsed from args[0]
        final var arr = new int[args.length - 1];
        // P && x is an integer parsed from args[0] && null != arr && arr.length == args.length - 1

        // Inv: P && x is an integer parsed from args[0] && null != arr && arr.length == args.length - 1
        //      for each j : 0 <= j < i, arr[j] is an integer parsed from args[1 + j]
        for (var i = 0; i < arr.length; ) {
            arr[i] = Integer.parseInt(args[++i]);
        }

        // P && x is an integer parsed from args[0] && null != arr && arr.length == args.length - 1
        // for each i : 0 <= i < arr.length, arr[i] is an integer parsed from args[1 + i]

        // F: null != arr && 0 < arr.length && for each i : 1 <= i < arr.length, arr[i] <= arr[i - 1]

        final var l = iterative(arr, x);
        // F && (l == arr.length || 0 <= l < arr.length && x >= arr[l]) && (x < arr[i] for each i: 0 <= i < l)
        final var r = recursive(arr, x);
        // F && (l == arr.length || 0 <= l < arr.length && x >= arr[l]) && (x < arr[i] for each i: 0 <= i < l) &&
        // (-1 == arr.length || 0 <= r < arr.length && x <= arr[r]) && (x > arr[i] for each i: r < i < arr.length)

        // F && (l is the index of the first occurrence of <= x in arr) &&
        // (r is the index of the last occurrence of >= x in arr)
        // => F && (l is the index of the first occurrence of <= x in arr) && (for each i: l <= i < r, arr[i] = x)
        final var n = r - l + 1;
        // F && (l is the index of the first occurrence of <= x in arr) && n is the number of i: arr[i] = x
        System.out.printf("%d %d", l, n);
    }

    // Pre:  F
    // Post: (R == arr.length || 0 <= R < arr.length && x >= arr[R]) && (x < arr[i] for each i: 0 <= i < R)
    private static int iterative(final int[] arr, final int x) {
        // F
        var l = -1;
        // F && l == -1
        var r = arr.length;
        // F && l == -1 && r == arr.length

        // Inv (I): F && (r == arr.length || x >= arr[r]) &&
        //          (x < arr[i] for each i: 0 <= i < l) &&
        //          -1 <= l < r <= arr.length
        while (l + 1 < r) {
            // I && 1 < r - l
            var m = l + (r - l) / 2;
            // I && l < m < r
            if (x < arr[m]) {
                // I && l < m < r && x < arr[m]
                l = m;
                // F && (r == arr.length || x >= arr[r]) && (x < arr[i] for each i: 0 <= i < l') &&
                // -1 <= l < r <= arr.length && l' < l < r && x < arr[l]

                // (x < arr[i] for each i: 0 <= i < l') && l' < l && x < arr[l] &&
                // (for each i : 1 <= i < arr.length, arr[i] <= arr[i - 1])) => (x < arr[i] for each i: 0 <= i < l)

                // I && l' < l
            } else {
                // I && l < m < r && x >= arr[m]
                r = m;
                // F && (r' == arr.length || x >= arr[r']) && (x < arr[i] for each i: 0 <= i < l) &&
                // -1 <= l < r' <= arr.length && l < r < r' && x >= arr[r]

                // (-1 <= l < r' <= arr.length) && (l < r < r') => -1 <= l < r < arr.length

                // I && r < r'
            }

            // I && (l' < l || r < r')
            // I && (r - l < r' - l')
        }

        // I && !(1 < r - l)

        // F && (r == arr.length || x >= arr[r]) && (x < arr[i] for each i: 0 <= i < l) &&
        // -1 <= l < r <= arr.length && !(1 < r - l)

        // (x < arr[i] for each i: 0 <= i < l) && 1 >= r - l
        // => (x < arr[i] for each i: 1 <= i + 1 < l + 1) && r <= l + 1
        // => x < arr[i] for each i: 1 <= i + 1 <= r
        // => x < arr[i] for each i: 0 <= i < r

        // -1 <= l < r <= arr.length => 0 <= r <= arr.length

        // (r == arr.length || 0 <= r < arr.length && x >= arr[r]) && (x < arr[i] for each i: 0 <= i < r)
        return r;
    }

    // Pre:  F
    // Post: (-1 == arr.length || 0 <= R < arr.length && x <= arr[R]) && (x > arr[i] for each i: R < i < arr.length)
    private static int recursive(final int[] arr, final int x) {
        return recursive(arr, x, -1, arr.length);
    }

    // Inv (I): F && (l == -1 || x <= arr[l]) &&
    //          (x > arr[i] for each i: r < i < arr.length) &&
    //          -1 <= l < r <= arr.length
    // Post:    (R == -1 || 0 <= R < arr.length && x <= arr[R]) && (x > arr[i] for each i: R < i < arr.length)
    private static int recursive(final int[] arr, final int x, int l, int r) {
        if (l + 1 >= r) {
            // I && 1 >= r - l

            // F && (l == -1 || x <= arr[l]) && (x > arr[i] for each i: r < i < arr.length) &&
            // -1 <= l < r <= arr.length && !(1 < r - l)

            // (x > arr[i] for each i: r < i < arr.length) && 1 >= r - l
            // => (x > arr[i] for each i: r - 1 < i - 1 < arr.length - 1) && l >= r - 1
            // => x > arr[i] for each i: l <= i - 1 < arr.length - 1
            // => x > arr[i] for each i: l < i < arr.length

            // -1 <= l < r <= arr.length => 0 <= r <= arr.length

            // (l == -1 || 0 <= l < arr.length && x <= arr[l]) && (x > arr[i] for each i: l < i < arr.length)
            return l;
        }

        // I && 1 < r - l
        var m = l + (r - l) / 2;
        // I && l < m < r

        if (x <= arr[m]) {
            // I && l < m < r && x <= arr[m]
            l = m;
            // F && (l' == -1 || x <= arr[l']) && (x > arr[i] for each i: r < i < arr.length) &&
            // -1 <= l' < r <= arr.length && l' < l < r && x <= arr[l]

            // -1 <= l' < r <= arr.length && l' < l < r => -1 <= l < r <= arr.length

            // I && l' < l
        } else {
            // I && l < m < r && x > arr[m]
            r = m;

            // F && (l == -1 || x <= arr[l]) && (x > arr[i] for each i: r' < i < arr.length) &&
            // -1 <= l < r' <= arr.length && l < r < r' && x > arr[r]

            // (x > arr[i] for each i: r' < i < arr.length) && r < r' && x > arr[r]
            // (for each i : 1 <= i < arr.length, arr[i] <= arr[i - 1]) => (x > arr[i] for each i: r < i < arr.length)

            // I && r < r'
        }

        // I && (l' < l || r < r')
        // I && (r - l < r' - l')

        // (R == -1 || 0 <= R < arr.length && x <= arr[R]) && (x > arr[i] for each i: R < i < arr.length)
        return recursive(arr, x, l, r);
    }
}
