(defn simple-parser [const variable ops]
  (letfn
    [(parser' [expr]
       (cond
         (number? expr) (const expr)
         (symbol? expr) (variable (str expr))
         (seq? expr) (apply
                       (ops (first expr))
                       (mapv parser' (rest expr)))))]
    #(parser' (read-string %))))

(defn div
  ([x] (/ 1.0 x))
  ([x & args] (/ (double x) (apply * args))))
(def average #(/ (apply + %&) (count %&)))

(load-file "functional.clj")
(load-file "object.clj")
(load-file "parser.clj")

(defparser parseObjectInfix
           *all-chars (mapv char (range 0 128))
           (*chars [p] (+char (apply str (filter p *all-chars))))
           *letter (*chars #(Character/isLetter %))
           *digit (*chars #(Character/isDigit %))
           *space (*chars #(Character/isWhitespace %))
           *ws (+ignore (+star *space))
           *int (+str (+plus *digit))
           *const (+seqf (comp Constant read-string str) (+opt \-) *int \. *int)
           *var (+map Variable (+str (+plus (+char "xyzXYZ"))))
           (*id [id] (apply +seqf (constantly id) (mapv #(_char #{%}) (str id))))
           (*op [ops] (+map object-ops (apply +or (mapv *id ops))))
           *arg (+or *const *var (delay *unary) (+seqn 1 \( (delay *expr) \)))
           *unary (+seqf #(%1 %2) (*op ['negate 'avg]) *ws *arg)
           (*fold [order] #(letfn [(fold ([x] x) ([x op y & rest] (apply fold (apply op (order [x y])) rest)))]
                             (apply fold (order (flatten %&)))))
           (*binary [order] (fn [sub & ops] (+seqf (*fold order) sub (+star (+seq *ws (*op ops) *ws sub)))))
           *left-binary (*binary identity)
           *right-binary (*binary reverse)
           *powlog (*right-binary *arg '** (symbol "//"))
           *multiplicative (*left-binary *powlog '* '/)
           *additive (*left-binary *multiplicative '+ '-)
           *expr (+seqn 0 *ws *additive *ws))
