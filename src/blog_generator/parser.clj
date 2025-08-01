(ns blog-generator.parser
  (:gen-class)
  (:require [clojure.string :as string]))


(defn parse-link
  "Parses a camarkup link. Links should be in the
  form {text@uri}, where text is the text to be
  displayed."
  [camarkup-string]
  (let [raw-link (string/split camarkup-string #"@")]
    (loop [camarkup (second raw-link)
           current-point (first camarkup)
           uri ""]
      
      (cond (empty? camarkup) [{:error "End of string found while parsing a link!"} ""]
            (= \} current-point) [{:link [{:text (first raw-link)}
                                          {:uri uri}]}
                                  (apply str (rest camarkup))]

            :else (recur (rest camarkup)
                         (second camarkup)
                         (str uri current-point))))))

(defn next-chunk
  "Get the next chunk of the camarkup string.
  Returned is a pair inside a vector, with the first element being
  the parsed lexeme, and the second being the remaining unparsed
  string.

  Once the input is empty, the left hand side of the vector will
  be written as nil."
  [partial-ast]
  (let [camarkup-string (last partial-ast)
        previous-p-ast (apply vector (drop-last partial-ast))]
    
    (loop [camarkup camarkup-string
           current-character (first camarkup-string)
           carry-chunk ""]

      (cond (empty? camarkup) [previous-p-ast nil]
            (= \  current-character) (apply vector
                                            (concat previous-p-ast [{:word carry-chunk}
                                                                    (apply str (rest camarkup))]))
            (= \{ current-character) (parse-link camarkup)
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
      ast
      (recur (next-chunk ast)))))
