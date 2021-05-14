package cljtest.multi;

import java.util.Arrays;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class SumAvg {
    public static void add(final Checker t) {
        t.any("sum", 0, args -> Arrays.stream(args).sum());
        t.any("avg", 1, args -> Arrays.stream(args).average().orElseThrow());
    }
}
