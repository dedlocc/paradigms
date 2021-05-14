package jstest.prefix;

import jstest.BaseJavascriptTest;
import jstest.ops.Checker;

import java.util.function.BiConsumer;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Prefix {
    private static final Kind KIND = new Kind("prefix", "parsePrefix", PrefixParserTest.PREFIX, false,
            "Empty input", "",
        "Unknown variable", "a",
        "Invalid number", "-a",
        "Missing )", "(* z (+ x y)",
        "Unknown operation", "(@@  x y)",
        "Excessive info", "(+ x y) x",
        "Empty op", "()",
        "Invalid unary (0 args)", "(negate)",
        "Invalid unary (2 args)", "(negate x y)",
        "Invalid binary (0 args)", "(+)",
        "Invalid binary (1 args)", "(+ x)",
        "Invalid binary (3 args)", "(+ x y z)",
        "Variable op (0 args)", "(x)",
        "Variable op (1 args)", "(x 1)",
        "Variable op (2 args)", "(x 1 2)",
        "Const op (0 args)", "(0)",
        "Const op (1 args)", "(0 1)",
        "Const op (2 args)", "(0 1 2)"
    );

    @SafeVarargs
    public static void test(final String[] args, final Class<?> type, final BiConsumer<BaseJavascriptTest.Dialect, Checker>... testAdders) {
        KIND.test(args, type, testAdders);
    }
}
