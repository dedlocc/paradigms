:- load_library('alice.tuprolog.lib.DCGLibrary').

lookup(K, [(K, V) | _], V).
lookup(K, [_ | T], V) :- lookup(K, T, V).

operation(op_add, A, B, R) :- R is A + B.
operation(op_subtract, A, B, R) :- R is A - B.
operation(op_multiply, A, B, R) :- R is A * B.
operation(op_divide, A, B, R) :- R is A / B.
operation(op_negate, A, R) :- R is -A.
operation(op_sinh, A, R) :- R is (exp(A) - exp(-A)) / 2.
operation(op_cosh, A, R) :- R is (exp(A) + exp(-A)) / 2.

evaluate(const(V), _, V).
evaluate(variable(Name), Vars, R) :-
    atom_chars(Name, [RealName | _]),
    lookup(RealName, Vars, R).
evaluate(operation(Op, A), Vars, R) :-
    evaluate(A, Vars, AV),
    operation(Op, AV, R).
evaluate(operation(Op, A, B), Vars, R) :-
    evaluate(A, Vars, AV),
    evaluate(B, Vars, BV),
    operation(Op, AV, BV, R).

nonvar(V, _) :- var(V).
nonvar(V, T) :- nonvar(V), call(T).

expr_p(variable(Var)) -->
    { nonvar(Var, atom_chars(Var, Chars)) },
    var_p(Chars),
    { atom_chars(Var, Chars) }.

var_letter_p(V) --> { member(V, [x, y, z, 'X', 'Y', 'Z']) }, [V].
var_p([H]) --> var_letter_p(H).
var_p([H | T]) --> var_letter_p(H), var_p(T).

expr_p(const(Value)) -->
    { nonvar(Value, number_chars(Value, Chars)) },
    number_p(Chars),
    { number_chars(Value, Chars) }.

digit_p(D) --> { member(D, ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.']) }, [D].
digits_p([H]) --> digit_p(H).
digits_p([H | T]) --> digit_p(H), digits_p(T).

number_p(Chars) --> digits_p(Chars).
number_p(['-' | T]) --> ['-'], digits_p(T).

operator_p(Op) --> { atom_chars(Op, C) }, C.

op_p(op_add, '+').
op_p(op_subtract, '-').
op_p(op_multiply, '*').
op_p(op_divide, '/').
op_p(op_sinh, 'sinh').
op_p(op_cosh, 'cosh').
op_p(op_negate, 'negate').

op_p(Op) --> { op_p(Op, S) }, operator_p(S).

skip_ws([], []).
skip_ws([' ' | T], RT) :- !, skip_ws(T, RT).
skip_ws([H | T], [H | RT]) :- skip_ws(T, RT).

expr_p(operation(Op, A, B)) -->
    { var(Op) -> Ws = []; Ws = [' '] },
    ['('], expr_p(A), Ws, op_p(Op), Ws, expr_p(B), [')'].
expr_p(operation(Op, A)) --> op_p(Op), ['('], expr_p(A), [')'].

infix_str(E, A) :- ground(E), phrase(expr_p(E), C), atom_chars(A, C).
infix_str(E, A) :-   atom(A), atom_chars(A, C), skip_ws(C, C1), phrase(expr_p(E), C1).
