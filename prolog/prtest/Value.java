package prtest;

import alice.tuprolog.Int;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import base.Asserts;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class Value {
    private static final Struct EMPTY_LIST = PrologUtil.pure("[]");

    private final Term term;

    private Value(final Term term) {
        this.term = term;
    }

    public Term toTerm() {
        return term;
    }

    public List<Value> toList() {
        Asserts.assertTrue("Type", toTerm().isList());

        Struct list = (Struct) toTerm();
        final List<Value> result = new ArrayList<>();
        while (!list.isEmptyList()) {
            result.add(term(list.getTerm(0)));
            list = (Struct) list.getTerm(1);
        }
        return result;
    }

    @Override
    public String toString() {
        return term.toString();
    }

    @Override
    public boolean equals(final Object that) {
        return this == that || that instanceof Value && term.equals(((Value) that).term);
    }

    @Override
    public int hashCode() {
        return Objects.hash(term);
    }

    public static Value term(final Term term) {
        return new Value(term);
    }

    public static Value convert(final Object term) {
        if (term instanceof Value) {
            return (Value) term;
        } else if (term instanceof Term) {
            return term((Term) term);
        } else if (term instanceof List) {
            return list((List<?>) term, Value::convert);
        } else if (term instanceof Map.Entry) {
            final Map.Entry<?, ?> entry = (Map.Entry<?, ?>) term;
            return Value.struct(",", convert(entry.getKey()), convert(entry.getValue()));
        } else if (term instanceof Integer || term instanceof Long) {
            return term(Int.of((Number) term));
        } else {
            throw new AssertionError("Cannot convert `" + term + "` of type " + term.getClass().getName() + " to Term");
        }
    }

    public static <T> Value list(final List<? extends T> items, final Function<? super T, Value> convert) {
        Struct current = EMPTY_LIST;
        for (final ListIterator<? extends T> i = items.listIterator(items.size()); i.hasPrevious(); ) {
            current = Struct.of(".", new Term[]{convert.apply(i.previous()).toTerm(), current});
        }
        return term(current);
    }

    public static Value string(final String string) {
        return term(PrologUtil.pure(string));
    }

    public static Value struct(final String rule, final Object... args) {
        return term(Struct.of(rule, Stream.of(args).map(Value::convert).map(Value::toTerm).toArray(Term[]::new)));
    }
}
