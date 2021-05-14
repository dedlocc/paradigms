package jstest.prefix;

import jstest.BaseJavascriptTest;
import jstest.Language;
import jstest.object.ObjectExpressionTest;
import jstest.ops.Checker;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Kind {
    final String toString;
    final String parse;
    final BaseJavascriptTest.Dialect unparsed;
    final Consumer<KindTest> parsingTest;
    final boolean testMulti;

    public Kind(final String toString, final String parse, final BaseJavascriptTest.Dialect unparsed, final boolean testMulti, final String... parsingTests) {
        this.testMulti = testMulti;
        assert parsingTests.length % 2 == 0;
        this.toString = toString;
        this.parse = parse;
        this.unparsed = unparsed;
        parsingTest = test -> {
            for (int i = 0; i < parsingTests.length; i += 2) {
                printParsingError(test, parsingTests[i], parsingTests[i + 1]);
            }
        };
    }

    private static void printParsingError(final KindTest test, final String description, final String input) {
        final String message = test.assertParsingError(input, "", "");
        final int index = message.lastIndexOf("in <eval>");

        System.err.format("%-25s: %s%n", description, message.substring(0, index > 0 ? index : message.length()));
    }


    public void test(final String[] args, final Class<?> type, final BiConsumer<BaseJavascriptTest.Dialect, Checker>[] testAdders) {
        final BaseJavascriptTest.Dialect parsed = ObjectExpressionTest.ARITHMETIC_DIALECT.renamed();
        final int mode = PrefixParserTest.prefixMode(args, type);
        final boolean testMulti = mode == 2;
        final Checker checker = new Checker(testMulti);
        System.err.println("testMulti = " + testMulti);
        Arrays.stream(testAdders).forEach(adder -> adder.accept(parsed, checker));
        new KindTest(mode, new Language(parsed, unparsed, checker.getTests()), this, testMulti).run(type);
    }

    private static class KindTest extends PrefixParserTest {
        public KindTest(final int mode, final Language language, final Kind kind, final boolean testMulti) {
            super(mode, language, kind.toString, kind.parse);
            addStage(() -> kind.parsingTest.accept(this));
            insertions = testMulti ? "()+*/@ABC" : "xyz()+*/@ABC";
        }
    }
}
