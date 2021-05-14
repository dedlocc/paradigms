package cljtest.multi;

import jstest.AbstractTests;
import jstest.ArithmeticTests;
import jstest.BaseJavascriptTest;
import jstest.VariablesTests;

import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Checker {
    private final VariablesTests tests = new VariablesTests();
    private final boolean testMulti;

    public Checker(final boolean testMulti) {
        this.testMulti = testMulti;
    }

    void unary(final String name, final DoubleUnaryOperator answer) {
        tests.unary(name, answer);
        tests.tests(
                tests.f(name, tests.f("-", tests.vx, tests.vy)),
                tests.f(name, tests.f("+", tests.vx, tests.vy)),
                tests.f(name, tests.f("/", tests.f(name, tests.vz), tests.f("+", tests.vx, tests.vy))),
                tests.f("+", tests.f(name, tests.f(name, tests.f("+", tests.vx, AbstractTests.c(10)))), tests.f("*", tests.vz, tests.f("*", tests.vy, tests.f(name, AbstractTests.c(4)))))
        );
    }

    void binary(final String name, final DoubleBinaryOperator answer) {
        tests.binary(name, answer);
        binaryTests(name);
    }

    void any(final String name, final int minArity, final BaseJavascriptTest.Func f) {
        if (testMulti) {
            tests.any(name, minArity, 5, f);
        } else {
            tests.fixed(name, 2, f);
        }

        binaryTests(name);

        if (testMulti) {
            tests.tests(
                    tests.f(name, tests.vx),
                    tests.f(name, tests.vx, tests.vy, tests.vz),
                    tests.f(name, tests.vx, tests.vy, tests.vz, VariablesTests.c(3), VariablesTests.c(5)),
                    tests.f(name, tests.f("+", tests.vx, VariablesTests.c(2))),
                    tests.f(name, tests.f("+", tests.vx, tests.vy))
            );
        }

        final Supplier<AbstractTests.TestExpression> generator = () -> tests.randomItem(tests.vx, tests.vy, tests.vz, VariablesTests.c(tests.randomInt(10)), VariablesTests.c(tests.randomInt(10)));
        for (int i = 1; i < 10; i++) {
            tests.tests(tests.f(name, Stream.generate(generator).limit(testMulti ? i : 2).toArray(AbstractTests.TestExpression[]::new)));
        }
    }

    private void binaryTests(final String name) {
        tests.tests(
                tests.f(name, tests.vx, tests.vy),
                tests.f(name, tests.f("negate", tests.vz), tests.f("+", tests.vx, tests.vy)),
                tests.f(name, tests.f("-", tests.vz, tests.vy), tests.f("negate", tests.vx))
        );

        tests.tests(
                tests.f(name, tests.f("negate", tests.vz), tests.f(name, tests.vx, tests.vy)),
                tests.f(name, tests.f(name, tests.vx, tests.vy), tests.f("negate", tests.vz))
        );
    }

    public void addAll(final ArithmeticTests tests) {
        this.tests.tests.addAll(tests.tests);
    }

    @SafeVarargs
    public static VariablesTests tests(final boolean multi, final Consumer<Checker>... testAdder) {
        final Checker tests = new Checker(multi);
        Arith.add(tests);
        Stream.of(testAdder).forEach(adder -> adder.accept(tests));
        return tests.tests;
    }
}
