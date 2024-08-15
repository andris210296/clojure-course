(ns clojure-course.course2.db)


(def pedido1 {:usuario 15
              :itens {:mochila {:id :mochila :quantidade 2 :preco-unitario 80}
                      :camiseta {:id :camiseta :quantidade 3 :preco-unitario 40}
                      :tenis {:id :tenis :quantidade 2 }}})

(def pedido2 {:usuario 1
              :itens {:mochila {:id :mochila :quantidade 2 :preco-unitario 80}
                      :camiseta {:id :camiseta :quantidade 3 :preco-unitario 40}
                      :tenis {:id :tenis :quantidade 2 }}})

(def pedido3 {:usuario 15
              :itens {:mochila {:id :mochila :quantidade 2 :preco-unitario 80}
                      :camiseta {:id :camiseta :quantidade 3 :preco-unitario 40}
                      :tenis {:id :tenis :quantidade 2 }}})

(def pedido4 {:usuario 15
              :itens {:mochila {:id :mochila :quantidade 2 :preco-unitario 80}
                      :camiseta {:id :camiseta :quantidade 3 :preco-unitario 40}
                      :tenis {:id :tenis :quantidade 2 }}})

(def pedido5 {:usuario 10
              :itens {:mochila {:id :mochila :quantidade 2 :preco-unitario 80}
                      :camiseta {:id :camiseta :quantidade 3 :preco-unitario 40}
                      :tenis {:id :tenis :quantidade 2 }}})


(defn todos-os-pedidos []
  [pedido1, pedido2, pedido3, pedido4, pedido5])