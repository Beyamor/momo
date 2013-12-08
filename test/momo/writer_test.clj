(ns momo.writer-test
  (:use clojure.test)
  (:require [momo.core :as m
             :refer [<- return bind]]
            [momo.writer :as writer
             :refer [Writer tell]]))

(deftest writer-test
         (m/with (Writer "")
                 (is (= "message"
                        (second (tell "message"))))))
