package cljtest.parsing;

import jstest.BaseJavascriptTest;

import java.util.function.DoubleBinaryOperator;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ClojureBooleanParsingTest {
    public static final BaseJavascriptTest.Dialect PARSED = ClojureObjectParsingTest.PARSED.copy()
            .rename("&&", "And")
            .rename("||", "Or")
            .rename("^^", "Xor");

    public static void main(final String... args) {
        ClojureObjectParsingTest.test(args, new Tests(), ClojureBooleanParsingTest.class, PARSED);
    }

    interface BooleanBinaryOperator {
        boolean applyAsBoolean(boolean a, boolean b);
    }

    public static class Tests extends ClojureVariablesParsingTest.Tests {
        public Tests() {
            binary("&&", 50, bool((a, b) -> a & b));
            binary("||", 30, bool((a, b) -> a | b));
            binary("^^", 10, bool((a, b) -> a ^ b));
        }

        protected static DoubleBinaryOperator bool(final BooleanBinaryOperator op) {
            return (a, b) -> op.applyAsBoolean(a > 0, b > 0) ? 1 : 0;
        }
    }
}
