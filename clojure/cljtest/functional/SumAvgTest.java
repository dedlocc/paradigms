package cljtest.functional;

import cljtest.multi.SumAvg;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class SumAvgTest {
    public static void main(final String... args) {
        ExpressionTest.test(args, SumAvgTest.class, SumAvg::add);
    }
}
