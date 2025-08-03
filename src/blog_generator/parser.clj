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

(defn parse-header
  "Parses a header."
  [camarkup-string]
  (loop [remaining-chunk camarkup-string
         header-level 0
         current-char (first remaining-chunk)]
    
    (if (not= \# current-char)
      [{:header {:level header-level
                 :text (apply str (take-until #(= \newline %)
                                              remaining-chunk))}}
       (apply str (rdrop-until #(= \newline %) remaining-chunk))]
      
      (recur (rest remaining-chunk)
             (+ header-level 1)
             (second remaining-chunk)))))

(defn lex-chunk-start
  "Lexer for a single chunk."
  [unknown-chunk]
  (let [start-symbol (first (last unknown-chunk))
        remaining (last unknown-chunk)]
    (cond (= \{ start-symbol) (parse-link remaining)
          (= \# start-symbol) (parse-header remaining)
          :else [{:error unknown-chunk} ""])))

(defn lex-chunk-end
  [unknown-chunk ends]
  (loop [remaining-chunk (last unknown-chunk)
         carry-chunk ""
         current-char (first remaining-chunk)]

    (if (or (some #{current-char} ends)
            (not current-char))
      
      [{:word carry-chunk} (rest remaining-chunk)]
      (recur (rest remaining-chunk)
             (str carry-chunk current-char)
             (second remaining-chunk)))))

(defn next-chunk
  "Get the next chunk of the camarkup string.
  Returned is a pair inside a vector, with the first element being
  the parsed lexeme, and the second being the remaining unparsed
  string."
  [partial-ast]
  (let [starts [\{ \#]
        ends (concat [\ ] starts)
        fl-char (first (last partial-ast))]
    
    (add-asts partial-ast (if (some #{fl-char}
                                    starts)
                            (lex-chunk-start partial-ast)
                            (lex-chunk-end partial-ast ends)))))

(defn trans-camarkup-ir
  "Translate a camarkup string into the internal
  representation used."
  [camarkup-string]
  (loop [ast [camarkup-string]]
    (if (empty? (last ast))
      (drop-last ast)
      (recur (next-chunk ast)))))
