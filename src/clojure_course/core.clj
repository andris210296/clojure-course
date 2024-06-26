(ns clojure-course.core)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

; Printing the result of the function
; (println "Fulano")

; Counting the number of elements in the list
(count [1 2 3 4 5 6 7 8 9 10])

; Adding a new element to the list
(conj [1 2 3 4 5 6 7 8 9 10] "Hello")

; Removing the last element of the list
(pop [1 2 3 4 5 6 7 8 9 10 "Hello"])

; Showing the last element of the list
(peek [1 2 3 4 5 6 7 8 9 10 "Hello"])

; Creating my first function
; define a function, create a name, add parameters and the body of the function
(defn imprimir-ola [] (println "Olá, mundo!"))
; (imprimir-ola)

; Creating a function with parameters
(defn parabenizar [nome sobrenome] (println "Parabéns, " nome sobrenome"!"))


; Function that calculates the fine value
; let can define a variable or function that is only available in the scope of the function
(defn valor-multa
  [valor]
  (let [percentual-multa 0.1]
    (* valor percentual-multa)))


; Using something similar to Java docs
(defn valor-total
  "Calcula o valor total da multa"
  [valor]
  (let [percentual-multa 0.1
        valor-multa (* valor percentual-multa)]
    (+ valor valor-multa)))


; Predicates - functions that return true or false
(even? 3)
(odd? 3)
(neg? 3)

(defn isento-imposto?
  [valor]
  (<= valor 1000))


; Using IFs
(defn valor-imposto
  [valor]
  (if (isento-imposto? valor)
    0
    (* valor 0.1)))

; Using IF and Else
(defn imposto-retido
  [salario]
  (let [imposto-primeira-faixa 0
        imposto-segunda-faixa 0.1
        imposto-terceira-faixa 0.2]
    (if (<= salario 1000)
      imposto-primeira-faixa
      (if (< salario 2000)
        (* salario imposto-segunda-faixa)
        (* salario imposto-terceira-faixa)))))

; Using when
(defn imprimir-mensagem-boas-vindas
  [idade]
  (when (>= idade 18)
    (println "Esta mensagem será exibida apenas para usuários maiores de 18 anos")))

(defn taxa-de-entrega
  [valor-da-compra]
  (if (<= valor-da-compra 100)
    15
    (if (<= valor-da-compra 200)
      5
      0)))


; Function that returns the double of a number
(defn two
  []
  2)

; Function that returns the double of a number and adds to a string
(defn get-double
  [number name]
  (let [double-value (* number 2)]
    (str
      "Olá, "
      name
      ", o dobro de "
      number
      " é " double-value)))


(def lista [1 2 3 4 5 6 7 8 9 10])

; Function to filter the even numbers of the lista
(filter (fn [number] (= (mod number 2) 0)) lista)

; Function to double the numbers of the lista
(map #(* 2 %) lista)

; Function to sum all the numbers of the lista
(reduce (fn [number result] (+ number result)) 0 lista)
