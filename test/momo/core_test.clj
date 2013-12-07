(ns momo.core-test
  (:use clojure.test)
  (:require [momo.core :as m
             :refer [<- return]]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))

(deftest seq-test
         (is (= [[1 :a] [1 :b] [2 :a] [2 :b]]
                (m/with seq
                      (<- [x [1 2]
                           y [:a :b]]
                          (return [x y]))))))
