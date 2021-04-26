(chapter "Church Encoding")

(section "Pairs")
(example "Basic declarations"
         (def pair (fn [f] (fn [s] (fn [p] ((p f) s)))))
         (def fst (fn [p] (p (fn [f] (fn [s] f)))))
         (def snd (fn [p] (p (fn [f] (fn [s] s))))))
(example "Instance"
         (def pp ((pair 10) 20))
         pp
         (fst pp)
         (snd pp))

(section "Numbers")
(example "zero and succ"
         ;(def zero (fn [f] (fn [x] x)))
         (def zero (constantly identity))
         ;(defn succ [n] (fn [f] (fn [x] (f ((n f) x)))))
         (defn succ [n] (fn [f] (comp f (n f)))))
(example "Conversion to integer"
         (defn to-int [n] ((n inc) 0)))
(example "Values"
         (def one (succ zero))
         (def two (succ one))
         (def three (succ two))
         (to-int zero)
         (to-int one)
         (to-int two)
         (to-int three))
(example "Addition"
         (defn n+ [a b] (fn [f] (comp (a f) (b f))))
         (to-int (n+ zero zero))
         (to-int (n+ two three)))
(example "Multiplication"
         (defn n* [a b] (comp a b))
         (to-int (n* zero zero))
         (to-int (n* two three)))
(example "Predecessor" (defn pred' [[_ v]] [v (succ v)])
         (defn pred [n] (first ((n pred') [zero zero])))
         (to-int (pred three))
         (to-int (pred two))
         (to-int (pred one))
         (to-int (pred zero)))
(example "Subtraction"
         (defn n- [a b] ((b pred) a))
         (to-int (n- three one))
         (to-int (n- three two))
         (to-int (n- one one))
         (to-int (n- one three)))

(section "Booleans")
(example "Values"
         (defn b-true [f s] f)
         (defn b-false [f s] s))
(example "Conversion to ordinary Booleans"
         (defn to-boolean [b] (b true false))
         (to-boolean b-true)
         (to-boolean b-false))
(example "Not"
         ;(defn b-not [b] (fn [f s] (b s f)))
         (defn b-not [b] (b b-false b-true))
         (to-boolean (b-not b-true))
         (to-boolean (b-not b-false)))
(example "And"
         (defn b-and [a b] (a b b-false))
         (to-boolean (b-and b-false b-false))
         (to-boolean (b-and b-true b-false))
         (to-boolean (b-and b-false b-true))
         (to-boolean (b-and b-true b-true)))
(example "Or"
         (defn b-or [a b] (a b-true b))
         (to-boolean (b-or b-false b-false))
         (to-boolean (b-or b-true b-false))
         (to-boolean (b-or b-false b-true))
         (to-boolean (b-or b-true b-true)))

(section "Predicates")
(example "= 0"
         (defn is-zero? [n] ((n (constantly b-false)) b-true))
         (to-boolean (is-zero? zero))
         (to-boolean (is-zero? one))
         (to-boolean (is-zero? three)))
(example "<="
         (defn less-or-equal? [a b] (is-zero? (n- a b)))
         (to-boolean (less-or-equal? one three))
         (to-boolean (less-or-equal? one one))
         (to-boolean (less-or-equal? three one)))
(example "=="
         (defn equal? [a b] (b-and (less-or-equal? a b) (less-or-equal? b a)))
         (to-boolean (equal? one three))
         (to-boolean (equal? one one))
         (to-boolean (equal? three one)))

(section "Signed numbers")
(example "Basic definitions"
         (defn signed
           ([p] [p zero])
           ([p n] [p n]))
         (defn negate [[p n]] [n p]))
(example "Conversion to integer"
         (defn signed-to-int [[p n]] ((p inc) ((n dec) 0)))
         (signed-to-int (signed zero))
         (signed-to-int (signed two))
         (signed-to-int (negate (signed zero)))
         (signed-to-int (negate (signed two))))
(example "Addition"
         (defn s+ [[ap an] [bp bn]] [(n+ ap bp) (n+ an bn)])
         (signed-to-int (s+ (signed one) (signed two)))
         (signed-to-int (s+ (signed one) (negate (signed two)))))
(example "Subtraction"
         (defn s- [a b] (s+ a (negate b)))
         (signed-to-int (s- (signed one) (signed two)))
         (signed-to-int (s- (signed one) (negate (signed two)))))
(example "Multiplication"
         (defn s* [[ap an] [bp bn]]
           [(n+ (n* ap bp) (n* bp bn))
            (n+ (n* ap bn) (n* an bp))])
         (signed-to-int (s* (signed three) (signed two)))
         (signed-to-int (s* (signed three) (negate (signed two)))))
