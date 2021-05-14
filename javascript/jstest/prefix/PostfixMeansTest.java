package jstest.prefix;

import jstest.ops.Means;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PostfixMeansTest {
    public static void main(final String... args) {
        Postfix.test(args, PostfixMeansTest.class, Means::add);
    }
}
