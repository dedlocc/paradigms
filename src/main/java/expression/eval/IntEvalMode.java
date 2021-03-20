package expression.eval;

public class IntEvalMode implements EvalMode<Integer> {
    @Override
    public Integer fromInt(final int value) {
        return value;
    }

    @Override
    public Integer add(final Integer a, final Integer b) {
        return a + b;
    }

    @Override
    public Integer subtract(final Integer a, final Integer b) {
        return a - b;
    }

    @Override
    public Integer multiply(final Integer a, final Integer b) {
        return a * b;
    }

    @Override
    public Integer divide(final Integer a, final Integer b) {
        return a / b;
    }

    @Override
    public Integer mod(final Integer a, final Integer b) {
        return a % b;
    }

    @Override
    public Integer negate(final Integer a) {
        return -a;
    }

    @Override
    public Integer abs(final Integer a) {
        return a < 0 ? -a : a;
    }

    @Override
    public Integer square(final Integer a) {
        return a * a;
    }
}
