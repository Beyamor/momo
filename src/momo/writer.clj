(ns momo.writer
  (:use [momo.util :only [keywordify-keys]])
  (:require [momo.core :as m
             :refer [monad]])
  (:refer-clojure :exclude [empty]))

(defn Writer
  [{:keys [empty append]}]
  (monad
    return (fn [x]
             [x empty])

    bind (fn [[x existing-log] f]
           (let [[new-x message] (f x)]
             [new-x (append existing-log message)]))))

(defn tell
  [message]
  [nil message])

(defmacro defwriter
  [name & {:as impl}]
  `(def ~name ~(keywordify-keys impl)))

(defwriter StringLog
           empty   ""
           append  str)
