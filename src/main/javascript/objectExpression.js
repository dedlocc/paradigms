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
        return 0 === arg ? Const.ONE : new Const(-1);
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
        return 0 === arg
            ? new Divide(Const.ONE, this.args[1])
            : new Divide(
                new Negate(this.args[0]),
                new Multiply(this.args[1], this.args[1])
            );
    }
}
Operation.register(Divide, '/');

class Cube extends Operation {
    func(x) {
        return x ** 3;
    }

    derivative() {
        return new Multiply(
            new Const(3),
            new Multiply(this.args[0], this.args[0])
        );
    }
}
Operation.register(Cube, 'cube');

class Cbrt extends Operation {
    func(x) {
        return Math.cbrt(x);
    }

    derivative() {
        return new Divide(
            Const.ONE,
            new Multiply(
                new Const(3),
                new Cbrt(
                    new Multiply(this.args[0], this.args[0])
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
