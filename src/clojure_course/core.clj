(ns clojure-course.core
  (:require [clojure.string :refer [upper-case join]]))

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
(defn parabenizar [nome sobrenome] (println "Parabéns, " nome sobrenome "!"))


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

; Using and
(defn parametros-preenchidos
  [nome idade]
  (if (and nome idade)
    (println "Estão preenchidos")
    (println "Faltam preencher parâmetros")))

; Using get and a default return in case not found
(defn obtem-terceiro-nome
  [nomes]
  (get nomes 2 "Desconhecido"))

; Function that changes the first name
(defn altera-primeiro-nome
  [nomes novo-primeiro-nome]
  (assoc nomes 0 novo-primeiro-nome))

; Types
(type 10)
(type 10.0)
(type (/ 10 3))
(type (* 3 (/ 10 3)))
(type 10N)
(type 10M)
(type [])
(type println)

; Using defn- to create a private function
(defn- esta-na-faixa-isencao?
  [valor]
  (< valor 1000))

(defn imposto-retido-fonte
  [valor]
  (if (esta-na-faixa-isencao? valor)
    0
    (* valor 0.1)))

;;;;;;;;;
; This is an example of adding a function inside a function and calling this function
(defn consulta-taxa-padrao-fixa
  "Returns the default tax rate"
  []
  0.2)

(defn consulta-taxa-padrao-por-http
  "Imagine that we have several calls to a http enpoint"
  []
  0.1)

(defn imposto-retido-chamando-funcao
  "If salary under 1000 reais then no tax discount, if salary equal or over 1000 reais then 20% tax discount"
  [consulta-taxa-padrao salario]

  (if (< salario 1000)
    0
    (* salario (consulta-taxa-padrao))))

; Adding a function as a parameter
(imposto-retido-chamando-funcao consulta-taxa-padrao-fixa 10)
;;;;;;;;;

;;;;;;;;;
; Example of a function that returns a function
(defn escolhe-consulta-taxa-padrao
  [ambiente]
  (if (= ambiente :teste)
    consulta-taxa-padrao-fixa
    consulta-taxa-padrao-por-http))


(imposto-retido-chamando-funcao (escolhe-consulta-taxa-padrao :teste) 2000)
;;;;;;;;;

; Anonymous function that doubles a number
((fn [x] (* x 2)) 3)

; Same function using the #() syntax
#(* 2 %)
(#(* 2 %) 5)

; Same anonymous function with two parameters
(#(+ %1 %2) 1 2)

; Example using constantly (the 0.2 will always be returned)
((constantly 0.2) 1 2 5 5 666888)

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


; Vector with some numbers that represents the school classes
(def classes [5 5 6 7 8 6 5 5])

(defn esta-no-quinto-ano?
  [ano]
  (= ano 5))

; Function that returns the quantity of students in 5th grade
(defn quantidade-estudantes-no-quinto-ano
  [estudantes]
  (count (filter esta-no-quinto-ano? estudantes)))

; Same function but using the fn syntax
;(defn quantidade-estudantes-no-quinto-ano
; [estudantes]
; (count (filter (fn [grade] (= grade 5)) estudantes)))

; Same function but using the #() syntax
;(defn quantidade-estudantes-no-quinto-ano
; [estudantes]
; (count (filter #(= % 5) estudantes)))

; Function that sums the values in a list
(def ages [ 5 10 5])
(defn sum-ages
  [ages]
  (reduce + ages))

; Function that receives a list of names and returns the average size of the names
(def names ["Marcio" "João"])
(defn average-name-size
  [names]
  (/ (reduce + (map count names)) (count names)))

; Possible using strings as keys, but are not recommended
{"nome" "Marcio" "sobrenome" "Ferreira"}
; Better way
{:nome "Marcio" :sobrenome "Ferreira"}


(defn compras
  []
  {:tomate {:quantidade 2 :preco 5}
 :arroz {:quantidade 1 :preco 4}
 :feijao {:quantidade 2 :preco 10}})

; Returning a value from a map
(get (compras) :tomate)
((compras) :tomate)
(:tomate (compras))

; Getting the quantity of tomatoes
(:quantidade (:tomate (compras)))

; Returning a standard value in case the key is not found
(:batata (compras) {:quantidade 0 :preco 0})
(:batata (compras) "Não encontrado")

; Adding a new key value to the map
(conj (compras)  {:batata {:quantidade 2 :preco 8}})
(assoc (compras) :batata {:quantidade 2 :preco 8})

; Remove a key value from the map
(dissoc (compras) :tomate :arroz)

; Function that updates a key value
(assoc (compras) :tomate {:quantidade 3 :preco 8})

; Increase a value
(update {:nome "Nome" :idade 10} :idade inc)

; Increase a value of an inner map
(update-in (compras) [:tomate :preco] inc)
; Duble the price of tomatoes
(update-in (compras) [:tomate :preco] * 2)




; Function that given a map with disciplines and the semester
; it should filter the disciplines of the given semester and beyond
; it should change the name to capital letters
; it should create a string concatenating the filtered disciplines

(defn disciplinas
  []
  [{:nome "Estrutura de dados" :semestre 2}
   {:nome "Algoritmos" :semestre 1}
   {:nome "Inteligência artificial" :semestre 3}])

(defn filter-disciplines
  [disciplinas semestre-atual]
  (join ", " (map upper-case (map :nome (filter #(>= (:semestre %) semestre-atual) disciplinas)))))


(defn filter-disciplines-refactoring
  [disciplinas semestre-atual]
  (->> disciplinas
       (filter #(>= (:semestre %) semestre-atual))
       (map :nome)
       (map upper-case)
       (join ", ")))
