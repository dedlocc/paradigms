package expression.operation;

import expression.Expression;
import expression.eval.EvalMode;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface UnaryOperation<T extends Number> extends UnaryOperator<Expression<T>> {
    @Override
    default Expression<T> apply(final Expression<T> a) {
        return (mode, x, y, z) -> apply(mode, a.evaluate(mode, x, y, z));
    }

    T apply(final EvalMode<T> mode, final T a);
}
