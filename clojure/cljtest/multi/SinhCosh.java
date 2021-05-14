package cljtest.multi;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class SinhCosh {
    public static void add(final Checker t) {
        t.unary("sinh", Math::sinh);
        t.unary("cosh", Math::cosh);
    }
}
