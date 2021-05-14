package cljtest.parsing;

import jstest.BaseJavascriptTest;

import java.util.function.DoubleBinaryOperator;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class BooleanTest {
    public static void main(final String... args) {
        Tester.test(args, BooleanTest.class, VariablesTest::add, BooleanTest::add);
    }

    interface BooleanBinaryOperator {
        boolean applyAsBoolean(boolean a, boolean b);
    }

    public static DoubleBinaryOperator bool(final BooleanBinaryOperator op) {
        return (a, b) -> op.applyAsBoolean(a > 0, b > 0) ? 1 : 0;
    }

    public static void add(final BaseJavascriptTest.Dialect parsed, final Tester.Tests tests) {
        parsed.rename("&&", "And", "||", "Or", "^^", "Xor");
        tests.binary("&&", 50, bool((a, b) -> a & b));
        tests.binary("||", 30, bool((a, b) -> a | b));
        tests.binary("^^", 10, bool((a, b) -> a ^ b));
    }
}
