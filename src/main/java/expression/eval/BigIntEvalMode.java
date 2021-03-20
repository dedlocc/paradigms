package expression.eval;

import java.math.BigInteger;

public class BigIntEvalMode implements EvalMode<BigInteger> {
    @Override
    public BigInteger fromInt(final int value) {
        return BigInteger.valueOf(value);
    }

    @Override
    public BigInteger add(final BigInteger a, final BigInteger b) {
        return a.add(b);
    }

    @Override
    public BigInteger subtract(final BigInteger a, final BigInteger b) {
        return a.subtract(b);
    }

    @Override
    public BigInteger multiply(final BigInteger a, final BigInteger b) {
        return a.multiply(b);
    }

    @Override
    public BigInteger divide(final BigInteger a, final BigInteger b) {
        return a.divide(b);
    }

    @Override
    public BigInteger mod(final BigInteger a, final BigInteger b) {
        return a.mod(b);
    }

    @Override
    public BigInteger negate(final BigInteger arg) {
        return arg.negate();
    }

    @Override
    public BigInteger abs(final BigInteger a) {
        return a.abs();
    }

    @Override
    public BigInteger square(final BigInteger a) {
        return a.multiply(a);
    }
}
