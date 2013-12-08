(ns momo.state-test
  (:use clojure.test)
  (:require [momo.core :as m
             :refer [<- return bind zero plus chain lift join]]
            [momo.state :as state
             :refer [State]]))

(deftest state-test
         (m/with State
           (let [pop (fn [[x & xs]]
                       [x xs])

                 push (fn [x]
                        (fn [xs]
                          [nil (conj xs x)]))]

             (is (= [nil [8 3 0 2 1 0]]
                    ((<- [a pop]
                         (if (= a 5)
                           (push 5)
                           (chain
                             (push 3)
                             (push 8))))
                       (list 9 0 2 1 0))))

             (is (= [[1 2 3] [1 2 3]]
                    ((chain
                       (push 3)
                       (push 2)
                       (push 1)
                       state/get)
                       (list))))

             (is (= [1]
                    (second
                      ((chain
                         (push 3)
                         (state/set [])
                         (push 1))
                         (list)))))

             (is (= 3
                    (second
                      ((chain
                         (state/update inc)
                         (state/update inc)
                         (state/update inc))
                         0)))))))


