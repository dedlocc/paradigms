package cljtest.functional;

import cljtest.multi.MeanVarn;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class MeanVarnTest {
    public static void main(final String... args) {
        ExpressionTest.test(args, MeanVarnTest.class, MeanVarn::add);
    }
}
