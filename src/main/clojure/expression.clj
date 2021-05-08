; Common

(defn parser [const variable ops]
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

(defn drop-nth [v n] (concat (take n v) (drop (inc n) v)))

; Object

(declare ZERO ONE NEG_ONE)

(definterface Expression
  (evaluate [args])
  (diff [d]))

(deftype Constant' [value]
  Expression
  (evaluate [_ _] value)
  (diff [_ _] ZERO)
  Object
  (toString [_] (format "%.1f" (double value))))

(deftype Variable' [name]
  Expression
  (evaluate [_ args] (args name))
  (diff [_ d] (if (= d name) ONE ZERO))
  Object
  (toString [_] name))

(defn Constant [value] (Constant'. value))
(defn Variable [name] (Variable'. name))

(def ZERO (Constant 0))
(def ONE (Constant 1))
(def NEG_ONE (Constant -1))

(declare Add Multiply Subtract Divide Negate Sum Avg)

(defmacro defop [name operand f d]
  (let [name' (symbol (str name "Op"))]
    `(do
       (deftype ~name' [ops#]
         Expression
         (evaluate [_ args#] (apply ~f (mapv #(.evaluate % args#) ops#)))
         (diff [_ d#] (apply Add (map-indexed #(Multiply (~d %1 ops#) (.diff %2 d#)) ops#)))
         Object
         (toString [_] (str "(" ~operand " " (clojure.string/join " " ops#) ")")))
       (defn ~name [& ops#] (new ~name' ops#)))))

(defop Add "+" + (constantly ONE))
(defop Subtract "-" - #(if (and (not= 1 (count %2)) (zero? %1)) ONE NEG_ONE))
(defop Multiply "*" * #(apply Multiply (drop-nth %2 %1)))
(defop Divide "/" div
       (fn [i args]
         (cond
           (== 1 (count args)) (let [x (first args)] (Negate (Divide (Multiply x x))))
           (zero? i) (Divide (apply Multiply (rest args)))
           :else (Negate (Divide
                           (first args)
                           (let [x (nth args i)]
                             (apply Multiply x x (rest (drop-nth args i)))))))))
(defop Negate "negate" - (constantly NEG_ONE))
(defop Sum "sum" + (constantly ONE))
(defop Avg "avg" average #(Divide (Constant (count %2))))

(defn evaluate [expr args] (.evaluate expr args))
(defn diff [expr d] (.diff expr d))
(defn toString [expr] (.toString expr))

(def parseObject
  (parser
    Constant
    Variable
    {
     '+ Add,
     '- Subtract
     '* Multiply
     '/ Divide
     'negate Negate
     'sum Sum
     'avg Avg}))

; Functional

(def constant constantly)
(defn variable [name] #(% name))

(defn op [f]
  (fn [& args]
    (fn [vals]
      (apply f (mapv #(% vals) args)))))

(def add (op +))
(def subtract (op -))
(def multiply (op *))
(def divide (op div))
(def negate subtract)
(def sum add)
(def avg (op average))

(def parseFunction
  (parser
    constant
    variable
    {
     '+ add
     '- subtract
     '* multiply
     '/ divide
     'negate negate
     'sum sum
     'avg avg}))
