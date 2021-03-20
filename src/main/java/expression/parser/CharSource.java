package expression.parser;

public interface CharSource {
    char END = '\uffff';

    char next();

    char current();

    char peek();

    int position();

    void reset(final int position);

    default boolean isEnd() {
        return END == peek();
    }

    default boolean test(final CharMatcher matcher, final boolean next) {
        if (matcher.match(peek())) {
            if (next) {
                next();
            }
            return true;
        }

        return false;
    }

    default boolean test(final CharMatcher matcher) {
        return test(matcher, true);
    }

    default boolean test(final CharMatcher... matchers) {
        final var pos = position();
        for (final var m : matchers) {
            if (!test(m)) {
                reset(pos);
                return false;
            }
        }
        return true;
    }

    default boolean test(final char c) {
        return test(CharMatcher.equals(c));
    }

    default boolean test(final char... c) {
        final var matchers = new CharMatcher[c.length];
        for (int i = 0; i < c.length; ++i) {
            matchers[i] = CharMatcher.equals(c[i]);
        }
        return test(matchers);
    }

    default boolean test(final String s) {
        return test(s.toCharArray());
    }
}
