package cljtest.object;

import cljtest.multi.Means;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class MeansTest {
    public static void main(final String... args) {
        Tester.test(args, MeansTest.class, Means::add,
                "arith-mean", "ArithMean", "geom-mean", "GeomMean", "harm-mean", "HarmMean");
    }
}
