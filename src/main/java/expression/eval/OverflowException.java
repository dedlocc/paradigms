package expression.eval;

public class OverflowException extends EvaluationException {
    public OverflowException(final String message) {
        super(message);
    }
}
