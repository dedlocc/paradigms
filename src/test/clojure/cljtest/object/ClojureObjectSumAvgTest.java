package cljtest.object;

import cljtest.multi.MultiSumAvgTests;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ClojureObjectSumAvgTest {
    public static void main(final String... args) {
        ClojureObjectExpressionTest.test(
                args, MultiSumAvgTests::new, ClojureObjectSumAvgTest.class,
                ClojureObjectExpressionTest.PARSED.copy()
                        .rename("sum", "Sum")
                        .rename("avg", "Avg")
        );
    }
}
