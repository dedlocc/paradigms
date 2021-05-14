package jstest.prefix;

import jstest.ops.SinhCosh;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PrefixSinhCoshTest {
    public static void main(final String... args) {
        Prefix.test(args, PrefixSinhCoshTest.class, SinhCosh::add);
    }
}
