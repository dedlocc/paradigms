package expression.parser;

public class ParseException extends RuntimeException {
    private final int pos;

    public ParseException(final String message, final int pos) {
        super(formatMessage(message, pos));
        this.pos = pos;
    }

    public ParseException(final String message, final int pos, final Throwable cause) {
        super(formatMessage(message, pos), cause);
        this.pos = pos;
    }

    private static String formatMessage(final String message, final int pos) {
        return message + " at position " + (1 + pos);
    }

    public int getPos() {
        return pos;
    }
}
