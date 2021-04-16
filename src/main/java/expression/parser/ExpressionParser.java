package expression.parser;

import expression.expr.Const;
import expression.expr.Expression;
import expression.expr.Variable;
import expression.operation.BinaryOperation;
import expression.operation.UnaryOperation;

import java.util.*;

public class ExpressionParser<T extends Number> {
    private static final Set<String> ALLOWED_VARIABLE_NAMES = Set.of("x", "y", "z");

    protected final Map<String, BinaryOperation<T>> binaryOperations;
    protected final Map<String, UnaryOperation<T>> unaryOperations;

    protected CharSource chars;

    public ExpressionParser(
        final Map<String, BinaryOperation<T>> binaryOperations,
        final Map<String, UnaryOperation<T>> unaryOperations
    ) {
        this.binaryOperations = binaryOperations;
        this.unaryOperations = unaryOperations;
    }

    public Expression<T> parse(final String expression) throws ParseException {
        chars = new CharSequenceSource(expression);
        return parseExpression(CharSource.END);
    }

    protected Expression<T> parseExpression(final char until) {
        var res = parseExpression(until, null, null);
        while (res.op != null) {
            res = parseExpression(until, res.expr, res.op);
        }
        return res.expr;
    }

    protected ParseResult parseExpression(
        final char until,
        Expression<T> expr,
        BinaryOperation<T> op
    ) {
        while (true) {
            var newExpr = parseElement();
            skipWhitespace();

            if (chars.test(until)) {
                return new ParseResult(null == expr ? newExpr : op.apply(expr, newExpr), null);
            }

            var newOp = parseBinaryOperation();

            if (null == newOp) {
                throw UnexpectedCharacterException.fromCharSource(chars);
            }

            if (null == expr) {
                expr = newExpr;
            } else {
                while (op.getPrecedence() < newOp.getPrecedence()) {
                    skipWhitespace();
                    final var result = parseExpression(until, newExpr, newOp);
                    newExpr = result.expr;
                    newOp = result.op;
                    if (null == newOp) {
                        return new ParseResult(op.apply(expr, newExpr), null);
                    }

                }

                expr = op.apply(expr, newExpr);

                if (op.getPrecedence() > newOp.getPrecedence()) {
                    return new ParseResult(expr, newOp);
                }
            }

            op = newOp;
        }
    }

    private BinaryOperation<T> parseBinaryOperation() {
        final var op = parseRawOperation(binaryOperations);

        if (null != op) {
            return op;
        }

        throw UnexpectedCharacterException.fromCharSource(chars);
    }

    protected Expression<T> parseElement() {
        skipWhitespace();

        if (chars.test('(')) {
            return parseExpression(')');
        }

        final var pos = chars.position();

        final var minus = chars.test('-');

        if (chars.test(Character::isDigit, false)) {
            return new Const<>(parseInteger(minus));
        }

        if (minus) {
            chars.reset(pos);
        }

        final var op = parseRawOperation(unaryOperations);
        if (null != op) {
            return op.apply(parseElement());
        }

        if (chars.test(Character::isLetter, false)) {
            return new Variable<>(parseVariableName());
        }

        throw UnexpectedCharacterException.fromCharSource(chars);
    }

    protected <S> S parseRawOperation(final Map<String, S> dict) {
        final var isAlphabetical = chars.test(Character::isLetter, false);
        final CharMatcher condition = isAlphabetical ? ExpressionParser::isAlphaNumeric : ExpressionParser::isSimple;
        final var pos = chars.position();
        final var sb = new StringBuilder();

        while (chars.test(condition)) {
            sb.append(chars.current());
        }

        if (isAlphabetical) {
            final var op = sb.toString();
            if (dict.containsKey(op)) {
                return dict.get(op);
            }
            chars.reset(pos);
            return null;
        }

        //noinspection SizeReplaceableByIsEmpty
        while (0 < sb.length()) {
            final var op = sb.toString();
            if (dict.containsKey(op)) {
                chars.reset(pos + sb.length());
                return dict.get(op);
            }
            sb.setLength(sb.length() - 1);
        }

        chars.reset(pos);
        return null;
    }

    protected int parseInteger(final boolean negative) {
        final var startPos = chars.position();

        try {
            final var sb = new StringBuilder();
            if (negative) {
                sb.append('-');
            }

            while (chars.test(Character::isDigit)) {
                sb.append(chars.current());
            }

            return Integer.parseInt(sb.toString());
        } catch (final NumberFormatException e) {
            throw new ConstOverflowException(negative, negative ? startPos - 1 : startPos, e);
        }
    }

    protected String parseVariableName() {
        final var startPos = chars.position();
        final var sb = new StringBuilder();

        while (chars.test(Character::isLetter)) {
            sb.append(chars.current());
        }

        final var name = sb.toString();

        if (!ALLOWED_VARIABLE_NAMES.contains(name)) {
            throw new UnknownVariableException(name, startPos);
        }

        return name;

    }

    protected void skipWhitespace() {
        //noinspection StatementWithEmptyBody
        while (chars.test(Character::isWhitespace)) {
        }
    }

    static boolean isAlphaNumeric(final char ch) {
        return Character.isDigit(ch) || Character.isLetter(ch);
    }

    static boolean isSimple(final char ch) {
        return CharSource.END != ch && '(' != ch && !Character.isWhitespace(ch) && !isAlphaNumeric(ch);
    }

    private class ParseResult {
        private final Expression<T> expr;
        private final BinaryOperation<T> op;

        public ParseResult(final Expression<T> expr, BinaryOperation<T> op) {
            this.expr = expr;
            this.op = op;
        }
    }
}
