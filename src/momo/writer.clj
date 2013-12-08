(ns momo.writer
  (:require [momo.core :as m
             :refer [monad]]))

(defprotocol Writeable
  (append [this other]))

(defn Writer
  [initial-log]
  (monad
    return (fn [x]
             [x initial-log])

    bind (fn [[x existing-log] f]
           (let [[new-x new-log] (f x)]
             [new-x (append existing-log new-log)]))))

(defn tell
  [log]
  [nil log])

(extend-protocol Writeable
  clojure.lang.IPersistentCollection
  (append [this other]
    (concat this other))

  String
  (append [this other]
    (str this other)))
