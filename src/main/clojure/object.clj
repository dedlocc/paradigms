(declare ZERO ONE NEG_ONE)

(definterface Expression
  (evaluate [args])
  (diff [d])
  (toStringInfix []))

(deftype Constant' [value]
  Expression
  (evaluate [_ _] value)
  (diff [_ _] ZERO)
  (toStringInfix [this] (.toString this))
  Object
  (toString [_] (format "%.1f" (double value))))

(deftype Variable' [name]
  Expression
  (evaluate [_ args] (-> name first clojure.string/lower-case args))
  (diff [_ d] (if (= d name) ONE ZERO))
  (toStringInfix [this] (.toString this))
  Object
  (toString [_] name))

(defn Constant [value] (Constant'. value))
(defn Variable [name] (Variable'. name))

(def ZERO (Constant 0))
(def ONE (Constant 1))
(def NEG_ONE (Constant -1))

(defn drop-nth [v n] (concat (take n v) (drop (inc n) v)))

(declare Add Multiply Subtract Divide Negate Sum Avg IPow ILog)

(defmacro defop [name operand f d]
  (let [name' (symbol (str name "Op"))]
    `(do
       (deftype ~name' [ops#]
         Expression
         (evaluate [_ args#] (apply ~f (mapv #(.evaluate % args#) ops#)))
         (diff [_ d#] (apply Add (map-indexed #(Multiply (apply ~d %1 ops#) (.diff %2 d#)) ops#)))
         (toStringInfix [_] (if (== 1 (count ops#))
                              (str ~operand "(" (.toStringInfix (first ops#)) ")")
                              (str "(" (.toStringInfix (first ops#)) " " ~operand " " (.toStringInfix (second ops#)) ")")))
         Object
         (toString [_] (str "(" ~operand " " (clojure.string/join " " ops#) ")")))
       (defn ~name [& ops#] (new ~name' ops#)))))

(defop Add "+" + (constantly ONE))
(defop Subtract "-" - #(if (and (not= 1 (count %&)) (zero? %1)) ONE NEG_ONE))
(defop Multiply "*" * #(apply Multiply (drop-nth %& %1)))
(defop Divide "/" div
       (fn
         ([_ x] (Negate (Divide (Multiply x x))))
         ([i x & rest] (if (zero? i)
                         (Divide (apply Multiply rest))
                         (Negate (Divide x (let [y (nth rest (dec i))]
                                             (apply Multiply y y (drop-nth rest (dec i))))))))))
(defop Negate "negate" - (constantly NEG_ONE))
(defop Sum "sum" + (constantly ONE))
(defop Avg "avg" average #(Divide (Constant (count %&))))
(defop IPow "**" #(Math/pow %1 %2) identity)
(defop ILog "//" #(/ (Math/log (Math/abs ^double %2)) (Math/log (Math/abs ^double %1))) identity)

(defn evaluate [expr args] (.evaluate expr args))
(defn diff [expr d] (.diff expr d))
(defn toString [expr] (.toString expr))
(defn toStringInfix [expr] (.toStringInfix expr))

(def object-ops {'+            Add,
                 '-            Subtract
                 '*            Multiply
                 '/            Divide
                 'negate       Negate
                 'sum          Sum
                 'avg          Avg
                 '**           IPow
                 (symbol "//") ILog})

(def parseObject (simple-parser Constant Variable object-ops))
