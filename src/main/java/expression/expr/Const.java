package expression.expr;

import expression.eval.EvalMode;

public final class Const<T extends Number> implements Expression<T> {
    private final int value;

    public Const(final int value) {
        this.value = value;
    }

    @Override
    public T evaluate(final EvalMode<T> mode, final T x, final T y, final T z) {
        return mode.fromInt(value);
    }
}
