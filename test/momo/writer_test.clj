(ns momo.writer-test
  (:use clojure.test)
  (:require [momo.core :as m
             :refer [<- return bind]]
            [momo.writer :as writer
             :refer [Writer tell]]))

(deftest writer-test
         (m/with (Writer :string)
                 (is (= "message"
                        (second (tell "message"))))

               (is (= [8 "was squared.was halved."]
                      (<- [x (return 4)
                           x [(* x x) "was squared."]
                           x [(/ x 2) "was halved."]]
                          (return x))))))
