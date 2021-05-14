package cljtest.object;

import cljtest.multi.SumAvg;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class SumAvgTest {
    public static void main(final String... args) {
        Tester.test(args, SumAvgTest.class, SumAvg::add, "sum", "Sum", "avg", "Avg");
    }
}
