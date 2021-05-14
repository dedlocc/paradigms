package jstest.prefix;

import jstest.ops.SumsqLength;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PrefixSumsqLengthTest {
    public static void main(final String... args) {
        Prefix.test(args, PrefixSumsqLengthTest.class, SumsqLength::add);
    }
}
