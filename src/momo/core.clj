(ns momo.core)

(declare ^:dynamic bind)
(declare ^:dynamic return)

(declare <-)

(def monad-fs #{:bind :return})

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
    (for [f monad-fs]
      `(~(symbol "momo.core" (name f)) (~m ~f)))
    (apply concat)
    vec))

(defmacro with
  [m & body]
  `(binding ~(bindings m)
     ~@body))

(defmonad Seq
          return (fn [v]
                   [v]))
