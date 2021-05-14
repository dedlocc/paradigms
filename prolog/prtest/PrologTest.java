package prtest;

import expression.BaseTest;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public abstract class PrologTest extends BaseTest {
    private final PrologScript prolog;

    public PrologTest(final String file) {
        prolog = new PrologScript(file);
    }

    public void assertSuccess(final boolean expected, final Rule rule, final Object... args) {
        prolog.assertSuccess(expected, rule, args);
    }

    public void assertResult(final Object expected, final Rule f, final Object... args) {
        prolog.assertResult(expected, f, args);
    }

    public boolean test(final Rule rule, final Object... args) {
        return prolog.test(rule, args);
    }

    public Value solveOne(final Rule rule, final Object... args) {
        return prolog.solveOne(rule, args);
    }
}
