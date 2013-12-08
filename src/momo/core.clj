(ns momo.core
  (:use [momo.util :only [keywordify-keys]])
  (:refer-clojure :exclude [filter]))

(def monad-fs #{:bind :return :zero :plus})

(doseq [f monad-fs
        :let [sym (-> f name symbol)]]
  (eval
       `(declare ~(with-meta sym {:dynamic true}))))

(defn impl->monad
  [impl]
  (-> impl
    keywordify-keys
    (select-keys monad-fs)))

(defmacro monad
  [& {:as impl}]
  (impl->monad impl))

(defmacro defmonad
  [m & {:as impl}]
    `(def ~m ~(impl->monad impl)))

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

(defmacro chain
  [& exprs]
  (let [bindings (->>
                   (for [expr (butlast exprs)]
                     [(gensym) expr])
                   (apply concat)
                   vec)]
    `(<- ~bindings
         ~(last exprs))))

(defn lift
  [f mv]
  (<- [x mv]
      (return (f x))))

(defn join
  [mv]
  (<- [inner-mv mv]
      inner-mv))

(defn filter
  [pred? xs]
  (if (empty? xs)
    (return [])
    (let [[x & xs] xs
          filtered-xs (filter pred? xs)]
      (<- [keep? (pred? x)]
          (if keep?
            (lift (partial cons x) filtered-xs)
            filtered-xs)))))

(defmonad Seq
          return (fn [v]
                   [v])

          bind (fn [s f]
                 (apply concat
                        (for [v s]
                          (f v))))

          zero []

          plus concat)

(defmonad Maybe
          return identity

          bind (fn [v f]
                 (when-not (nil? v)
                   (f v))))

(defmonad Err
          return (fn [x]
                   {:right x})

          bind (fn [mv f]
                 (if (contains? mv :right)
                   (f (:right mv))
                   mv)))
