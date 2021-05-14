package cljtest.parsing;

import base.Randomized;
import cljtest.ClojureScript;
import jstest.ArithmeticTests;
import jstest.BaseJavascriptTest;
import jstest.Language;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntPredicate;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Tester {
    @SafeVarargs
    static void test(final String[] args, final Class<?> test, final BiConsumer<BaseJavascriptTest.Dialect, Tests>... testAdders) {
        final Tests tests = new Tests();
        final cljtest.object.Tester.Dialect parsed = cljtest.object.Tester.PARSED.renamed();
        Arrays.stream(testAdders).forEach(adder -> adder.accept(parsed, tests));

        final Mode mode = cljtest.object.Tester.mode(args, test) ? Mode.INFIX : Mode.SUFFIX;
        final Language language = new Language(parsed, mode.unparsed, tests);
        cljtest.object.Tester.test(language, false, mode.toString, mode.parse, spoiler(mode, tests), test);
    }

    private static BiFunction<Randomized, String, String> spoiler(final Mode mode, final Tests tests) {
        return (random, expression) -> {
            if (random.randomBoolean()) {
                return expression;
            }
            if (mode == Mode.INFIX) {
                final Parsed parsed = new Parser(expression, tests.getVariableNames().keySet(), tests.priorities).parse();
                expression = parsed.convert(new StringBuilder(), 0).toString();
            }
            return cljtest.object.Tester.addSpaces(expression, random.getRandom());
        };
    }

    private enum Mode {
        SUFFIX("toStringSuffix", "parseObjectSuffix", cljtest.object.Tester.dialect(
                "%s",
                "%s.0",
                (op, args) -> "("+ String.join(" ", args) + " " + op + ")"
        )),
        INFIX("toStringInfix", "parseObjectInfix", cljtest.object.Tester.dialect(
                "%s",
                "%s.0",
                (op, args) -> {
                    switch (args.size()) {
                        case 1: return op + "(" + args.get(0) + ")";
                        case 2: return "(" + args.get(0) + " " + op + " " + args.get(1) + ")";
                        default: throw new AssertionError("Unsupported op " + op + "/" + args.size());
                    }
                }
        ));

        private final cljtest.object.Tester.Dialect unparsed;
        private final ClojureScript.F<String> toString;
        private final String parse;

        Mode(final String toString, final String parse, final cljtest.object.Tester.Dialect unparsed) {
            this.unparsed = unparsed;
            this.toString = ClojureScript.function(toString, String.class);
            this.parse = parse;
        }
    }

    private interface Parsed {
        StringBuilder convert(StringBuilder sb, int priority);
    }

    private static class Parser {
        private final String expression;
        private final Set<String> vars;
        private final Map<String, Integer> priorities;
        int pos = 0;

        public Parser(final String expression, final Set<String> vars, final Map<String, Integer> priorities) {
            this.expression = expression + "$";
            this.vars = vars;
            this.priorities = priorities;
        }

        public Parsed parse() {
            skipSpaces();
            if (test('(')) {
                final Parsed left = parse();
                skipSpaces();
                final String op = parseIdentifier();
                final Parsed right = parse();
                skipSpaces();
                expect(')');
                return (sb, priority) -> {
                    final int p = priorities.get(op);
                    final int local = Math.abs(p);
                    final int l = local + (p > 0 ? 0 : 1);
                    final int r = local + (p > 0 ? 1 : 0);
                    if (local < priority) {
                        return right.convert(left.convert(sb.append("("), l).append(op), r).append(")");
                    } else {
                        return right.convert(left.convert(sb, l).append(op), r);
                    }
                };
            } else if (Character.isDigit(getChar()) || getChar() == '-') {
                final char first = getChar();
                pos++;
                final String value = first + get(ch -> Character.isDigit(ch) || ch == '.');
                return (sb, priority) -> sb.append(value);
            } else {
                final String identifier = parseIdentifier();
                if (vars.contains(identifier)) {
                    return (sb, priority) -> sb.append(identifier);
                } else {
                    skipSpaces();
                    expect('(');
                    final Parsed arg = parse();
                    skipSpaces();
                    expect(')');
                    return (sb, priority) -> arg.convert(sb.append(identifier).append(" "), Integer.MAX_VALUE);
                }
            }
        }

        private static final String SYMBOLS = "*/-+&|^<>=";

        private String parseIdentifier() {
            final char first = getChar();
            if (Character.isLetter(first)) {
                return get(Character::isLetterOrDigit);
            } else {
                return get(ch -> SYMBOLS.indexOf(ch) >= 0);
            }
        }

        private void expect(final char ch) {
            if (!test(ch)) {
                throw new AssertionError(String.format("%d: expected '%c', found '%c'", pos + 1, ch, getChar()));
            }
        }

        private char getChar() {
            return expression.charAt(pos);
        }

        private boolean test(final char ch) {
            if (getChar() == ch) {
                pos++;
                return true;
            }
            return false;
        }

        private void skipSpaces() {
            get(Character::isWhitespace);
        }

        private String get(final IntPredicate p) {
            final int start = pos;
            while (p.test(getChar())) {
                pos++;
            }
            return expression.substring(start, pos);
        }
    }

    static class Tests extends ArithmeticTests {
        private final Map<String, Integer> priorities = new HashMap<>(Map.of(
                "+", 100, "-", 100,
                "*", 200, "/", 200
        ));

        protected void binary(final String name, final int priority, final DoubleBinaryOperator answer) {
            binary(name, answer);
            priorities.put(name, priority);

            tests(
                    f(name, vx, vy),
                    f(name, vx, f("negate", vy)),
                    f(name, f("negate", vx), vy),
                    f(name, f("negate", vx), f("negate", vy)),
                    f(name, vx, f("-", c(1), vy)),
                    f(name, f("-", vx, c(2)), vy)
            );
        }
    }
}
