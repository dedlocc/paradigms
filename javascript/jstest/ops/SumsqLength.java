package jstest.ops;

import jstest.BaseJavascriptTest;

import java.util.Arrays;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class SumsqLength {
    public static void add(final BaseJavascriptTest.Dialect dialect, final Checker checker) {
        dialect.rename("sumsq", "Sumsq", "length", "Length");
        checker.any("sumsq", 0, 3, SumsqLength::sumsq);
        checker.any("length", 0, 5, args -> Math.sqrt(sumsq(args)));

    }

    private static double sumsq(final double[] args) {
        return Arrays.stream(args).map(a -> a * a).sum();
    }
}
