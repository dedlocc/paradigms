package jstest.prefix;

import jstest.ops.Means;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PrefixMeansTest {
    public static void main(final String... args) {
        Prefix.test(args, PrefixMeansTest.class, Means::add);
    }
}
