package jstest.ops;

import jstest.BaseJavascriptTest;

import java.util.Arrays;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.DoubleStream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Means {
    public static void add(final BaseJavascriptTest.Dialect dialect, final Checker checker) {
        dialect.rename("arith-mean", "ArithMean", "geom-mean", "GeomMean", "harm-mean", "HarmMean");
        checker.any("arith-mean", 1, 5, mean((args, n) -> args.sum() / n));
        checker.any("geom-mean", 1, 5, mean((args, n) -> Math.pow(Math.abs(product(args)), 1 / n)));
        checker.any("harm-mean", 1, 5, mean((args, n) -> n / args.map(a -> 1 / a).sum()));
    }

    private static double product(final DoubleStream args) {
        return args.reduce(1, (a, b) -> a * b);
    }

    private static BaseJavascriptTest.Func mean(final ToDoubleBiFunction<DoubleStream, Double> f) {
        return args -> f.applyAsDouble(Arrays.stream(args), (double) args.length);
    }
}
