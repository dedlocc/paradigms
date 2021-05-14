package cljtest.multi;

import java.util.Arrays;
import java.util.stream.DoubleStream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class MeanVarn {
    public static void add(final Checker t) {
        t.any("mean", 1, args -> mean(Arrays.stream(args)));
        t.any("varn", 1, MeanVarn::varn);
    }

    private static double varn(final double[] args) {
        final double mean = mean(Arrays.stream(args));
        return mean(Arrays.stream(args).map(a -> a - mean).map(a -> a * a));
    }

    private static double mean(final DoubleStream args) {
        return args.average().orElseThrow();
    }
}
