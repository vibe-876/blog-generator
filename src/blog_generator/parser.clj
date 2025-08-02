(ns blog-generator.parser
  (:gen-class)
  (:require [clojure.string :as string]))


(defn add-asts
  "Appends the old partial AST to the new one."
  ([ast]
   ast)
  ([ast other-ast]
   (apply vector
          (concat (drop-last ast) other-ast)))
  ([ast other-ast & extra-asts]
   (reduce add-asts (apply vector ast other-ast extra-asts))))

(defn take-until
  "Takes some boolean function p?, and and take elements
  from a sequence until p? evaluates to false."
  [p? coll]
  (loop [coll-seq coll
         current (first coll)
         carry []]
    (cond (= current nil) carry
          (p? current) carry
          :else (recur (rest coll-seq)
                       (second coll-seq)
                       (conj carry current)))))
(defn drop-until
  "Takes some boolean function p?, and drops elements
  from a sequence until p? evaluates to false."
  [p? coll]
  (loop [coll-seq coll
         current (first coll)]
    (cond (= current nil) []
          (p? current) coll-seq
          :else  (recur (rest coll-seq)
                        (second coll-seq)))))

(defn parse-link
  "Parses a camarkup link. Links should be in the
  form {text@uri}, where text is the text to be
  displayed."
  [camarkup-string]
  (let [raw-link (take-until #(= \@ %) camarkup-string)]
    (loop [camarkup (second raw-link)
           current-point (first camarkup)
           uri ""]
      
      (cond (empty? camarkup) [{:error "End of string found while parsing a link!"} ""]
            (= \} current-point) [{:link [{:text (->> (first raw-link)
                                                      (rest)
                                                      (apply str))}
                                          {:uri uri}]}
                                  (apply str (rest camarkup))]

            :else (recur (rest camarkup)
                         (second camarkup)
                         (str uri current-point))))))

;; {:div-name code [
(defn parse-div
  "Parses a div."
  []
  0)

(defn next-chunk
  "Get the next chunk of the camarkup string.
  Returned is a pair inside a vector, with the first element being
  the parsed lexeme, and the second being the remaining unparsed
  string."
  [partial-ast]
  (let [camarkup-string (last partial-ast)
        previous-p-ast (apply vector (drop-last partial-ast))]
    
    (loop [camarkup camarkup-string
           current-character (first camarkup-string)
           carry-chunk ""]

      (cond (empty? camarkup) [previous-p-ast ""]
            (= \  current-character) (add-asts previous-p-ast {:word carry-chunk}
                                               (apply str (rest camarkup)))
            ;; (= \# current-character) (add-asts previous-p-ast [{:word carry-chunk}] (parse-div))>
            (= \{ current-character) (add-asts previous-p-ast (parse-link camarkup))

            :else (recur (rest camarkup)
                         (second camarkup)
                         (str carry-chunk current-character))))))

(defn trans-camarkup-ir
  "Translate a camarkup string into the internal
  representation used."
  [camarkup-string]
  (loop [ast [camarkup-string]]
    (println ast)
    (if (= (last ast) "")
      (drop-last ast)
      (recur (next-chunk ast)))))
