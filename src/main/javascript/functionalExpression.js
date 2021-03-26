'use strict';

const cnst = value => _ => value;
const variable = name => {
    const index = vars[name];
    return (...values) => values[index];
};

const op = f => (...args) => (...values) => f(...args.map(arg => arg(...values)));

const add = op((a, b) => a + b);
const subtract = op((a, b) => a - b);
const multiply = op((a, b) => a * b);
const divide = op((a, b) => a / b);

const negate = op(x => -x);

const min5 = op(Math.min);
const max3 = op(Math.max);

const one = cnst(1);
const two = cnst(2);

const vars = {
    'x': 0,
    'y': 1,
    'z': 2,
};

const constTokens = {
    'one': one,
    'two': two,
};

const operations = {
    '+': [add, 2],
    '-': [subtract, 2],
    '*': [multiply, 2],
    '/': [divide, 2],
    'negate': [negate, 1],
    'min5': [min5, 5],
    'max3': [max3, 3],
};

const parse = input => {
    const stack = [];

    for (let token of input.trim().split(/\s+/)) {
        if (token in vars) {
            stack.push(variable(token));
        } else if (token in constTokens) {
            stack.push(constTokens[token]);
        } else if (token in operations) {
            const [op, arity] = operations[token];
            stack.push(op(...stack.splice(-arity)));
        } else {
            stack.push(cnst(+token));
        }
    }

    return stack.pop();
};