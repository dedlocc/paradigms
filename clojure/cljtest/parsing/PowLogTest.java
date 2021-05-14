package cljtest.parsing;

import jstest.BaseJavascriptTest;

import static jstest.AbstractTests.c;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PowLogTest {
    public static void main(final String... args) {
        Tester.test(args, PowLogTest.class, VariablesTest::add, PowLogTest::add);
    }

    public static void add(final BaseJavascriptTest.Dialect parsed, final Tester.Tests tests) {
        parsed.rename("**", "Pow", "//", "Log");
        tests.binary("**", -300, Math::pow);
        tests.binary("//", -300, (a, b) -> Math.log(Math.abs(b)) / Math.log(Math.abs(a)));
        @SuppressWarnings("UnnecessaryLocalVariable") final Tester.Tests t = tests;
        t.tests(
                t.f("**", t.vx, t.f("-", t.vy, t.vz)),
                t.f("**",
                        c(2),
                        t.f("+", c(1), t.f("*", c(2), t.f("-", t.vy, t.vz)))
                ),
                t.f("//",
                        t.f("+", c(2), t.f("*", c(4), t.f("-", t.vx, t.vz))),
                        t.f("+", c(1), t.f("*", c(2), t.f("-", t.vy, t.vz)))
                )
        );
    }
}
