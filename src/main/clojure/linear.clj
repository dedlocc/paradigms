(defn vector-of?
  ([elem?]
   (fn [v] (vector-of? elem? v)))
  ([elem? v]
   (and (vector? v) (every? elem? v))))

(defn replace-with-zeros [v]
  {:pre [(or (vector? v) (number? v))]}
  (if (number? v)
    0
    (mapv replace-with-zeros v)))

(defn same-form? [vs pred?]
  {:pre [(coll? vs)]}
  (or
    (empty? vs)
    (and
      (pred? (first vs))
      (apply = (mapv replace-with-zeros vs)))))

(defn op [valid-arg?]
  (fn [f]
    (fn [& args]
      {:pre [(same-form? args valid-arg?)]}
      (apply
        (fn op' [& args]
          (if (every? number? args)
            (apply f args)
            (apply mapv op' args)))
        args))))

(def v? (vector-of? number?))
(def v-op (op v?))

(def v+ (v-op +))
(def v- (v-op -))
(def v* (v-op *))
(def vd (v-op /))

(defn scalar [& vs]
  {:pre [(same-form? vs v?)]}
  (apply + (apply v* vs)))

(defn v*s [v & ss]
  {:pre [(v? v) (every? number? ss)]}
  (let [s (apply * ss)]
    (mapv #(* % s) v)))


(def m? #(same-form? % v?))
(def m-op (op m?))

(def m+ (m-op +))
(def m- (m-op -))
(def m* (m-op *))
(def md (m-op /))

(defn transpose [m]
  {:pre [(m? m)]}
  (apply mapv vector m))

(defn m*s [m & ss]
  {:pre [(m? m) (every? number? ss)]}
  (let [s (apply * ss)]
    (mapv #(apply v*s % s) m)))

(defn m*v [m v]
  {:pre [(m? m) (v? v)]}
  (mapv (partial scalar v) m))

(defn m*m [& ms]
  {:pre [(every? m? ms)]}
  (reduce
    (fn [a b]
      (let [tb (transpose b)]
        (mapv #(m*v tb %) a)))
    ms))

(defn vect [& vs]
  {:pre [(same-form? vs (every-pred v? #(== 3 (count %))))]}
  (reduce
    (partial
      (fn [[x y z] b]
        (m*v
          [[0 (- z) y]
           [z 0 (- x)]
           [(- y) x 0]]
          b)))
    vs))

(defn x? [x]
  (or
    (number? x)
    (v? x)
    (and
      (vector-of? (every-pred vector? x?) x)
      (let [size (count x)]
        (every?
          true?
          (map-indexed
            #(== (count %2) (- size %1))
            x))))))

(def x-op (op x?))

(def x+ (x-op +))
(def x- (x-op -))
(def x* (x-op *))
(def xd (x-op /))
