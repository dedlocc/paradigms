package expression.parser;

import expression.parser.ParseException;

public class UnknownVariableException extends ParseException {
    public UnknownVariableException(final String name, final int pos) {
        super(String.format("Unknown variable name '%s'", name), pos);
    }
}
