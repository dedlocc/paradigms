package expression.expr;

import expression.eval.EvalMode;
import expression.eval.EvaluationException;

public final class Variable<T extends Number> implements Expression<T> {
    private final String name;

    public Variable(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public T evaluate(final EvalMode<T> mode, final T x, final T y, final T z) {
       switch (name) {
           case "x": return x;
           case "y": return y;
           case "z": return z;
           default: throw new NoValueException(name);
        }
    }

    public static class NoValueException extends EvaluationException {
        private NoValueException(final String name) {
            super(String.format("No value provided for variable \"%s\"", name));
        }
    }
}
