(ns clojure-course.course3.hospital
  (:use [clojure pprint])
  (:require [clojure-course.course3.model :as h.model]
            [clojure-course.course3.logic :as h.logic]))

(let [hospital-do-gui (h.model/novo-hospital)]
  (pprint hospital-do-gui))

;(pprint h.model/fila-vazia)



(defn simula-um-dia []
  ; root binding
  (def hospital (h.model/novo-hospital))
  (def hospital (h.logic/chega-em hospital :espera "111"))
  (def hospital (h.logic/chega-em hospital :espera "222"))
  (def hospital (h.logic/chega-em hospital :espera "333"))
  (pprint hospital)

  (def hospital (h.logic/chega-em hospital :laboratorio1 "444"))
  (def hospital (h.logic/chega-em hospital :laboratorio3 "555"))
  (pprint hospital)

  (def hospital (h.logic/atende hospital :laboratorio1))
  (def hospital (h.logic/atende hospital :espera))
  (pprint hospital)

  (def hospital (h.model/novo-hospital))
  (def hospital (h.logic/chega-em hospital :espera "666"))
  (def hospital (h.logic/chega-em hospital :espera "777"))
  (def hospital (h.logic/chega-em hospital :espera "888"))
  (def hospital (h.logic/chega-em hospital :espera "999"))
  (pprint hospital)
  )


;(simula-um-dia)



; Example on using atom and swap!
(defn testa-atom []
  (let [hospital-novo (atom { :espera h.model/fila-vazia})]
    (println hospital-novo)
    (pprint @hospital-novo)
    (pprint (deref hospital-novo)) ; remove the @
    (pprint @hospital-novo)

    ; swap! is a function that takes an atom, a function and arguments and changes
    ; the ! makes it clear that the function has side effects
    (swap! hospital-novo assoc :laboratorio1 h.model/fila-vazia)

    (swap! hospital-novo update :laboratorio1 conj "111")
    ))


; Swap can be used with any function
; It's important to use swap with functions where I know that can be called multiple times, use it with pure functions
; This is related to optimistic/pessimistic locking
; It's also related to @Transactional in Java
; Busy retry
(defn chega-em-malvado! [hospital pessoa]
  (swap! hospital h.logic/chega-em-pausado hospital :espera pessoa)
  (println "apos inserir" pessoa))


; Example on using threads
(defn simula-um-dia-em-paralelo
  []
  (let [hospital (atom (h.model/novo-hospital))]
    (.start (Thread. (fn [] (chega-em-malvado! hospital "111"))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital "222"))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital "333"))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital "444"))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital "555"))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital "666"))))
    (.start (Thread. (fn [] (Thread/sleep 4000)
                       (pprint hospital))))))

;(simula-um-dia-em-paralelo)

; Same code but using mapv
; It's possible to use partial, doseq, dotimes
(defn simula-um-dia-em-paralelo-mapv
  []
  (let [hospital (atom (h.model/novo-hospital))
        pessoas ["111", "222", "333", "444", "555", "666"]]

    (map #(.start (Thread. (fn [] (chega-em-malvado! hospital %)))) pessoas)

    (.start (Thread. (fn [] (Thread/sleep 8000)
                       (pprint hospital))))))


;(simula-um-dia-em-paralelo-mapv)










(defn cabe-na-fila? [fila]
  (-> fila
      count
      (< 5)))


; Example on using refs
(defn chega-em
  [fila pessoa]
  (if (cabe-na-fila? fila)
    (conj fila pessoa)
    (throw (ex-info "Fila já está cheia" {:tentando-adicionar pessoa}))
  ))

(defn chega-em!
  "troca de referencia via ref-set"
  [hospital pessoa]
  (let [fila (get hospital :espera)]
    (ref-set fila (chega-em @fila pessoa))))

(defn chega-em!
  "troca de referencia via alter"
  [hospital pessoa]
  (let [fila (get hospital :espera)]
    (alter fila chega-em pessoa)))

(defn simula-um-dia-ref []
  (let [hospital {:espera       (ref h.model/fila-vazia)
                  :laboratorio1 (ref h.model/fila-vazia)
                  :laboratorio2 (ref h.model/fila-vazia)
                  :laboratorio3 (ref h.model/fila-vazia)}]
    (dosync
      (chega-em! hospital "guilherme")
      (chega-em! hospital "maria")
      (chega-em! hospital "lucia")
      (chega-em! hospital "daniela")
      (chega-em! hospital "ana")
      (chega-em! hospital "paulo")
      (pprint hospital))))

(simula-um-dia-ref)

; Example of using future
(defn async-chega-em! [hospital pessoa]
  (future
    (Thread/sleep (rand 5000))
    (dosync
      (println "Tentando o codigo sincronizado" pessoa)
      (chega-em! hospital pessoa))))

(defn simula-um-dia-async []
  (let [hospital {:espera       (ref h.model/fila-vazia)
                  :laboratorio1 (ref h.model/fila-vazia)
                  :laboratorio2 (ref h.model/fila-vazia)
                  :laboratorio3 (ref h.model/fila-vazia)}]
    (def futures (mapv #(async-chega-em! hospital %) (range 10)))
    (future
      (dotimes [n 4]
        (Thread/sleep 2000)
        (pprint hospital)
        (pprint futures)))
    ))

(simula-um-dia-async)