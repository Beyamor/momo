(ns momo.core)

(def monad-fs #{:bind :return :zero :plus})

(doseq [f monad-fs
        :let [sym (-> f name symbol)]]
  (eval
       `(declare ~(with-meta sym {:dynamic true}))))

(defn keywordify-keys
  [m]
  (into {}
        (for [[k v] m]
          [(keyword k) v])))

(defmacro defmonad
  [m & {:as impl}]
  (let [impl (-> impl
               keywordify-keys
               (select-keys monad-fs))]
    `(def ~m ~impl)))

(defn bindings
  [m]
  (->>
    (for [f monad-fs
          :let [qualified-f (symbol "momo.core" (name f))]]
      `(~qualified-f (~m ~f)))
    (apply concat)
    vec))

(defmacro with
  [m & body]
  `(binding ~(bindings m)
     ~@body))

(defmacro <-
  [bindings & body]
  (reduce
    (fn [body [sym expr]]
      `(bind ~expr
             (fn [~sym]
               ~body)))
    `(do ~@body)
    (->> bindings
      (partition 2)
      reverse)))

(defmonad Seq
          return (fn [v]
                   [v])

          bind (fn [s f]
                 (apply concat
                        (for [v s]
                          (f v))))

          zero []

          plus concat)
