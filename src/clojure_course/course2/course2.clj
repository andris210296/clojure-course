(ns clojure-course.course2.course2)


(println (first ["daniela" "guilherme" "lucas" "maria" "joao"]))
(println (rest ["daniela" "guilherme" "lucas" "maria" "joao"]))
(println (next ["daniela" "guilherme" "lucas" "maria" "joao"]))
(println (seq ["daniela" "guilherme" "lucas" "maria" "joao"]))

(println "\n\n\n\n")

; Example of a recursive function
; It's important to use recur to avoid stack overflow
; Tail recursion is optimized by the JVM
(defn my-map
  [funcao sequencia]
  (let [primeiro (first sequencia)]
    (if (not(nil? primeiro))
      (do
        (funcao primeiro)
        (recur funcao (rest sequencia))))))

(my-map println ["daniela" "guilherme" "lucas" "maria" "joao"])
(println "\n\n\n\n")
(my-map println ["daniela" false "lucas" "maria" "joao"])
(println "\n\n\n\n")
(my-map println [])
(my-map println nil)

; (my-map println (range 100000000)) this will not brake anymore, due to recur


; Example of reduce
; Counting number of elements in a list
(defn my-count

  ([elements]
   (my-count 0 elements))

  ([total-until-now elements]
   (if (seq elements)
     (recur (inc total-until-now) (next elements))
     total-until-now)))

(println "\n\n\n\n")
(println (my-count ["daniela" "guilherme" "lucas" "maria" "joao"]))



; Example of loop
; Counting number of elements in a list
(defn my-count-with-loop
  [elements]
  (loop [total-until-now 0
         elements elements]
    (if (seq elements)
      (recur (inc total-until-now) (next elements))
      total-until-now)))

(println "\n\n\n\n")
(println (my-count-with-loop ["daniela" "guilherme" "lucas" "maria" "joao"]))
