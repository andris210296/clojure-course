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


(deftest imposto-retido-fonte-test
  (testing "Given a value over 1000 reais then shouldn't have tax discount"
    (is (= 0 (imposto-retido-fonte 1)))
    (is (= 0 (imposto-retido-fonte 999.99))))
  (testing "Given a value equal or over 1000 reais then should have 10% tax discount"
    (is (= 100.00 (imposto-retido-fonte 1000)))
    (is (= 1000.00 (imposto-retido-fonte 10000)))))


(deftest imc-check-test
  (testing "Uma pessoa muito magra deve ter um IMC baixo"
    (is (= :baixo-peso (imc-check 1 2))))
  (testing "Uma pessoa com peso ideal deve ter IMC ideal"
    (is (= :normal (imc-check 70 1.70))))
  (testing "Uma pessoa com sobrepeso deve ter IMC acima do normal"
    (is (= :sobrepeso (imc-check 80 1.70)))))


