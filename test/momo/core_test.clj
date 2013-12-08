(ns momo.core-test
  (:use clojure.test)
  (:require [momo.core :as m
             :refer [<- return bind zero plus chain lift join
                     Seq Maybe Err State
                     get-state set-state update-state]]))

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
                            (return y))))

                 (is (= [] zero))

                 (is (= [1 2 3]
                        (plus [1 2 3] zero)))

                 (is (= [1 2 3]
                        (plus zero [1 2 3])))))

(deftest maybe-test
         (m/with Maybe
                 (is (= :foo
                        (return :foo)))

                 (is (= 2
                        (bind 1 inc)))

                 (is (= nil
                        (bind nil inc)))

                 (is (= 2
                        (<- [x 1
                             y (inc x)]
                            (return y))))

                 (is (= nil
                        (<- [x nil
                             y (inc x)]
                            (return y))))))

(deftest error-test
         (m/with Err
                 (is (= {:right 3}
                        (<- [x (return 1)
                             y (return (inc x))
                             z (return (inc y))]
                            (return z))))

                 (is (= {:left :err}
                        (<- [x (return 1)
                             y {:left :err}
                             z (return (inc y))]
                            (return z))))))

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
                       get-state)
                       (list))))

             (is (= [1]
                    (second
                      ((chain
                         (push 3)
                         (set-state [])
                         (push 1))
                         (list)))))

             (is (= 3
                    (second
                      ((chain
                         (update-state inc)
                         (update-state inc)
                         (update-state inc))
                         0)))))))

(deftest lifting-test
         (m/with Maybe
                 (is (= 24
                        (lift (partial * 3) 8)))))

(deftest joining-test
         (m/with Seq
                 (is (= [1 2 3]
                        (join [[1] [2 3]])))))

(deftest filtering-test
         (m/with Maybe
                 (is (= [2 4 6 8]
                        (m/filter even? [2 4 6 8]))))

         (m/with Seq
                 (is (= [[1 2 3]]
                        (-> (fn [x]
                              (if (< x 4)
                                [true]
                                [false]))
                        (m/filter [9 1 5 2 10 3]))))))
