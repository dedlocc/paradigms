package search;

public final class BinarySearch {
    // Pre (P): null != args && 0 < args.length &&
    //          for each i : 0 <= i < args.length, args[i] is an integer represented as a string &&
    //          for each i : 2 <= i < args.length, args[i] <= args[i - 1] (as integers)
    // Post: printed (to stdout) an index of first element of args[1:args.length-1] that is less or equal to args[0],
    //       or args.length if such an element does not exist
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

        // null != arr && 0 < arr.length && for each i : 1 <= i < arr.length, arr[i] <= arr[i - 1]

        System.out.println(iterative(arr, x));
        //System.out.println(recursive(arr, x));
    }

    // Pre (F): null != arr && 0 < arr.length && for each i : 1 <= i < arr.length, arr[i] <= arr[i - 1]
    // Post:    (R == arr.length || 0 <= R < arr.length && x >= arr[R]) && (x < arr[i] for each i: 0 <= i < R)
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

    // Pre (F): null != arr && 0 < arr.length && for each i : 1 <= i < arr.length, arr[i] <= arr[i - 1]
    // Post:    (R == arr.length || 0 <= R < arr.length && x >= arr[R]) && (x < arr[i] for each i: 0 <= i < R)
    private static int recursive(final int[] arr, final int x) {
        return recursive(arr, x, -1, arr.length);
    }

    // Inv (I): F && (r == arr.length || x >= arr[r]) &&
    //          (x < arr[i] for each i: 0 <= i < l) &&
    //          -1 <= l < r <= arr.length
    // Post:    (R == arr.length || 0 <= R < arr.length && x >= arr[R]) && (x < arr[i] for each i: 0 <= i < R)
    private static int recursive(final int[] arr, final int x, int l, int r) {
        if (l + 1 >= r) {
            // I && 1 >= r - l

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

        // (R == arr.length || 0 <= R < arr.length && x >= arr[R]) && (x < arr[i] for each i: 0 <= i < R)
        return recursive(arr, x, l, r);
    }
}
