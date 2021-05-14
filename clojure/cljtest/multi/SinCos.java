package cljtest.multi;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class SinCos {
    public static void add(final Checker t) {
        t.unary("sin", Math::sin);
        t.unary("cos", Math::cos);
    }
}
