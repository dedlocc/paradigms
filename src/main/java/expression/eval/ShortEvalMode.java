package expression.eval;

public class ShortEvalMode implements EvalMode<Short> {
    @Override
    public Short fromInt(final int value) {
        return (short) value;
    }

    @Override
    public Short add(final Short a, final Short b) {
        return (short) (a + b);
    }

    @Override
    public Short subtract(final Short a, final Short b) {
        return (short) (a - b);
    }

    @Override
    public Short multiply(final Short a, final Short b) {
        return (short) (a * b);
    }

    @Override
    public Short divide(final Short a, final Short b) {
        return (short) (a / b);
    }

    @Override
    public Short mod(final Short a, final Short b) {
        return (short) (a % b);
    }

    @Override
    public Short negate(final Short a) {
        return (short) -a;
    }

    @Override
    public Short abs(final Short a) {
        return a < 0 ? (short) -a : a;
    }

    @Override
    public Short square(final Short a) {
        return (short) (a * a);
    }
}
