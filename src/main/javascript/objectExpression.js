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
}

Const.ZERO = new Const(0);
Const.ONE = new Const(1);
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

    toString() {
        return this.args.join(' ') + ' ' + this.constructor.operator;
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

    diff(d) {
        return new Add(this.args[0].diff(d), this.args[1].diff(d));
    }
}
Operation.register(Add, '+');

class Subtract extends Operation {
    func(a, b) {
        return a - b;
    }

    diff(d) {
        return new Subtract(this.args[0].diff(d), this.args[1].diff(d));
    }
}
Operation.register(Subtract, '-');

class Multiply extends Operation {
    func(a, b) {
        return a * b;
    }

    diff(d) {
        const [u, v] = this.args;
        return new Add(
            new Multiply(u.diff(d), v),
            new Multiply(v.diff(d), u)
        );
    }
}
Operation.register(Multiply, '*');

class Divide extends Operation {
    func(a, b) {
        return a / b;
    }

    diff(d) {
        const [u, v] = this.args;
        return new Divide(
            new Subtract(
                new Multiply(u.diff(d), v),
                new Multiply(v.diff(d), u)
            ),
            new Multiply(v, v)
        );
    }
}
Operation.register(Divide, '/');

class Cube extends Operation {
    func(x) {
        return x ** 3;
    }

    diff(d) {
        const f = this.args[0];
        return new Multiply(
            new Multiply(
                new Const(3),
                f.diff(d)
            ),
            new Multiply(f, f)
        );
    }
}
Operation.register(Cube, 'cube');

class Cbrt extends Operation {
    func(x) {
        return Math.cbrt(x);
    }

    diff(d) {
        const f = this.args[0];
        return new Divide(
            f.diff(d),
            new Multiply(
                new Const(3),
                new Cbrt(new Multiply(f, f))
            )
        );
    }
}
Operation.register(Cbrt, 'cbrt');

class Negate extends Operation {
    func(x) {
        return -x;
    }

    diff(d) {
        return new Negate(this.args[0].diff(d));
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
