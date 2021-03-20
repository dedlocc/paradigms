package expression.generic;

import expression.eval.*;
import expression.operation.BinaryOperation;
import expression.parser.ExpressionParser;

import java.util.Map;

public class GenericTabulator implements Tabulator {
    final static Map<String, EvalMode<? extends Number>> modes = Map.of(
        "i", new CheckedIntEvalMode(),
        "d", new DoubleEvalMode(),
        "bi", new BigIntEvalMode(),
        "u", new IntEvalMode(),
        "l", new LongEvalMode(),
        "s", new ShortEvalMode()
    );

    public static void main(final String[] args) {
        if (null == args || null == args[0] || null == args[1] || 2 != args.length || 2 > args[0].length() || '-' != args[0].charAt(0)) {
            System.out.println("Bad arguments");
            return;
        }

        final var arr = new GenericTabulator().tabulate(args[0].substring(1), args[1], -2, 2, -2, 2, -2, 2);

        for (var x = 0; x < 5; ++x) {
            for (var y = 0; y < 5; ++y) {
                for (var z = 0; z < 5; ++z) {
                    System.out.printf("[%d, %d, %d]: %s%n", x - 2, y - 2, z - 2, arr[x][y][z]);
                }
            }
        }
    }

    @Override
    public Number[][][] tabulate(
        final String mode,
        final String expression,
        final int x1, final int x2,
        final int y1, final int y2,
        final int z1, final int z2
    ) {
        final var m = modes.get(mode);

        if (null == m) {
            throw new IllegalArgumentException("Unknown mode: " + mode);
        }

        return tabulate(m, expression, x1, x2, y1, y2, z1, z2);
    }

    private <T extends Number> Number[][][] tabulate(
        final EvalMode<T> mode,
        final String expression,
        final int x1, final int x2,
        final int y1, final int y2,
        final int z1, final int z2
    ) {
        final var parser = new ExpressionParser<T>(
            Map.of(
                "+", new BinaryOperation<>(EvalMode::add, 10),
                "-", new BinaryOperation<>(EvalMode::subtract, 10),
                "*", new BinaryOperation<>(EvalMode::multiply, 20),
                "/", new BinaryOperation<>(EvalMode::divide, 20),
                "mod", new BinaryOperation<>(EvalMode::mod, 20)
            ),
            Map.of(
                "-", EvalMode::negate,
                "abs", EvalMode::abs,
                "square", EvalMode::square
            )
        );

        final var expr = parser.parse(expression);

        Number[][][] result = new Number[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];

        for (var i = x1; i <= x2; ++i) {
            final var x = mode.fromInt(i);
            for (var j = y1; j <= y2; ++j) {
                final var y = mode.fromInt(j);
                for (var k = z1; k <= z2; ++k) {
                    final var z = mode.fromInt(k);
                    try {
                        result[i - x1][j - y1][k - z1] = expr.evaluate(mode, x, y, z);
                    } catch (final ArithmeticException ignored) {}
                }
            }
        }

        return result;
    }
}

