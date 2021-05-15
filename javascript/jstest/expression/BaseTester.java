package jstest.expression;

import expression.BaseTest;
import jstest.Engine;
import jstest.JSEngine;

import java.util.*;
import java.util.function.ToDoubleFunction;

/**
 * @author Niyaz Nigmatullin
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public abstract class BaseTester<E extends Engine> extends BaseTest {
    public static final int N = 5;
    public static final double EPS = 1e-3;

    protected final E engine;
    /*package*/ final Language language;
    private final List<Runnable> stages = new ArrayList<>();

    final boolean testParsing;

    protected BaseTester(final E engine, final Language language, final boolean testParsing) {
        this.engine = engine;
        this.language = language;
        this.testParsing = testParsing;
    }

    private static boolean safe(final char ch) {
        return !Character.isLetterOrDigit(ch) && "+-*/.<>=&|^".indexOf(ch) == -1;
    }

    public static String addSpaces(final String expression, final Random random) {
        String spaced = expression;
        for (int n = StrictMath.min(10, 200 / expression.length()); n > 0;) {
            final int index = random.nextInt(spaced.length() + 1);
            final char c = index == 0 ? 0 : spaced.charAt(index - 1);
            final char nc = index == spaced.length() ? 0 : spaced.charAt(index);
            if ((safe(c) || safe(nc)) && c != '\'' && nc != '\'' && c != '"' && nc != '"') {
                spaced = spaced.substring(0, index) + " " + spaced.substring(index);
                n--;
            }
        }
        return spaced;
    }

    @Override
    protected void test() {
        for (final Expr test : language.getTests()) {
            test(test.parsed, test.answer, test.unparsed);
            if (testParsing) {
                test(parse(test.unparsed), test.answer, test.unparsed);
                test(parse(addSpaces(test.unparsed, random)), test.answer, test.unparsed);
            }
        }

        testRandom(444);
        stages.forEach(Runnable::run);
    }

    public abstract String parse(final String expression);

    protected void test(final String expression, final Func f, final String unparsed) {
        System.out.println("Testing: " + expression);

        engine.parse(expression);
        for (double i = 0; i <= N; i++) {
            for (double j = 0; j <= N; j++) {
                for (double k = 0; k <= N; k++) {
                    final double[] vars = new double[]{i, j, k};
                    evaluate(vars, f.applyAsDouble(vars));
                }
            }
        }

        test(expression, unparsed);
    }

    protected void test(final String parsed, final String unparsed) {
    }

    public void testRandom(final int n) {
        System.out.println("Testing random tests");
        for (int i = 0; i < n; i++) {
            if (i % 100 == 0) {
                System.out.printf("    Completed %3d out of %d%n", i, n);
            }
            final double[] vars = random.doubles().limit(language.getVariables().size()).toArray();

            final Expr test = this.language.randomTest(i);
            final double answer = test.answer.applyAsDouble(vars);

            engine.parse(test.parsed);
            evaluate(vars, answer);
            test(test.parsed, test.unparsed);
            test(addSpaces(test.parsed, random), test.unparsed);
            if (testParsing) {
                counter.nextTest();
                final String expr = parse(test.unparsed);
                test(expr, test.unparsed);

                engine.parse(expr);
                evaluate(vars, answer);
                counter.passed();
            }
        }
    }

    public void evaluate(final double[] vars, final double answer) {
        counter.nextTest();
        final Engine.Result<Number> result = engine.evaluate(vars);
        assertEquals(result.context, EPS, answer, result.value.doubleValue());
        counter.passed();
    }

    public static int mode(final String[] args, final Class<?> type, final String... modes) {
        if (args.length == 0) {
            System.err.println("ERROR: No arguments found");
        } else if (args.length > 1) {
            System.err.println("ERROR: Only one argument expected, " + args.length + " found");
        } else if (!Arrays.asList(modes).contains(args[0])) {
            System.err.println("ERROR: First argument should be one of: \"" + String.join("\", \"", modes) + "\", found: \"" + args[0] + "\"");
        } else {
            return Arrays.asList(modes).indexOf(args[0]);
        }
        System.err.println("Usage: java -ea " + JSEngine.OPTIONS + " " + type.getName() + " {" + String.join("|", modes) + "}");
        System.exit(0);
        return -1;
    }

    public void addStage(final Runnable stage) {
        stages.add(stage);
    }

    public interface Func extends ToDoubleFunction<double[]> {
        @Override
        double applyAsDouble(double... args);
    }

    public static class Expr {
        public final String parsed;
        public final String unparsed;
        public final Func answer;

        public Expr(final String parsed, final String unparsed, final Func answer) {
            this.parsed = Objects.requireNonNull(parsed);
            this.unparsed = Objects.requireNonNull(unparsed);
            this.answer = answer;
        }
    }
}
