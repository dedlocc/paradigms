init(M) :-
    B is floor(sqrt(M)),
    exclude(2, 4, M),
    sieve(3, B, M), !.

sieve(I, B, _) :- I > B, !.
sieve(I, B, M) :-
    (composite(I) ; I2 is I * I, exclude(I, I2, M)),
    I1 is I + 2,
    sieve(I1, B, M).

exclude(_, J, M) :- J > M, !.
exclude(I, J, M) :-
    assert(composite(J)),
    J1 is J + I,
    exclude(I, J1, M).

prime(2) :- !.
prime(N) :-
    N > 2,
    \+ composite(N).

least_divisor(N, I, I) :- 0 is N mod I, !.
least_divisor(N, R, I) :-
    I1 is I + 1,
    least_divisor(N, R, I1).

prime_divisors(1, []) :- !.
prime_divisors(N, [N]) :- prime(N), !.
prime_divisors(N, L) :- number(N), !, prime_divisors(N, L, 2).
prime_divisors(N, L) :- product(N, L).

prime_divisors(N, [N], _) :- prime(N), !.
prime_divisors(N, [H | T], I) :-
    least_divisor(N, H, I),
    N1 is N / H,
    prime_divisors(N1, T, H).

product(R, [H1, H2 | T]) :-
    H1 =< H2,
    prime(H1),
    prime_divisors(R1, [H2 | T]),
    R is R1 * H1.

intersection([], _, []) :- !.
intersection(_, [], []) :- !.
intersection([H | T1], [H | T2], [H | TR]) :- intersection(T1, T2, TR).
intersection([H1 | T1], [H2 | T2], R) :- H1 < H2, intersection(T1, [H2 | T2], R).
intersection([H1 | T1], [H2 | T2], R) :- H1 > H2, intersection([H1 | T1], T2, R).

gcd(A, B, GCD) :-
    prime_divisors(A, PA),
    prime_divisors(B, PB),
    intersection(PA, PB, I),
    prime_divisors(GCD, I).
