package prtest;

import alice.tuprolog.*;
import alice.tuprolog.exceptions.NoMoreSolutionException;
import alice.tuprolog.exceptions.NoSolutionException;
import base.Asserts;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PrologScript {
    public static final Var V = Var.of("V");
    public static Path PROLOG_ROOT = Path.of(".");
    private static final Rule CONSULT = new Rule("consult", 1);

    private final Prolog prolog = new Prolog();

    public PrologScript() {
        prolog.addExceptionListener(exceptionEvent -> {throw new PrologException(exceptionEvent.getMsg());});
        prolog.addOutputListener(event -> System.out.print(event.getMsg()));
    }

    public PrologScript(final String file) {
        this();
        consult(file);
    }

    private static PrologException error(final Throwable cause, final String format, final Object... arguments) {
        return new PrologException(String.format(format, arguments), cause);
    }

    public void consult(final String file) {
        final Path path = PROLOG_ROOT.resolve(file);
        System.out.println("Loading " + path);
        try {
            if (!test(CONSULT, Value.string(path.toString()))) {
                throw error(null, "Error opening '%s'", path);
            }
        } catch (final PrologException e) {
            throw error(e, "Error opening '%s': %s", path, e.getMessage());
        }
    }

    public void addTheory(final Theory theory) {
        prolog.addTheory(theory);
    }

    public boolean test(final Rule rule, final Object... args) {
        return prolog.solve(rule.apply(args)).isSuccess();
    }

    private List<Term> solve(final Term term) {
        SolveInfo info = prolog.solve(term);
        final List<Term> values = new ArrayList<>();
        try {
            while (info.isSuccess()) {
                values.add(info.getVarValue(V.getName()));
                if (!info.hasOpenAlternatives()) {
                    return values;
                }
                info = prolog.solveNext();
            }
            return values;
        } catch (final NoSolutionException | NoMoreSolutionException e) {
            throw new AssertionError(e);
        }
    }

    private Value solveOne(final Term term) {
        final List<Term> values = solve(term);
        if (values.size() != 1) {
            throw Asserts.error("Exactly one solution expected for %s in %s%n  found: %d %s", V, term, values.size(), values);
        }
        return Value.term(values.get(0));
    }

    public Value solveOne(final Rule rule, final Object... args) {
        return solveOne(rule.apply(args));
    }

    public void assertSuccess(final boolean expected, final Rule rule, final Object... args) {
        Asserts.assertEquals(rule.apply(args).toString(), expected, test(rule, args));
    }

    public void assertResult(final Object expected, final Rule f, final Object... args) {
        final Term term = f.apply(args);
        if (expected != null) {
            final Term converted = Value.convert(expected).toTerm();
            final Term actual = solveOne(term).toTerm();
            if (!converted.equals(actual)) {
                throw Asserts.error("%s:%n  expected `%s`,%n    actual `%s`", term, converted, actual);
            }
        } else {
            final List<Term> values = solve(term);
            if (!values.isEmpty()) {
                throw Asserts.error("No solutions expected for %s in %s%n  found: %d %s", V, term, values.size(), values);
            }
        }
    }
}
