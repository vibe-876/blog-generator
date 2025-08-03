(ns blog-generator.parser
  (:gen-class))


(defn add-asts
  "Appends the old partial AST to the new one."
  ([ast]
   ast)
  ([ast other-ast]
   (apply vector
          (concat (drop-last ast) other-ast)))

  ([ast other-ast & asts]
   (loop [summed (add-asts ast other-ast)
          next-ast (first asts)
          remaining (rest asts)]
     (if (empty? remaining)
       (add-asts summed next-ast)
       (recur (add-asts summed next-ast)
              (first remaining)
              (rest remaining))))))

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

(defn rtake-until
  "Calls take-until, and then rest on the result."
  [p? coll]
  (->> coll
       (take-until p?)
       (rest)))

(defn rdrop-until
  "Calls drop-until, and then rest on the result."
  [p? coll]
  (->> coll
       (drop-until p?)
       (rest)))


(defn parse-link
  "Parses a camarkup link. Links should be in the
  form {text@uri}, where text is the text to be
  displayed."
  [camarkup-string]
  (let [at-middle? #(= \@ %)
        at-end? #(= \} %)
        
        text (rtake-until at-middle? camarkup-string)
        rhs (drop-until at-middle? camarkup-string)
        uri (rtake-until at-end? rhs)
        remaining (rdrop-until at-end? rhs)]
    
    [{:link {:text (apply str text)
             :uri (apply str uri)}}
     (apply str remaining)]))

(defn lex-chunk-start
  "Lexer for a single chunk."
  [unknown-chunk]
  (cond ;(= identifier \{) (parse-link unknown-chunk)
        :else {:error unknown-chunk}))

(defn lex-chunk-end
  [unknown-chunk]
  0)

(defn next-chunk
  "Get the next chunk of the camarkup string.
  Returned is a pair inside a vector, with the first element being
  the parsed lexeme, and the second being the remaining unparsed
  string."
  [partial-ast]
  (let [starts [\{ \#]
        ends (concat [\ ] starts)
        fl-char (first (last partial-ast))]
    
    (if (some #{fl-char}
              starts)
      (lex-chunk-start partial-ast)
      (lex-chunk-end partial-ast))))

(defn trans-camarkup-ir
  "Translate a camarkup string into the internal
  representation used."
  [camarkup-string]
  (loop [ast [camarkup-string]]
    (println ast)
    (if (= (last ast) "")
      (drop-last ast)
      (recur (next-chunk ast)))))
