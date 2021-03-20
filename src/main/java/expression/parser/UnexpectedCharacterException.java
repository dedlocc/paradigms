package expression.parser;

public class UnexpectedCharacterException extends ParseException {
    public UnexpectedCharacterException(final String message, final int pos) {
        super(message, pos);
    }

    public static UnexpectedCharacterException fromCharSource(final CharSource chars) {
        final var ch = chars.peek();

        return new UnexpectedCharacterException(CharSource.END == ch
            ? "Unexpected end of string" : String.format("Unexpected '%c'", ch), chars.position());
    }
}
