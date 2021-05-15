package cljtest.object;

import base.Randomized;
import cljtest.ClojureEngine;
import cljtest.ClojureScript;
import cljtest.functional.FunctionalTester;
import jstest.Engine;
import jstest.expression.*;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ObjectTester extends FunctionalTester {
    public static final Dialect PARSED = new Dialect("(Variable \"%s\")", "(Constant %s.0)", "(op args)", " ");
    private static final ClojureScript.F<String> TO_STRING = ClojureScript.function("toString", String.class);
    private static final Diff DIFF = new Diff(1, N, new Dialect("\"%s\"", "%s", "(op args)", " "));
    static final Selector SELECTOR = new Selector(List.of("easy", "hard"), (operations, mode) -> {
        final Builder builder = new Builder(mode == 1, operations);
        final Language language = builder.dialect(PARSED, UNPARSED);
        return new ObjectTester(language, true, TO_STRING, "parseObject", (a, b) -> b);
    });

    private final ClojureScript.F<String> toString;

    public ObjectTester(
            final Language language,
            final boolean testDiff,
            final ClojureScript.F<String> toString,
            final String parse,
            final BiFunction<Randomized, String, String> spoiler
    ) {
        super(language, Optional.of("evaluate"), parse, spoiler);
        if (testDiff) {
            DIFF.diff(this);
        }
        this.toString = toString;
    }

    public static void test(final Language language, final boolean testDiff, final ClojureScript.F<String> toString, final String parse, final BiFunction<Randomized, String, String> spoiler, final Class<?> test) {
        new ObjectTester(language, testDiff, toString, parse, spoiler).run(test);
    }

    @Override
    protected void test(final String parsed, final String unparsed) {
        testToString(engine, parsed, unparsed);
        testToString(engine, addSpaces(parsed, random), unparsed);
    }

    private void testToString(final ClojureEngine engine, final String expression, final String expected) {
        counter.nextTest();
        engine.parse(expression);
        final Engine.Result<String> result = engine.toString(toString);
        assertEquals(result.context, expected, result.value);
        counter.passed();
    }
}
