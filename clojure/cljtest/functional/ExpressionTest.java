package cljtest.functional;

import base.Randomized;
import cljtest.ClojureEngine;
import cljtest.multi.Checker;
import jstest.BaseJavascriptTest;
import jstest.Language;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ExpressionTest extends BaseJavascriptTest<ClojureEngine> {
    public static final BiFunction<String, List<String>, String> CALL = (op, args) -> String.format("(%s %s)", op, String.join(" ", args));
    public static final Dialect PARSED = dialect("(variable \"%s\")", "(constant %s.0)", CALL)
            .renamed("+", "add", "-", "subtract", "*", "multiply", "/", "divide");
    public static final Dialect UNPARSED = dialect("%s", "%s.0", CALL);

    private final String parse;
    private final BiFunction<Randomized, String, String> spoiler;

    protected ExpressionTest(
            final Language language,
            final Optional<String> evaluate,
            final String parse,
            final BiFunction<Randomized, String, String> spoiler
    ) {
        super(new ClojureEngine("expression.clj", evaluate), language, true);
        this.parse = parse;
        this.spoiler = spoiler;
    }

    @Override
    public String parse(final String expression) {
        return "(" + parse + " \"" + spoiler.apply(this, expression) + "\")";
    }

    @SafeVarargs
    static void test(final String[] args, final Class<?> test, final Consumer<Checker>... testAdders) {
        final Language language1 = new Language(PARSED, UNPARSED, Checker.tests(mode(args, test), testAdders));
        new ExpressionTest(language1, Optional.empty(), "parseFunction", (a, b) -> b).run(test);
    }

    public static void main(final String... args) {
        test(args, ExpressionTest.class);
    }

    public static boolean mode(final String[] args, final Class<?> type) {
        return BaseJavascriptTest.mode(args, type, "easy", "hard") == 1;
    }
}
