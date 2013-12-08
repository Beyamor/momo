(ns momo.writer
  (:require [momo.core :as m
             :refer [monad]])
  (:refer-clojure :exclude [empty]))

(defmulti writer identity)

(defn Writer
  [writer-type]
  (let [{:keys [empty append]} (writer writer-type)]
    (monad
      return (fn [x]
               [x empty])

      bind (fn [[x existing-log] f]
             (let [[new-x message] (f x)]
               [new-x (append existing-log message)])))))

(defn tell
  [message]
  [nil message])

(defmethod writer :string
  [_]
  {:empty   ""
   :append  str})
