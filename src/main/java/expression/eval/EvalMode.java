package expression.eval;

public interface EvalMode<T extends Number> {
    T fromInt(final int value);

    T add(final T a, final T b);

    T subtract(final T a, final T b);

    T multiply(final T a, final T b);

    T divide(final T a, final T b);

    T mod(final T a, final T b);

    T negate(final T arg);

    T abs(final T a);

    T square(final T a);
}
