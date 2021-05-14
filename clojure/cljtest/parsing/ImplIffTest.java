package cljtest.parsing;

import jstest.BaseJavascriptTest;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ImplIffTest {
    public static void main(final String... args) {
        Tester.test(args, ImplIffTest.class, VariablesTest::add, ImplIffTest::add);
    }

    public static void add(final BaseJavascriptTest.Dialect parsed, final Tester.Tests tests) {
        parsed.rename("->", "Impl", "<->", "Iff");
        tests.binary("->", -9, BooleanTest.bool((a, b) -> !a | b));
        tests.binary("<->", 5, BooleanTest.bool((a, b) -> a == b));
    }
}
