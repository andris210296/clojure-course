(ns clojure-course.course6.login-test
  (:use clojure.pprint)
  (:require [clojure.test :refer :all]
            [clojure-course.course6.logic :refer :all]
            [clojure-course.course6.model :as h.model]
            [schema.core :as s]))

(s/set-fn-validation! true)

(deftest cabe-na-fila?-test

  ; boundary tests
  ; exatamente na borda e one off. -1, +1, <=, >=, =

  ;borda do zero
  (testing "Que cabe na fila"
    (is (cabe-na-fila? {:espera []}, :espera)))

  ;borda do limite
  (testing "Que não cabe na fila quando a fila está cheia"
    (is (not (cabe-na-fila? {:espera [1 2 3 4 5]}, :espera))))

  ;one off da borda do limite para cima
  (testing "Que não cabe na fila quando tem mais do que uma fila cheia"
    (is (not (cabe-na-fila? {:espera [1 2 3 4 5 6]}, :espera))))

  ;dentro das bordas
  (testing "Que cabe na fila quando tem pouco menos do que uma fila cheia"
    (is (cabe-na-fila? {:espera [1 2 3 4]}, :espera))
    (is (cabe-na-fila? {:espera [1 2]}, :espera)))

  (testing "Que não cabe quando o departamento não existe"
    (is (not (cabe-na-fila? {:espera [1 2 3 4]}, :raio-x))))
  )


(deftest chega-em-test

  (let [hospital-cheio {:espera [1, 35, 42, 64, 21]}]

  (testing "aceita pessoas enquanto cabem pessoas na fila"
    (is (= {:espera [1, 2, 3, 4, 5]}
           (chega-em {:espera [1, 2, 3, 4]}, :espera, 5)))
    (is (= {:espera [1, 2, 5]}
           (chega-em {:espera [1, 2]}, :espera, 5))))

 ;; (testing "não aceita quando não cabe na fila"
   ;; (is (thrown? IllegalStateException (chega-em hospital-cheio, :espera 76))))

; One way of testing excpetions
  ;(is ((try
   ;      (chega-em {:espera [1, 35, 42, 64, 21]}, :espera 76)
    ;     false
     ;    (catch clojure.lang.ExceptionInfo e
      ;     (= :impossivel-colocar-pessoa-na-fila (:tipo ex-data e))))))

  ))

(deftest transfere-test

  (testing "aceita pessoas se cabe"
    (let [hospital-original {:espera (conj h.model/fila-vazia "5"), :raio-x h.model/fila-vazia}]
      (is (= { :espera []
              :raio-x ["5"]}
             (transfere hospital-original :espera :raio-x))))

    (let [hospital-original {:espera (conj h.model/fila-vazia "51", "5"), :raio-x (conj h.model/fila-vazia "13")}]
      (is (= { :espera ["5"]
              :raio-x ["13", "51"]}
             (transfere hospital-original :espera :raio-x)))))

  (testing "recusa pessoas se não cabe"
    (let [hospital-cheio {:espera (conj h.model/fila-vazia "5"), :raio-x (conj h.model/fila-vazia "1", "2", "53", "42", "13")}]
      (is (thrown? clojure.lang.ExceptionInfo
                   (transfere hospital-cheio :espera :raio-x)))))

  (testing "Não pode invocar transferência sem hospital"
    (is (thrown? clojure.lang.ExceptionInfo (transfere nil :espera :raio-x))))


  (testing "condições obrigatórias"
    (let [hospital {:espera (conj h.model/fila-vazia "5"), :raio-x (conj h.model/fila-vazia "1", "54", "43", "12")}]
      (is (thrown? AssertionError
          (transfere hospital :nao-existe :raio-x)))
      (is (thrown? AssertionError
               (transfere hospital :raio-x :nao-existe)))))
  )