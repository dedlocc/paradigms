package cljtest.object;

import base.Randomized;
import cljtest.ClojureEngine;
import cljtest.ClojureScript;
import cljtest.multi.Checker;
import jstest.Engine;
import jstest.Language;
import jstest.object.Diff;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Tester extends cljtest.functional.ExpressionTest {
    public static final Dialect PARSED = dialect("(Variable \"%s\")", "(Constant %s.0)", CALL)
            .renamed("+", "Add", "-", "Subtract", "*", "Multiply", "/", "Divide", "negate", "Negate");

    private static final ClojureScript.F<String> TO_STRING = ClojureScript.function("toString", String.class);
    private static final Diff DIFF = new Diff(1, N, dialect("\"%s\"", "%s", CALL));

    private final ClojureScript.F<String> toString;

    private Tester(
            final Language language,
            final boolean testDiff,
            final ClojureScript.F<String> toString,
            final String parse,
            final BiFunction<Randomized, String, String> spoiler
    ) {
        super(language, Optional.of("evaluate"), parse, spoiler);
        if (testDiff) {
            DIFF.add(this);
        }
        this.toString = toString;
    }

    public static void test(final Language language, final boolean testDiff, final ClojureScript.F<String> toString, final String parse, final BiFunction<Randomized, String, String> spoiler, final Class<?> test) {
        new Tester(language, testDiff, toString, parse, spoiler).run(test);
    }

    @Override
    protected void test(final String parsed, final String unparsed) {
        testToString(engine, parsed, unparsed);

        testToString(engine, addSpaces(parsed, random), unparsed);
    }

    private void testToString(final ClojureEngine engine, final String expression, final String expected) {
        engine.parse(expression);
        final Engine.Result<String> result = engine.toString(toString);
        assertEquals(result.context, expected, result.value);
    }

    static void test(final String[] args, final Class<?> test, final Consumer<Checker> testAdder, final String... renames) {
        final boolean hard = mode(args, test);
        final Language language = new Language(PARSED.renamed(renames), UNPARSED, Checker.tests(hard, testAdder));
        test(language, true, TO_STRING, "parseObject", (a, b) -> b, test);
    }
}
