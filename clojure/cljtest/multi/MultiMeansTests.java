package cljtest.multi;

import jstest.BaseJavascriptTest;

import java.util.Arrays;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.DoubleStream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class MultiMeansTests extends MultiTests {
    public MultiMeansTests(final boolean testMulti) {
        super(testMulti);

        any("arith-mean", 1, mean((args, n) -> args.sum() / n));
        any("geom-mean", 1, mean((args, n) -> Math.pow(Math.abs(product(args)), 1 / n)));
        any("harm-mean", 1, mean((args, n) -> {
            final double sum = args.map(a -> 1 / a).sum();
            return Double.isFinite(sum) ? n / sum : Double.NaN;
        }));
    }

    private static double product(final DoubleStream args) {
        return args.reduce(1, (a, b) -> a * b);
    }

    private static BaseJavascriptTest.Func mean(final ToDoubleBiFunction<DoubleStream, Double> f) {
        return args -> f.applyAsDouble(Arrays.stream(args), (double) args.length);
    }
}
