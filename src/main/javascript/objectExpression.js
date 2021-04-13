'use strict';

class Const {
    constructor(value) {
        this.value = value;
    }

    evaluate() {
        return this.value;
    }

    diff() {
        return Const.ZERO;
    }

    toString() {
        return this.value.toString();
    }

    prefix() {
        return this.toString();
    }

    postfix() {
        return this.toString();
    }
}

Const.ZERO = new Const(0);
Const.ONE = new Const(1);
Const.TWO = new Const(2);
Const.NEG_ONE = new Const(-1);
Const.E = new Const(Math.E);

class Variable {
    constructor(name) {
        this.name = name;
        this._index = Variable.registry.get(name);
    }

    evaluate(...args) {
        return args[this._index];
    }

    diff(d) {
        return d === this.name ? Const.ONE : Const.ZERO;
    }

    toString() {
        return this.name;
    }

    prefix() {
        return this.toString();
    }

    postfix() {
        return this.toString();
    }
}

Variable.registry = new Map([
    ['x', 0],
    ['y', 1],
    ['z', 2],
]);

class Operation {
    constructor(...operands) {
        this.args = operands;
    }

    evaluate(...vars) {
        return this.func(...this.args.map(arg=> arg.evaluate(...vars)));
    }

    diff(d) {
        return 0 === this.args.length
            ? Const.ZERO
            : this.args.map((arg, i) => new Multiply(
                arg.diff(d),
                this.derivative(i)
            )).reduce((a, b) => new Add(a, b));
    }

    toString() {
        return `${this.args.join(' ')} ${this.constructor.operator}`;
    }

    prefix() {
        return `(${this.constructor.operator} ${this.args.map(arg => arg.prefix()).join(' ')})`;
    }

    postfix() {
        return `(${this.args.map(arg => arg.postfix()).join(' ')} ${this.constructor.operator})`;
    }

    static register(operation, operator) {
        operation.arity = operation.prototype.func.length;
        operation.operator = operator;
        Operation.registry.set(operator, operation);
    }
}

Operation.registry = new Map();

class Add extends Operation {
    func(a, b) {
        return a + b;
    }

    derivative() {
        return Const.ONE;
    }
}
Operation.register(Add, '+');

class Subtract extends Operation {
    func(a, b) {
        return a - b;
    }

    derivative(arg) {
        return 0 === arg ? Const.ONE : Const.NEG_ONE;
    }
}
Operation.register(Subtract, '-');

class Multiply extends Operation {
    func(a, b) {
        return a * b;
    }

    derivative(arg) {
        return this.args[1 - arg];
    }
}
Operation.register(Multiply, '*');

class Divide extends Operation {
    func(a, b) {
        return a / b;
    }

    derivative(arg) {
        const [u, v] = this.args;
        return 0 === arg
            ? new Divide(Const.ONE, v)
            : new Divide(
                new Negate(u),
                new Multiply(v, v)
            );
    }
}
Operation.register(Divide, '/');

class Sumsq extends Operation {
    func(...args) {
        return args.reduce((sum, x) => sum + x * x, 0);
    }

    derivative(arg) {
        return new Multiply(Const.TWO, this.args[arg]);
    }
}
Operation.register(Sumsq, 'sumsq');

class Length extends Operation {
    func(...args) {
        return Math.sqrt(Sumsq.prototype.func(...args));
    }

    derivative(arg) {
        return new Divide(this.args[arg], this);
    }
}
Operation.register(Length, 'length');

class Negate extends Operation {
    func(x) {
        return -x;
    }

    derivative() {
        return Const.NEG_ONE;
    }
}
Operation.register(Negate, 'negate');

class ParseError extends Error {
    constructor(message, position) {
        super(`${message} at position ${1 + position}`);
    }
}
ParseError.prototype.name = ParseError.name;

const createParser = (() => {
    class Parser {
        constructor(input) {
            this.input = input;
            this.index = 0;
        }

        parse() {
            const expr = this.parseExpression();

            if (this.hasNext()) {
                throw new ParseError('End of string expected', this.index);
            }

            return expr;
        }

        parseExpression(inParentheses = false) {
            const start = this.index;
            this.skipWhitespaces();

            const tokens = [];
            while (this.hasNext() && ')' !== this.char()) {
                if (this.test('(')) {
                    tokens.push(this.parseExpression(true));
                    if (!this.test(')')) {
                        throw new ParseError('Missing )', 1 + this.index);
                    }
                } else {
                    tokens.push(this.parseArgument());
                }
                this.skipWhitespaces();
            }

            if (0 === tokens.length) {
                throw new ParseError('Empty expression', start);
            }

            const op = this.takeOp(tokens);
            const isOp = op.prototype instanceof Operation;

            if (inParentheses !== isOp) {
                throw new ParseError('Malformed parentheses usage', start);
            }

            if (isOp) {
                if (tokens.some(arg => !('evaluate' in arg))) {
                    throw new ParseError('Unevaluable expression', start);
                }

                if (0 !== op.arity && op.arity !== tokens.length) {
                    throw new ParseError('Invalid number of arguments', start);
                }

                return new op(...tokens);
            }

            if (0 !== tokens.length) {
                throw new ParseError('Unexpected sequence of arguments', start);
            }

            return op;
        }

        parseArgument() {
            const start = this.index;
            this.skip(c => !' ()'.includes(c));
            const token = this.input.substring(start, this.index);

            if (Operation.registry.has(token)) {
                return Operation.registry.get(token);
            }

            if (Variable.registry.has(token)) {
                return new Variable(token);
            }

            const num = +token;

            if (!isNaN(num)) {
                return new Const(num);
            }

            throw new ParseError('Unknown argument type', start);
        }

        hasNext() {
            return this.index < this.input.length;
        }

        char() {
            return this.input[this.index];
        }

        test(char) {
            if (char === this.char()) {
                ++this.index;
                return true;
            }
            return false;
        }

        skipWhitespaces() {
            this.skip(c => ' ' === c);
        }

        skip(predicate) {
            while (this.hasNext() && predicate(this.input[this.index])) {
                ++this.index;
            }
        }
    }

    return takeOp => {
        class NewParser extends Parser {
            takeOp(tokens) {
                return takeOp(tokens);
            }
        }
        return input => new NewParser(input).parse();
    };
})();

const parsePrefix = createParser(tokens => tokens.shift());
const parsePostfix = createParser(tokens => tokens.pop());
