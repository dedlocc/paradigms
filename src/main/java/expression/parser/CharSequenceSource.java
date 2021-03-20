package expression.parser;

public class CharSequenceSource implements CharSource {
    private final CharSequence data;
    private int pos;
    private char current;

    public CharSequenceSource(final CharSequence data) {
        this.data = data;
    }

    @Override
    public char current() {
        return current;
    }

    @Override
    public char peek() {
        return pos < data.length() ? data.charAt(pos) : END;
    }

    @Override
    public char next() {
        return current = pos < data.length() ? data.charAt(pos++) : END;
    }

    @Override
    public int position() {
        return pos;
    }

    @Override
    public void reset(final int position) {
        pos = position;
    }
}
