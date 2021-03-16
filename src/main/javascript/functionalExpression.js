'use strict';

const cnst = value => _ => value;
const variable = () => x => x;

const biOp = f => (first, second) => x => f(first(x), second(x));

const add = biOp((a, b) => a + b);
const subtract = biOp((a, b) => a - b);
const multiply = biOp((a, b) => a * b);
const divide = biOp((a, b) => a / b);

const negate = e => x => -e(x);

const flip = f => (a, b) => f(b, a);

const binaryOperations = {
    '+': add,
    '-': subtract,
    '*': multiply,
    '/': divide,
};

const parse = input => {
    const stack = [];

    for (let token of input.trim().split(/\s+/)) {
        token = token.trim();
        if ('x' === token) {
            stack.push(variable('x'));
        } else if (token in binaryOperations) {
            stack.push(flip(binaryOperations[token])(stack.pop(), stack.pop()));
        } else {
            stack.push(cnst(+token))
        }
    }

    return stack.pop();
};