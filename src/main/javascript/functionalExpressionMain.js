include('functionalExpression.js');

const expr = add(
    subtract(
        multiply(
            variable('x'),
            variable('x')
        ),
        multiply(
            cnst(2),
            variable('x')
        )
    ),
    cnst(-1)
);

for (let i = 0; i <= 10; ++i) {
    println(i, expr(i));
}
