package jstest.object;

import jstest.BaseJavascriptTest;
import jstest.Engine;

import java.util.ArrayList;
import java.util.List;

import static jstest.BaseJavascriptTest.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Diff {
    public static final double D = 1e-6;

    private final Dialect dialect;
    private final int min;
    private final int max;

    public Diff(final int min, final int max, final Dialect dialect) {
        this.dialect = dialect;
        this.min = min;
        this.max = max;
    }

    private List<Engine.Result<String>> diff(final BaseJavascriptTest<?> test, final Expr expr, final String parsed, final boolean simplify) {
        final List<Engine.Result<String>> results = new ArrayList<>(3);
        final List<String> variables = List.of("x", "y", "z");
        for (int variable = 0; variable < 3; variable++) {
            final String diff1 = dialect.operation("diff", List.of(parsed, dialect.variable(variables.get(variable))));
            final String value = simplify ? dialect.operation("simplify", List.of(diff1)) : diff1;
            System.out.println("Testing: " + value);
            test.engine.parse(value);

            results.add(test.engine.parsedToString());

            final double di = variable == 0 ? D : 0;
            final double dj = variable == 1 ? D : 0;
            final double dk = variable == 2 ? D : 0;
            for (int i = min; i <= max; i++) {
                for (int j = min; j <= max; j++) {
                    for (int k = min; k <= max; k++) {
                        final double d = Math.abs(expr.answer.applyAsDouble(i, j, k));
                        if (EPS < d && d < 1 / EPS) {
                            final double expected = (
                                    expr.answer.applyAsDouble(i + di, j + dj, k + dk) -
                                            expr.answer.applyAsDouble(i - di, j - dj, k - dk)) / D / 2;
                            test.evaluate(new double[]{i, j, k}, expected);
                        }
                    }
                }
            }
        }
        return results;
    }

    public void add(final BaseJavascriptTest<?> test) {
        test.addStage(() -> {
            for (final Expr expr : test.language.tests) {
                diff(test, expr, expr.parsed, false);
                diff(test, expr, test.parse(expr.unparsed), false);
            }
        });
    }

    public void addSimplify(final ObjectExpressionTest test) {
        test.addStage(() -> {
            for (int i = 0; i < test.simplifications.size(); i++) {
                final Expr expr = test.language.tests.get(i);
                final int[] expected = test.simplifications.get(i);
                final List<Engine.Result<String>> actual = diff(test, expr, test.parse(expr.unparsed), true);
//                    System.out.format("{%s},%n", actual.stream()
//                            .mapToInt(result -> result.value.length())
//                            .mapToObj(Integer::toString)
//                            .collect(Collectors.joining(", ")));
                for (int j = 0; j < expected.length; j++) {
                    final Engine.Result<String> result = actual.get(j);
                    final int length = result.value.length();
                    assertTrue(
                            String.format("Simplified length too long: %d instead of %d%s", length, expected[j], result.context),
                            length <= expected[j]
                    );
                }
            }
        });
    }
}
