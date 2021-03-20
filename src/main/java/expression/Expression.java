package expression;

import expression.eval.EvalMode;

@FunctionalInterface
public interface Expression<T extends Number> {
    T evaluate(final EvalMode<T> mode, final T x, final T y, final T z);
}
