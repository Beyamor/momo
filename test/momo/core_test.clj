(ns momo.core-test
  (:use clojure.test)
  (:require [momo.core :as m
             :refer [<- return bind Seq]]))

(deftest seq-test
         (m/with Seq
                 (is (= [1]
                        (return 1)))

                 (is (= [1 2 2 3 3 4]
                        (bind [1 2 3]
                              (fn [v]
                                [v (inc v)]))))

                 (is (= [[:a 1] [:a 2] [:b 1] [:b 2]]
                        (<- [x [:a :b]
                             y [1 2]]
                            (return [x y]))))

                 (is (= [1 2 2 3 3 4]
                        (<- [x [1 2 3]
                             y [x (inc x)]]
                            (return y))))))
