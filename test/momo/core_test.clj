(ns momo.core-test
  (:use clojure.test)
  (:require [momo.core :as m
             :refer [<- return Seq]]))

(deftest seq-test
         (m/with Seq
                 (is (= [1] (return 1)))))
