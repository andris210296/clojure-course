(ns clojure-course.course6.logic
  (:require [clojure-course.course6.model :as h.model]
            [schema.core :as s]))


(defn cabe-na-fila?
  [hospital departamento]
  (when-let [fila (get hospital departamento)]
    (-> fila
        count
        (< 5))))

(defn chega-em
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    ; using ex-data
    ; (throw (ex-info "não cabe ninguém neste departamento" {:paciente pessoa, :tipo :impossivel-colocar-pessoas-na-fila}))
    ; (throw (IllegalStateException. "não cabe ninguém neste departamento." ))))
    (throw (ex-info "não cabe ninguém neste departamento" {:paciente pessoa}))))

(s/defn atende :- h.model/Hospital
  [hospital :- h.model/Hospital, departamento :- s/Keyword]
  (update hospital departamento pop))

(s/defn proxima :- h.model/PacienteID
  "Retorna o próximo paciente da fila"
  [hospital :- h.model/Hospital departamento :- s/Keyword]
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
  (let [pessoa (proxima hospital de)]
    (-> hospital
        (atende de)
        (chega-em para pessoa))))