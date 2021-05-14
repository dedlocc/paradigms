package jstest.prefix;

import jstest.ArithmeticTests;
import jstest.EngineException;
import jstest.Language;
import jstest.object.ObjectExpressionTest;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PrefixParserTest extends ObjectExpressionTest {
    public String insertions = "xyz()+*/@ABC";
    public static final Dialect PREFIX = dialect(
            "%s",
            "%s",
            (op, args) -> "(" + op + " " + String.join(" ", args) + ")"
    );

    private final String parse;

    public PrefixParserTest(final int mode, final Language language, final String toString, final String parse) {
        super(mode, language);
        engine.toStringMethod = toString;
        this.parse = parse;
    }

    @Override
    public String parse(final String expression) {
        return parse + "('" + expression + "')";
    }

    @Override
    protected void test(final String parsed, final String unparsed) {
        super.test(parsed, unparsed);
        super.test(removeSpaces(parsed), unparsed);

        for (int i = 0; i < 1 + Math.min(10, 200 / unparsed.length()); i++) {
            final int index = randomInt(unparsed.length());
            final char c = unparsed.charAt(index);
            if (!Character.isDigit(c) && !Character.isWhitespace(c) && "-hxyz".indexOf(c) == -1){
                counter.nextTest();
                assertParsingError(unparsed.substring(0, index), "<SYMBOL REMOVED>", unparsed.substring(index + 1));
                counter.passed();
            }
            final char newC = insertions.charAt(randomInt(insertions.length()));
            if (!Character.isDigit(c) && c != '-') {
                counter.nextTest();
                assertParsingError(unparsed.substring(0, index), "<SYMBOL INSERTED -->", newC + unparsed.substring(index));
                counter.passed();
            }
        }
    }

    private static String removeSpaces(final String expression) {
        return expression.replace(" (", "(").replace(") ", ")");
    }

    protected String assertParsingError(final String prefix, final String comment, final String suffix) {
        try {
            engine.parse(parse(prefix + suffix));
            throw new AssertionError("Parsing error expected for " + prefix + comment + suffix);
        } catch (final EngineException e) {
            return e.getCause().getMessage();
        }
    }

    public static void main(final String... args) {
        final int mode = prefixMode(args, PrefixParserTest.class);
        final Language language = new Language(ARITHMETIC_DIALECT, PREFIX, new ArithmeticTests());
        new PrefixParserTest(mode, language, "prefix", "parsePrefix").run(PrefixParserTest.class);
    }

    protected static int prefixMode(final String[] args, final Class<?> type) {
        return mode(args, type, "easy", "hard") + 1;
    }

    interface Constructor<T> {
        T create(int mode, Language language, String toString, String parse);
    }
}
