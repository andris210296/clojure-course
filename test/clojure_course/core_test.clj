(ns clojure-course.core-test
  (:require [clojure.test :refer :all]
            [clojure-course.core :refer :all]))



(deftest nao-sei-o-nome-ainda-test
  (testing "Given a number under 100 reais, the delivery tax should be 15 reais"
    (is (= 15 (taxa-de-entrega 1)))
    (is (= 15 (taxa-de-entrega 100))))
  (testing "Given a number between 100 and 200 reais, the delivery tax should be 5 reais"
    (is (= 5 (taxa-de-entrega 100.01)))
    (is (= 5 (taxa-de-entrega 200))))
  (testing "Given a number above 200 reais, the delivery tax should be 0 reais"
    (is (= 0 (taxa-de-entrega 200.01)))
    (is (= 0 (taxa-de-entrega 1000)))))


