package expression.eval;

public class CheckedIntEvalMode extends IntEvalMode {
    @Override
    public Integer fromInt(final int value) {
        return value;
    }

    @Override
    public Integer add(final Integer a, final Integer b) {
        if (a > 0 && b > Integer.MAX_VALUE - a || a < 0 && b < Integer.MIN_VALUE - a) {
            throw new OverflowException(String.format("Overflow to represent the sum of %d and %d", a, b));
        }

        return super.add(a, b);
    }

    @Override
    public Integer subtract(final Integer a, final Integer b) {
        if (b > 0 && a < Integer.MIN_VALUE + b || b < 0 && a > Integer.MAX_VALUE + b) {
            throw new OverflowException(String.format("Overflow to represent the difference between %d and %d", a, b));
        }

        return super.subtract(a, b);
    }

    @Override
    public Integer multiply(final Integer a, final Integer b) {
        final var max = Integer.signum(a) == Integer.signum(b) ? Integer.MAX_VALUE : Integer.MIN_VALUE;

        if (Integer.MIN_VALUE == a && -1 == b || Integer.MIN_VALUE == b && -1 == a || 0 != a &&
            (-1 != a && b > 0 && b > max / a || b < 0 && b < max / a)
        ) {
            throw new OverflowException(String.format("Overflow to represent the product of %d and %d", a, b));
        }

        return super.multiply(a, b);
    }

    @Override
    public Integer divide(final Integer a, final Integer b) {
        if (0 == b) {
            throw new DivisionByZeroException();
        }

        if (Integer.MIN_VALUE == a && -1 == b) {
            throw new OverflowException(String.format("Overflow to represent the quotient of %d and %d", a, b));
        }

        return super.divide(a, b);
    }

    @Override
    public Integer mod(final Integer a, final Integer b) {
        if (0 == b) {
            throw new DivisionByZeroException();
        }

        return super.mod(a, b);
    }

    @Override
    public Integer negate(final Integer a) {
        if (Integer.MIN_VALUE == a) {
            throw new OverflowException(String.format("Overflow to represent negated value of %d", a));
        }

        return super.negate(a);
    }

    @Override
    public Integer abs(final Integer a) {
        if (Integer.MIN_VALUE == a) {
            throw new OverflowException(String.format("Overflow to represent an absolute value of %d", a));
        }

        return super.abs(a);
    }

    @Override
    public Integer square(final Integer a) {
        final var max = Integer.MAX_VALUE;

        if (a > 0 && a > max / a || a < 0 && a < max / a) {
            throw new OverflowException(String.format("Overflow to represent square of %d", a));
        }

        return super.square(a);
    }
}
