(ns clojure-course.course4.course4
  (:use clojure.pprint))

(defn adiciona-paciente
  "Os pacientes são um mapa da seguinte forma { 15 {paciente 15}, 23 {paciente 23} }.
  O paciente { :id 15 ... }"
  [pacientes paciente]
  (let [id (:id paciente)]
    (assoc pacientes id paciente)))

(defn testa-uso-de-pacientes []
  (let [pacientes {}
        guilherme {:id 15 :nome "Guilherme" :nascimento "18/9/1981"}
        daniela {:id 20 :nome "Daniela" :nascimento "18/9/1982"}
        paulo { :nome "Paulo", :nascimento "18/10/1983"}
      ]
      (pprint (adiciona-paciente pacientes guilherme))
      (pprint (adiciona-paciente pacientes daniela))
      (pprint (adiciona-paciente pacientes paulo))
))

(testa-uso-de-pacientes)


; Example of defrecord
; It's a way to work with oriented object programming in Clojure
(defrecord Paciente [id nome nascimento])
(println (->Paciente 15 "Guilherme" "18/9/1981"))
(pprint (->Paciente 15 "Guilherme" "18/9/1981"))

;This is how to use the constructor
(pprint (Paciente. "Guilherme" 15 "18/9/1981"))
(pprint (Paciente. 15 "18/9/1981" "Guilherme"))
(pprint (map->Paciente {:id 15 :nome "Guilherme" :nascimento "18/9/1981"}))

; Allows to work as a map
(let [guilherme (->Paciente 15 "Guilherme" "18/9/1981")]
  (println (:id guilherme))
  (println (vals guilherme))
  (println (class guilherme))
  (println (record? guilherme))
  (println (.nome guilherme)))

; Allows to expand the record
(pprint (map->Paciente {:id 15 :nome "Guilherme" :nascimento "18/9/1981" :rg "12345678"}))

; Allows to remove a key
(pprint (map->Paciente {:nome "Guilherme" :nascimento "18/9/1981" :rg "12345678"}))

; This doesn't work
; (pprint (Paciente. "18/9/1981" "Guilherme"))

; But this works
(pprint (assoc (Paciente. nil "18/9/1981" "Guilherme") :id 38))

; It's possible to check equals
(pprint(= (Paciente. 15 "Guilherme" "18/9/1981") (Paciente. 15 "Guilherme" "18/9/1981")))
(pprint(= (Paciente. 15 "Guilherme" "18/9/1981") (Paciente. 16 "Guilherme" "18/9/1982")))






(defrecord PacienteParticular [id nome nascimento situacao])
(defrecord PacientePlanoDeSaude [id nome nascimento situacao plano])

; Example of defprotocol
; you can add behaviours to the records
(defprotocol Cobravel
  (deve-assinar-pre-autorizacao? [paciente procedimento valor]))

(extend-type PacienteParticular
  Cobravel
  (deve-assinar-pre-autorizacao? [paciente procedimento valor]
    (>= valor 50)))

(extend-type PacientePlanoDeSaude
  Cobravel
  (deve-assinar-pre-autorizacao? [paciente, procedimento, valor]
    (let [plano (:plano paciente)]
      (not (some #(= % procedimento) plano)))))

(let [particular (->PacienteParticular 15, "Guilherme", "18/9/1981", :normal)
      plano (->PacientePlanoDeSaude 15, "Guilherme", "18/9/1981", :normal [:raio-x, :ultrassom])]
    (pprint (deve-assinar-pre-autorizacao? particular, :raio-x, 500))
    (pprint (deve-assinar-pre-autorizacao? particular, :raio-x, 40))
    (pprint (deve-assinar-pre-autorizacao? plano, :raio-x, 999999)))



; Example of defmulti and defmethod
(defmulti deve-assinar-pre-autorizacao-multi? class)
(defmethod deve-assinar-pre-autorizacao-multi? PacienteParticular [paciente]
  (println "invocando PacienteParticular")
  true)
(defmethod deve-assinar-pre-autorizacao-multi? PacientePlanoDeSaude [paciente]
  (println "invocando PacientePlanoDeSaude")
  false)


(let [particular (->PacienteParticular 15, "Guilherme", "18/9/1981", :urgente)
      plano (->PacientePlanoDeSaude 15, "Guilherme", "18/9/1981", :urgente, [:raio-x, :ultrassom])]
  (pprint (deve-assinar-pre-autorizacao-multi? particular))
  (pprint (deve-assinar-pre-autorizacao-multi? plano)))






(defn tipo-de-autorizador [pedido]
(let [paciente (:paciente pedido)
      situacao (:situacao paciente)
      urgencia? (= :urgente situacao)]
  (if urgencia?
    :sempre-autorizado)
  (class paciente)))

(defmulti deve-assinar-pre-autorizacao-do-pedido? tipo-de-autorizador)
(defmethod deve-assinar-pre-autorizacao-do-pedido? :sempre-autorizado [pedido]
  false)
(defmethod deve-assinar-pre-autorizacao-do-pedido? PacienteParticular [pedido]
  (>= (:valor pedido 0) 50))
(defmethod deve-assinar-pre-autorizacao-do-pedido? PacientePlanoDeSaude [pedido]
  (not (some #(= % (:procedimento pedido)) (:plano (:paciente pedido)))))

(let [particular (->PacienteParticular 15, "Guilherme", "18/9/1981", :urgente)
      plano (->PacientePlanoDeSaude 15, "Guilherme", "18/9/1981", :urgente, [:raio-x, :ultrassom])]
  (pprint (deve-assinar-pre-autorizacao-do-pedido? {:paciente particular, :valor 1000, :procedimento :coleta-de-sangue}))
  (pprint (deve-assinar-pre-autorizacao-do-pedido? {:paciente plano, :valor 1000, :procedimento :coleta-de-sangue})))




