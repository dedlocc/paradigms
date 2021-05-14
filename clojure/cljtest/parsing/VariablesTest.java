package cljtest.parsing;

import jstest.AbstractTests;
import jstest.ArithmeticTests;
import jstest.BaseJavascriptTest;

import java.util.function.BiFunction;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class VariablesTest {
    public static void main(final String... args) {
        Tester.test(args, VariablesTest.class, VariablesTest::add);
    }

    public static void add(final BaseJavascriptTest.Dialect parsed, final Tester.Tests tests) {
        final BiFunction<Character, Integer, AbstractTests.TestExpression> var =
            (first, i) -> tests.variable((tests.randomBoolean() ? first : Character.toUpperCase(first)) + tests.randomString("xyzXYZ"), i);
        for (int i = 0; i < 10; i++) {
            tests.tests.addAll(new ArithmeticTests(var.apply('x', 0), var.apply('y', 1), var.apply('z', 2)).tests);
        }
    }
}
