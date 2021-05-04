(chapter "Macro")

(section "Showcase")
(example "Code generation"
         (defmacro infix [[a op b]]
           (list op a b))
         (macroexpand '(infix (10 + 20)))
         (infix (10 + 20)))

(example "Syntax quote"
         (defmacro infix [[a op b]]
           `(~op ~a ~b))
         (macroexpand '(infix (10 + 20)))
         (infix (10 + 20)))

(example "Recursion"
         (defmacro infix [v]
           (cond
             (list? v) (let [[a op b] v] `(~op (infix ~a) (infix ~b)))
             :else v))
         (infix (10 + 20))
         (macroexpand '(infix (10 + (a * 3))))
         (let [a 2] (infix (10 + (a * 3)))))

(section "JS-like Objects")
(example "Single field"
         (defmacro deffield
           "Defines field"
           [name]
           (let [key (keyword (subs (str name) 1))]
             `(defn ~name
                ([this#] (proto-get this# ~key))
                ([this# def#] (proto-get this# ~key def#)))))
         (macroexpand '(deffield _x))
         (deffield _x)
         (_x {:x 100})
         (_x {})
         (_x {} 100))

(example "Multiple fields"
         (defmacro deffields
           "Defines fields"
           [& names]
           `(do ~(mapv (fn [name] `(deffield ~name)) names)))
         (macroexpand '(deffields _x _y))
         (deffields _x _y)
         (_x {:x 100})
         (_y {:y 100}))

(example "Single method"
         (defmacro defmethod
           "Defines method"
           [name]
           (let [key (keyword (subs (str name) 1))]
             `(defn ~name [this# & args#] (apply proto-call this# ~key args#))))
         (macroexpand '(defmethod _getX))
         (defmethod _getX)
         (_getX {:getX (fn [this] 10)})
         (defmethod _add)
         (_add {:add (fn [this a b] (+ a b))} 10 20))

(example "Multiple methods"
         (defmacro defmethods
           "Defines methods"
           [& names]
           `(do ~(mapv (fn [name] `(defmethod ~name)) names)))
         (macroexpand '(defmethods _getX _getY))
         (defmethods _getX _getY)
         (_getX {:getX (fn [this] 10)})
         (_getY {:getY _y :y 20}))

(example "Constructors"
         (defmacro defconstructor
           "Defines constructor"
           [name ctor prototype]
           `(defn ~name [& args#] (apply ~ctor {:prototype ~prototype} args#)))
         (macroexpand '(defconstructor _Point Point PointPrototype)))

(example "Point"
         (deffields _x _y)
         (defmethods _getX _getY _sub _length _distance)
         (def _Point)
         (def PointPrototype
           {:getX (fn [this] (_x this))
            :getY _y
            :sub (fn [this that] (_Point (- (_getX this) (_getX that))
                                         (- (_getY this) (_getY that))))
            :length (fn [this] (let [square #(* % %)] (Math/sqrt (+ (square (_getX this)) (square (_getY this))))))
            :distance (fn [this that] (_length (_sub this that)))
            })
         (defn Point [this x y]
           (assoc this
             :x x
             :y y))
         (defconstructor _Point Point PointPrototype)
         (_length (_Point 3 4))
         (_distance (_Point 5 5) (_Point 1 2)))

(example "Shifted point"
         (deffields _dx _dy)
         (def ShiftedPointPrototype
           (assoc PointPrototype
             :getX (fn [this] (+ (_x this) (_dx this)))
             :getY (fn [this] (+ (_y this) (_dy this)))))
         (defn ShiftedPoint [this x y dx dy]
           (assoc (Point this x y)
             :dx dx
             :dy dy
             ))
         (defconstructor _ShiftedPoint ShiftedPoint ShiftedPointPrototype)
         (_distance (_ShiftedPoint 2 2 3 3) (_Point 1 2)))
