'use strict';

include('functionalExpression.js');

const expr = parse("x x * 2 x * - 1 +");

for (let i = 0; i <= 10; ++i) {
    println(i, expr(i));
}
