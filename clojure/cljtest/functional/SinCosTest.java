package cljtest.functional;

import cljtest.multi.SinCos;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class SinCosTest {
    public static void main(final String... args) {
        ExpressionTest.test(args, SinCosTest.class, SinCos::add);
    }
}
