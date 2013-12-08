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
           (let [[new-x message] (f x)]
             [new-x (append existing-log message)]))))

(defn tell
  [message]
  [nil message])

(extend-protocol Writeable
  clojure.lang.IPersistentCollection
  (append [this other]
    (concat this other))

  String
  (append [this other]
    (str this other)))
