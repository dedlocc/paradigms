package expression.parser;

@FunctionalInterface
public interface CharMatcher {
    boolean match(final char c);

    static CharMatcher equals(final char ch) {
        return c -> c == ch;
    }
}
