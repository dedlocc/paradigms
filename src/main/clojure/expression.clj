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

(load-file "functional.clj")
(load-file "object.clj")
