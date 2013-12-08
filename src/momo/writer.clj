(ns momo.writer
  (:use [momo.util :only [keywordify-keys]])
  (:require [momo.core :as m
             :refer [monad <- return]])
  (:refer-clojure :exclude [empty]))

(defn Writer
  [{:keys [empty append]}]
  (monad
    return (fn [x]
             [x empty])

    bind (fn [[x existing-log]    f]
           (let [[new-x new-log]  (f x)]
             [new-x (append existing-log new-log)]))))

(defn tell
  [log]
  [nil log])

(defn listen
  [[x log]]
  [[x log] log])

(defn pass
  [[a f] log]
  [a (f log)])

(defn censor
  [f m]
  (pass
    (<- [a m]
          (return [a f]))))

(defmacro deflog
  [name & {:as impl}]
  `(def ~name ~(keywordify-keys impl)))

(deflog StringLog
        empty   ""
        append  str)
