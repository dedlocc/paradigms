package cljtest.object;

import cljtest.multi.MultiMeansTests;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ClojureObjectMeansTest {
    public static void main(final String... args) {
        ClojureObjectExpressionTest.test(
                args, MultiMeansTests::new, ClojureObjectMeansTest.class,
                ClojureObjectExpressionTest.PARSED.copy()
                        .rename("arith-mean", "ArithMean")
                        .rename("geom-mean", "GeomMean")
                        .rename("harm-mean", "HarmMean")
        );
    }
}
