package cljtest.multi;

import jstest.ArithmeticTests;
import jstest.BaseJavascriptTest;

import java.util.Arrays;
import java.util.function.DoubleBinaryOperator;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Arith {
    public static void add(final Checker t) {
        t.unary("negate", a -> -a);
        t.addAll(new ArithmeticTests());

        t.any("+", 0, arith(0, Double::sum));
        t.any("-", 1, arith(0, (a, b) -> a - b));
        t.any("*", 0, arith(1, (a, b) -> a * b));
        t.any("/", 1, arith(1, (a, b) -> a / b));
    }

    private static BaseJavascriptTest.Func arith(final double zero, final DoubleBinaryOperator f) {
        return args -> args.length == 0 ? zero
                : args.length == 1 ? f.applyAsDouble(zero, args[0])
                : Arrays.stream(args).reduce(f).orElseThrow();
    }
}
