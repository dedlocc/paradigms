package jstest.ops;

import jstest.AbstractTests;
import jstest.ArithmeticTests;
import jstest.BaseJavascriptTest;
import jstest.VariablesTests;

import java.util.function.DoubleUnaryOperator;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Checker {
    private final ArithmeticTests tests = new ArithmeticTests();
    private final boolean testMulti;

    public Checker(final boolean testMulti) {
        this.testMulti = testMulti;
    }

    public void unary(final String name, final DoubleUnaryOperator op) {
        tests.unary(name, op);
        tests.tests(
                tests.f(name, tests.f("-", tests.vx, tests.vy)),
                tests.f(name, tests.f("+", tests.vx, tests.vy)),
                tests.f(name, tests.f("/", tests.f(name, tests.vz), tests.f("+", tests.vx, tests.vy))),
                tests.f("+", tests.f(name, tests.f(name, tests.f("+", tests.vx, AbstractTests.c(10)))), tests.f("*", tests.vz, tests.f("*", tests.vy, tests.f(name, AbstractTests.c(4)))))
        );
    }

    public void any(final String name, final int minArity, final int fixedArity, final BaseJavascriptTest.Func f) {
        tests.any(name, minArity, 5, f);
        if (testMulti) {
            tests.any(name, minArity, 5, f);
        } else {
            tests.fixed(name, fixedArity, f);
        }

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
            tests.tests(tests.f(name, Stream.generate(generator).limit(testMulti ? i : fixedArity).toArray(AbstractTests.TestExpression[]::new)));
        }
    }

    public AbstractTests getTests() {
        return tests;
    }
}
