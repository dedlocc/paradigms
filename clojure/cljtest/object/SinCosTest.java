package cljtest.object;

import cljtest.multi.SinCos;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class SinCosTest {
    public static void main(final String... args) {
        Tester.test(args, SinCosTest.class, SinCos::add, "sin", "Sin", "cos", "Cos");
    }
}
