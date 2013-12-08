(ns momo.state
  (:require [momo.core :as m
             :refer [defmonad]])
  (:refer-clojure :exclude [get set]))

(defmonad State
         return (fn [x]
                  (fn [state]
                    [x state]))

         bind (fn [transformer f]
                (fn [state]
                  (let [[v new-state] (transformer state)]
                    ((f v) new-state)))))

(def get
  (fn [state]
    [state state]))

(defn set
  [new-state]
  (fn [state]
    [nil new-state]))

(defn update
  [f]
  (fn [state]
    (let [updated-state (f state)]
      [updated-state updated-state])))
