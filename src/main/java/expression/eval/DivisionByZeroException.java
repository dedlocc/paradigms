package expression.eval;

public class DivisionByZeroException extends EvaluationException {
    public DivisionByZeroException() {
        super("Division by zero is not allowed");
    }
}
