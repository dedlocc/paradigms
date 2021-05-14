package jstest.functional;

import expression.BaseTest;
import jstest.*;

import java.util.function.BiFunction;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class FunctionalExpressionTest extends BaseJavascriptTest<Engine> {
    public static final Dialect ARITHMETIC_FUNCTIONS = dialect(
            "variable('%s')",
            "cnst(%s)",
            (op, args) -> op + "(" + String.join(", ", args) + ")"
    ).renamed("+", "add", "-", "subtract", "/", "divide", "*", "multiply");

    public static final Dialect POLISH = dialect(
            "%s",
            "%s",
            (op, args) -> String.join(" ", args) + " " + op
    );

    protected FunctionalExpressionTest(final Language language, final boolean testParsing) {
        this(new JSEngine("functionalExpression.js", ""), language, testParsing);
    }

    protected FunctionalExpressionTest(final Engine engine, final Language language, final boolean testParsing) {
        super(engine, language, testParsing);
    }

    @Override
    public String parse(final String expression) {
        return "parse('" + expression + "')";
    }

    protected static <T extends BaseTest> void test(final String[] args, final Class<T> type, final BiFunction<Language, Boolean, T> cons, final AbstractTests tests) {
        test(args, type, cons, tests, ARITHMETIC_FUNCTIONS);
    }

    protected static <T extends BaseTest> void test(final String[] args, final Class<T> type, final BiFunction<Language, Boolean, T> cons, final AbstractTests tests, final Dialect parsed) {
        cons.apply(
                new Language(parsed, POLISH, tests),
                mode(args, type, "easy", "hard") == 1
        ).run();
        System.err.println("Mode: " + args[0]);
    }

    public static void main(final String... args) {
        test(args, FunctionalExpressionTest.class, FunctionalExpressionTest::new, new AbstractTests() {
            protected final TestExpression vx = variable("x", 0);

            {
                //noinspection Convert2MethodRef
                binary("+", (a, b) -> a + b);
                binary("-", (a, b) -> a - b);
                binary("*", (a, b) -> a * b);
                binary("/", (a, b) -> a / b);

                tests(
                        c(10),
                        vx,
                        f("+", vx, c(2)),
                        f("-", c(3), vx),
                        f("*", c(4), vx),
                        f("/", c(5), vx),
                        f("/", vx, f("*", f("+", vx, c(1)), vx)),
                        f("+", f("+", f("*", vx, vx), f("*", vx, vx)), f("*", vx, vx)),
                        f("-", f("+", f("*", vx, vx), f("*", c(5), f("*", vx, f("*", vx, vx)))), f("*", vx, c(8)))
                );
            }
        });
    }
}
