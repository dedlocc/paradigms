package expression.eval;

public class DoubleEvalMode implements EvalMode<Double> {
    @Override
    public Double fromInt(final int value) {
        return (double) value;
    }

    @Override
    public Double add(final Double a, final Double b) {
        return a + b;
    }

    @Override
    public Double subtract(final Double a, final Double b) {
        return a - b;
    }

    @Override
    public Double multiply(final Double a, final Double b) {
        return a * b;
    }

    @Override
    public Double divide(final Double a, final Double b) {
        return a / b;
    }

    @Override
    public Double mod(final Double a, final Double b) {
        return a % b;
    }

    @Override
    public Double negate(final Double arg) {
        return -arg;
    }

    @Override
    public Double abs(final Double a) {
        return a < 0 ? -a : a;
    }

    @Override
    public Double square(final Double a) {
        return a * a;
    }
}
