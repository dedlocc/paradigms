package expression.operation;

import expression.expr.Expression;
import expression.eval.EvalMode;

import java.util.function.BinaryOperator;

public class BinaryOperation<T extends Number> implements BinaryOperator<Expression<T>> {
    private final Func<T> func;
    private final int precedence;

    public BinaryOperation(final Func<T> func, final int precedence) {

        this.func = func;
        this.precedence = precedence;
    }

    @Override
    public Expression<T> apply(final Expression<T> a, final Expression<T> b) {
        return (mode, x, y, z) -> func.apply(mode, a.evaluate(mode, x, y, z), b.evaluate(mode, x, y, z));
    }

    public int getPrecedence() {
        return precedence;
    }

    @FunctionalInterface
    public interface Func<T extends Number> {
        T apply(final EvalMode<T> mode, final T a, final T b);
    }
}
