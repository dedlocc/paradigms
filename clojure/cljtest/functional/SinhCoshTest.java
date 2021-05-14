package cljtest.functional;

import cljtest.multi.SinhCosh;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class SinhCoshTest {
    public static void main(final String... args) {
        ExpressionTest.test(args, SinhCoshTest.class, SinhCosh::add);
    }
}
