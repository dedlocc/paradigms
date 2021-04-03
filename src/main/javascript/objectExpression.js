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

    simplify() {
        return this;
    }

    equals(expr) {
        return expr instanceof Const && expr.value === this.value;
    }
}

Const.ZERO = new Const(0);
Const.ONE = new Const(1);
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
        return new Const(d === this.name ? 1 : 0);
    }

    toString() {
        return this.name;
    }

    simplify() {
        return this;
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
        return this.func(...this.args.map(o => o.evaluate(...vars)));
    }

    diff(d) {
        return this.args.map((o, arg) => new Multiply(
            o.diff(d),
            this.derivative(arg)
        )).reduce((a, b) => new Add(a, b));
    }

    toString() {
        return this.args.join(' ') + ' ' + this.constructor.operator;
    }

    simplify() {
        return new this.constructor(...this.args.map(o => o.simplify()))._simplifyImpl();
    }

    _simplifyImpl() {
        if (this.args.every(o => o instanceof Const)) {
            return new Const(this.evaluate());
        }
        return this;
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

    _simplifyImpl() {
        const [u, v] = this.args;
        if (Const.ZERO.equals(v)) {
            return u;
        }
        if (Const.ZERO.equals(u)) {
            return v;
        }
        return super._simplifyImpl();
    }
}
Operation.register(Add, '+');

class Subtract extends Operation {
    func(a, b) {
        return a - b;
    }

    derivative(arg) {
        return 0 === arg ? Const.ONE : new Const(-1);
    }

    _simplifyImpl() {
        const [u, v] = this.args;
        if (Const.ZERO.equals(v)) {
            return u;
        }
        if (Const.ZERO.equals(u)) {
            return new Negate(v);
        }
        return super._simplifyImpl();
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

    _simplifyImpl() {
        const [u, v] = this.args;
        if (Const.ZERO.equals(u) || Const.ONE.equals(v)) {
            return u;
        }
        if (Const.ZERO.equals(v) || Const.ONE.equals(u)) {
            return v;
        }
        return super._simplifyImpl();
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

    _simplifyImpl() {
        const [u, v] = this.args;
        if (Const.ZERO.equals(u) || Const.ONE.equals(v)) {
            return u;
        }
        return super._simplifyImpl();
    }
}
Operation.register(Divide, '/');

class Cube extends Operation {
    func(x) {
        return x ** 3;
    }

    derivative() {
        const [f] = this.args;
        return new Multiply(
            new Const(3),
            new Multiply(f, f)
        );
    }
}
Operation.register(Cube, 'cube');

class Cbrt extends Operation {
    func(x) {
        return Math.cbrt(x);
    }

    derivative() {
        const [f] = this.args;
        return new Divide(
            Const.ONE,
            new Multiply(
                new Const(3),
                new Cbrt(
                    new Multiply(f, f)
                )
            )
        );
    }
}
Operation.register(Cbrt, 'cbrt');

class Negate extends Operation {
    func(x) {
        return -x;
    }

    derivative() {
        return Const.NEG_ONE;
    }
}
Operation.register(Negate, 'negate');

const parse = input => {
    const stack = [];

    for (let token of input.trim().split(/\s+/)) {
        if (Variable.registry.has(token)) {
            stack.push(new Variable(token));
        } else if (Operation.registry.has(token)) {
            const op = Operation.registry.get(token);
            stack.push(new op(...stack.splice(-op.arity)));
        } else {
            stack.push(new Const(+token));
        }
    }

    return stack.pop();
};
