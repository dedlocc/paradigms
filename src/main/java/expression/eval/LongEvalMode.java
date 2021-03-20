package expression.eval;

public class LongEvalMode implements EvalMode<Long> {
    @Override
    public Long fromInt(final int value) {
        return (long) value;
    }

    @Override
    public Long add(final Long a, final Long b) {
        return a + b;
    }

    @Override
    public Long subtract(final Long a, final Long b) {
        return a - b;
    }

    @Override
    public Long multiply(final Long a, final Long b) {
        return a * b;
    }

    @Override
    public Long divide(final Long a, final Long b) {
        return a / b;
    }

    @Override
    public Long mod(final Long a, final Long b) {
        return a % b;
    }

    @Override
    public Long negate(final Long a) {
        return -a;
    }

    @Override
    public Long abs(final Long a) {
        return a < 0 ? -a : a;
    }

    @Override
    public Long square(final Long a) {
        return a * a;
    }
}
