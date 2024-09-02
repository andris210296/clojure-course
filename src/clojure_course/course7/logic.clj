(ns clojure-course.course7.logic
  (:require [clojure-course.course7.model :as h.model]
            [schema.core :as s]))


(defn cabe-na-fila?
  [hospital departamento]
  (some-> hospital
          departamento
          count
          (< 5)))

(defn chega-em
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    (throw (ex-info "não cabe ninguém neste departamento"
                    {:type :fila-cheia
                     :paciente pessoa}))))

(s/defn atende :- h.model/Hospital
        [hospital :- h.model/Hospital, departamento :- s/Keyword]
        (update hospital departamento pop))

; Example of the using maybe
(s/defn proxima :- (s/maybe h.model/PacienteID)
  "Retorna o próximo paciente da fila"
  [hospital :- h.model/Hospital, departamento :- s/Keyword]
  (-> hospital
      departamento
      peek))

(defn mesmo-tamanho? [hospital outro-hospital de para]
  (= (+ (count (get outro-hospital de)) (count (get outro-hospital para)))
     (+ (count (get hospital de)) (count (get hospital para)))))

; Use of pre-conditions and post conditions
(s/defn transfere :- h.model/Hospital
        "Transfere o próximo paciente da fila de para a fila para"
        [hospital :- h.model/Hospital, de :- s/Keyword, para :- s/Keyword]
        {
         :pre [(contains? hospital de) (contains? hospital para)]
         :post [(mesmo-tamanho? hospital % de para)]
         }
        (if-let [pessoa (proxima hospital de)]
          (-> hospital
              (atende de)
              (chega-em para pessoa))
          hospital))

(defn total-de-pacientes [hospital]
  (reduce + (map count (vals hospital))))
