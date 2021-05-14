package jstest.object;

import expression.BaseTest;
import jstest.*;

import java.util.List;
import java.util.function.BiFunction;

import static jstest.functional.FunctionalExpressionTest.POLISH;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ObjectExpressionTest extends BaseJavascriptTest<JSEngine> {

    public static final Dialect ARITHMETIC_DIALECT = dialect("new Variable('%s')", "new Const(%s)",
            (op, args) -> String.format("new %s(%s)", op, String.join(", ", args)))
            .renamed("+", "Add", "-", "Subtract", "*", "Multiply", "/", "Divide", "negate", "Negate");


    final List<int[]> simplifications = list(
            new int[]{1, 1, 1},
            new int[]{1, 1, 1},
            new int[]{1, 1, 1},
            new int[]{1, 1, 1},
            new int[]{1, 1, 1},
            new int[]{1, 2, 1},
            new int[]{1, 1, 1},
            new int[]{1, 1, 10},
            new int[]{4, 1, 1},
            new int[]{9, 28, 28},
            new int[]{5, 5, 5},
            new int[]{5, 2, 21}
    );

    private static final Diff DIFF = new Diff(2, N, dialect(
            "'%s'", "%s",
            (name, args) -> String.format("%s.%s(%s)", args.get(0), name, String.join(", ", args.subList(1, args.size())))
    ));

    protected ObjectExpressionTest(final int mode, final Language language) {
        super(new JSEngine("objectExpression.js", ".evaluate"), language, mode >= 1);

        if (mode >= 2) {
            DIFF.add(this);
        }
        if (mode >= 3) {
            DIFF.addSimplify(this);
        }
    }

    @Override
    protected void test() {
        super.test();
    }

    @Override
    public String parse(final String expression) {
        return "parse('" + expression + "')";
    }

    @Override
    protected void test(final String parsed, final String unparsed) {
        counter.nextTest();
        testToString(parsed, unparsed);

        testToString(BaseJavascriptTest.addSpaces(parsed, random), unparsed);
        counter.passed();
    }

    private void testToString(final String expression, final String expected) {
        engine.parse(expression);
        final Engine.Result<String> result = engine.parsedToString();
        assertEquals(result.context, expected, result.value);
    }

    public static void main(final String... args) {
        test(ObjectExpressionTest.class, ObjectExpressionTest::new, new ArithmeticTests(), args, ARITHMETIC_DIALECT);
    }

    public static <T extends BaseTest> void test(final Class<T> type, final BiFunction<Integer, Language, T> cons, final AbstractTests tests, final String[] args, final Dialect parsed) {
        final int mode = mode(args, type, "easy", "", "hard", "bonus");
        cons.apply(mode, new Language(parsed, POLISH, tests)).run();
    }
}
