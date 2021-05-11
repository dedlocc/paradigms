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
    {'+ add
     '- subtract
     '* multiply
     '/ divide
     'negate negate
     'sum sum
     'avg avg}))
