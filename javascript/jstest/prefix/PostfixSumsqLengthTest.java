package jstest.prefix;

import jstest.ops.SumsqLength;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PostfixSumsqLengthTest {
    public static void main(final String... args) {
        Postfix.test(args, PostfixSumsqLengthTest.class, SumsqLength::add);
    }
}
