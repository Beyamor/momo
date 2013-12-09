(ns momo.cont
  (:require [momo.core :as m
             :refer [defmonad]]))

(defmonad Cont
          return
          ; returns a function which
          (fn [x]
            ;when given the next step of the computation, 
            (fn [c]
              ; will pass it the (unmodified) x
              (c x)))

          bind
          ; mv is the previous step in the computation
          ; (f v) will produce the current step
          (fn [mv f]
            ; c is the next step
            (fn [c]
              ; the continuation passed to mv
              ; accepts the result of mv's computation
              ; applies that to f to produce the current step
              ; and passes *that* step the continuation c
              (mv (fn [v]
                    ((f v) c))))))
