(def constant constantly)
(defn variable [name] #(% name))

(defn op [f]
  (fn [& args]
    #(apply f ((apply juxt args) %))))

(def add (op +))
(def subtract (op -))
(def multiply (op *))
(def divide (op (fn
                  ([x] (/ x))
                  ([x & args] (/ (double x) (apply * args))))))
(def negate subtract)
(def sum add)
(def avg (op #(/ (apply + %&) (count %&))))

(def ops {
          '+ add
          '- subtract
          '* multiply
          '/ divide
          'negate negate
          'sum sum
          'avg avg})

(defn parseFunction' [expr]
  (cond
    (number? expr) (constant expr)
    (symbol? expr) (variable (str expr))
    (seq? expr) (apply
                  (ops (first expr))
                  (mapv parseFunction' (rest expr)))))

(defn parseFunction [str]
  (parseFunction' (read-string str)))
