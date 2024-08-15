(ns clojure-course.course2.loja
  (:require [clojure-course.course2.db :as l.db]))


(println (l.db/todos-os-pedidos))

(println (group-by :usuario (l.db/todos-os-pedidos)))

(defn my-groupby-function
  [elemento]
  (println "elemento" elemento)
  (:usuario eduction))

(println (group-by my-groupby-function (l.db/todos-os-pedidos)))

; Example on counting how many orders each user has
(println (map count (vals (group-by :usuario (l.db/todos-os-pedidos)))))


; Example on counting how many orders each user has
(defn conta-total-por-usuario
  [[usuario pedidos]]
  {:usuario-id usuario
   :total-de-pedidos (count pedidos)})

(->> (l.db/todos-os-pedidos)
     (group-by :usuario)
     (map conta-total-por-usuario)
     println)


; Example on counting how many orders and how much each user has spent

(defn total-do-item
  [[item-id detalhes]]
  (* (get detalhes :quantidade 0)  (get detalhes :preco-unitario 0)))

(defn total-do-pedido
  [pedido]
  (->> pedido
       (map total-do-item)
       (reduce +)))

(defn total-dos-pedidos
  [pedidos]
  (->> pedidos
       (map :itens)
       (map total-do-pedido)
       (reduce +)))

(defn quantia-de-pedidos-e-gasto-total-por-usuario
  [[usuario pedidos]]
  {:usuario-id usuario
   :total-de-pedidos (count pedidos)
   :preco-total (total-dos-pedidos pedidos)})


(defn resumo-por-usuario
  [pedidos]
  (->> pedidos
       (group-by :usuario)
       (map quantia-de-pedidos-e-gasto-total-por-usuario)))

(println (resumo-por-usuario (l.db/todos-os-pedidos)))


; Example on sorting the results
(let  [pedidos (l.db/todos-os-pedidos)
       resumo (resumo-por-usuario pedidos)]
  (println "RESUMO" resumo)
  (println "Ordenado" (sort-by :preco-total resumo))
  (println "Ordenado ao contrario" (reverse (sort-by :preco-total resumo)))
  (println "Ordenado por id" (sort-by :usuario-id resumo))

  ; update-in, assoc-in, dissoc-in
  (println (get-in pedidos [0 :itens :mochila :quantidade]))
  )

(defn resumo-ordenado-por-usuario [pedidos]
  (->> pedidos
       (resumo-por-usuario)
       (sort-by :preco-total)
       reverse))

; Example of other functions
(let [pedidos (l.db/todos-os-pedidos)
      resumo (resumo-ordenado-por-usuario pedidos)]
  (println "Resumo" resumo)
  (println "Primeiro" (first resumo))
  (println "Segundo" (second resumo))
  (println "Resto" (rest resumo))
  (println "Total" (count resumo))
  (println "Class" (class resumo))
  (println "nth 1" (nth resumo 1))
  (println "get 1" (get resumo 1)) ; It's not going to work because resumo is a map and not a list
  )


; Example of take
(defn top-2 [pedidos]
  (take 2 pedidos))

(let [pedidos (l.db/todos-os-pedidos)
      resumo (resumo-ordenado-por-usuario pedidos)]
  (println "Resumo" resumo)
  (println "Top-2" (top-2 resumo)))


; Example of filter and some
(let [pedidos (l.db/todos-os-pedidos)
      resumo (resumo-ordenado-por-usuario pedidos)]
  (println "> 500 filter" (filter #(> (:preco-total %) 500) resumo))
  (println "> 500 some" (some #(> (:preco-total %) 500) resumo)))




(defn gastou-bastante?
  [info-do-usuario]
  (> (:preco-total info-do-usuario) 500))

;;Example of keep

(let  [pedidos (l.db/todos-os-pedidos)
       resumo (resumo-por-usuario pedidos)]
  (println "keep" (keep gastou-bastante? resumo))
  (println "filter" (filter gastou-bastante? resumo)))







