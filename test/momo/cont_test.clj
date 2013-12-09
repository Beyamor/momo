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
                          ((<- [x (return 10)
                                x (fn-a x)
                                x (fn-b x)
                                x (fn-c x)]
                               (return x))
                             identity))))))
