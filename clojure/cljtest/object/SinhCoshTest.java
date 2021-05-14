package cljtest.object;

import cljtest.multi.SinhCosh;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class SinhCoshTest {
    public static void main(final String... args) {
        Tester.test(args, SinhCoshTest.class, SinhCosh::add, "sinh", "Sinh", "cosh", "Cosh");
    }
}
