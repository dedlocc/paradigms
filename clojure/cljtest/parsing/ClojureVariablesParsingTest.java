package cljtest.parsing;

import jstest.ArithmeticTests;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ClojureVariablesParsingTest {
    public static void main(final String... args) {
        ClojureObjectParsingTest.test(
                args,
                new Tests(),
                ClojureVariablesParsingTest.class,
                ClojureObjectParsingTest.PARSED
        );
    }

    static class Tests extends ClojureObjectParsingTest.Tests {
        public Tests() {
            for (int i = 0; i < 10; i++) {
                tests.addAll(new ArithmeticTests(var('x', 0), var('y', 1), var('z', 2)).tests);
            }
        }

        private TestExpression var(final char first, final int i) {
            return variable((randomBoolean() ? first : Character.toUpperCase(first)) + randomString("xyzXYZ"), i);
        }
    }
}
