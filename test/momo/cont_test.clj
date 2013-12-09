(ns momo.cont-test
  (:use clojure.test)
  (:require [momo.core :as m
             :refer [<- return bind]]
            [momo.cont :as cont
             :refer [Cont]]))

(deftest cont-test
         (m/with Cont
                 (let [fn-a (fn [x]
                              (fn [c]
                                (c (inc x))))

                       fn-b (fn [x]
                              (fn [c]
                                (c (* 2 x))))

                       fn-c (fn [x]
                              (fn [c]
                                (c (dec x))))]

                   (is (= 21
                          (((m/pipe
                             fn-a
                             fn-b
                             fn-c)
                              10)
                             identity))))))
