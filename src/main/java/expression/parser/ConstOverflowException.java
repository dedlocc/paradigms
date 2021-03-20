package expression.parser;

import expression.parser.ParseException;

public class ConstOverflowException extends ParseException {
    public ConstOverflowException(final boolean negative, final int pos, final Throwable cause) {
        super("Too " + (negative ? "small" : "big") + " integer", pos, cause);
    }
}
