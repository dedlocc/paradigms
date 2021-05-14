package jstest.ops;

import jstest.BaseJavascriptTest;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class SinhCosh {
    public static void add(final BaseJavascriptTest.Dialect dialect, final Checker checker) {
        dialect.rename("sinh", "Sinh", "cosh", "Cosh");
        checker.unary("sinh", Math::sinh);
        checker.unary("cosh", Math::cosh);
    }
}
