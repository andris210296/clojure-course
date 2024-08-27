(ns clojure-course.course5.course5
  (:use clojure.pprint)
  (:require [schema.core :as s]))


(defn adiciona-paciente [pacientes paciente]
  (if-let [id (:id paciente)]
    (assoc pacientes id paciente)
    (throw (ex-info "Paciente não possui id" {:paciente paciente}))))

(defn imprime-relatorio-de-paciente [visitas, paciente]
  (println "Visitas do paciente" paciente "são" (get visitas paciente)))

; { 15 [], 20 [], 25 []}
(defn adiciona-visita
  [visitas, paciente, novas-visitas]
  (if (contains? visitas paciente)
    (update visitas paciente concat novas-visitas)
    (assoc visitas paciente novas-visitas)))

(defn testa-uso-de-pacientes []
  (let [guilherme {:id 15, :nome "Guilherme"}
        daniela {:id 20, :nome "Daniela"}
        paulo {:id 25, :nome "Paulo"}

        ; uma variação com reduce, mais natural
        pacientes (reduce adiciona-paciente {} [guilherme, daniela, paulo])

        ; uma variação com shadowing
        visitas {}
        visitas (adiciona-visita visitas 15 ["01/01/2019"])
        visitas (adiciona-visita visitas 20 ["01/02/2019", "01/01/2020"])
        visitas (adiciona-visita visitas 15 ["01/03/2019"])]
    (pprint pacientes)
    (pprint visitas)
    (imprime-relatorio-de-paciente visitas daniela)
    (println (get visitas 20))))

(testa-uso-de-pacientes)


; Using schemas

(pprint (s/validate Long 15))
; (pprint (s/validate Long "15")) this will throw an error

; The validation must be set to true to work
(s/set-fn-validation! true)

; Creating my first validation
(s/defn teste-simples [x :- Long]
  (println x))

(teste-simples 15)
; (teste-simples "guilherme") this will throw an error

(s/defn imprime-relatorio-de-paciente
  [visitas, paciente :- Long]
  (println "Visitas do paciente" paciente "são" (get visitas paciente)))

; In this case we have an error in execution time that tells that the given parameter is not the expected one
; (testa-uso-de-pacientes)

(s/defn novo-paciente
  [id :- Long, nome :- s/Str]
  {:id id, :nome nome})

(pprint (novo-paciente 15 "Guilherme"))
; (pprint (novo-paciente "Guilherme" 15)) this will throw an error


; Using defrecord and schema
; This will work, but in case you add a wrong type, it will allow it, because you might want to expand your schema
; if you don't want, it's better to use a different approach
; forward compatibility
; (s/defrecord Paciente [id :- Long, nome :- s/Str])
; (pprint (Paciente. 15 "Guilherme"))

; Not forward compatibility
(def Paciente
  "Schema de um paciente"
  {:id s/Num, :nome s/Str})

(pprint (s/explain Paciente))
(pprint (s/validate Paciente {:id 15, :nome "guilherme"}))
; In this case the validations work
;(pprint (s/validate Paciente {:id 15, :name "guilherme"}))
;(pprint (s/validate Paciente {:id 15, :nome "guilherme", :plano [:raio-x]}))


; Returning with validation
; this will throw an error
; (s/defn novo-paciente :- Paciente
; [id :- s/Num, nome :- s/Str]
; {:id id, :nome nome, :plano []})

(s/defn novo-paciente :- Paciente
  [id :- s/Num, nome :- s/Str]
  {:id id, :nome nome})

(pprint (novo-paciente 15 "Guilherme"))



(defn estritamente-positivo? [x]
  (> x 0))

(def EstritamentePositivo (s/pred estritamente-positivo?))
(pprint (s/validate EstritamentePositivo 15))


(def Paciente
  "Schema de um paciente"
  {:id (s/constrained s/Int estritamente-positivo?), :nome s/Str})
; can be changed to pos-int?

(pprint (s/validate Paciente {:id 15, :nome "Guilherme"}))
; (pprint (s/validate Paciente {:id -15, :nome "Guilherme"}))
; (pprint (s/validate Paciente {:id 0, :nome "Guilherme"}))


; Schemas more complex
(defn maior-ou-igual-a-zero? [x]
  (>= x 0))
(def ValorFinanceiro (s/constrained s/Num maior-ou-igual-a-zero?))

(def Pedido
  {:paciente Paciente
   :valor ValorFinanceiro
   :procedimento s/Keyword})

(s/defn novo-pedido :- Pedido
  [paciente :- Paciente, valor :- ValorFinanceiro, procedimento :- s/Keyword]
  {:paciente paciente, :valor valor :procedimento procedimento})

(pprint (novo-pedido (novo-paciente 15, "Guilherme"), 15.53, :raio-x))


; Using other structures with schema
(def Numeros [s/Num])
(pprint (s/validate Numeros [15]))
(pprint (s/validate Numeros [15, 13]))
(pprint (s/validate Numeros [15, 13, 132, 321, 23.2]))
(pprint (s/validate Numeros [0]))
; (pprint (s/validate Numeros [nil]))
(pprint (s/validate Numeros []))
(pprint (s/validate Numeros nil))

(pprint (s/validate [s/Num] nil))
;(pprint (s/validate s/Num nil))


(def Plano [s/Keyword])
(pprint (s/validate Plano [:raio-x]))






(def PosInt (s/pred pos-int? 'inteiro-positivo))

(def Plano [s/Keyword])

(def Paciente
  {:id PosInt, :nome s/Str, :plano Plano})

; The optional key is to allow the key to be nil
(def Paciente
  {:id PosInt, :nome s/Str, :plano Plano, (s/optional-key :nascimento) s/Str})

(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano [:raio-x, :ultrassom]}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano [:raio-x]}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano []}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano nil}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano [], :nascimento "18/09/1981"}))
