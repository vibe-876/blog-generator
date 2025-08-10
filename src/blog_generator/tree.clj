(ns blog-generator.tree
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


(defn ensure-head-exists
  "If no head has been attached to the
  document, throw some default one into
  the ast."
  [ast]
  (if (empty? (vals ast))
    '({:title "Blog Entry"})
    ast))

(defn fix-header
  "If a node is a header with an invalid
  level, then set it to a valid level."
  [ast-node]
  (let [header (first (:header ast-node))
        text (:text header)
        level (:level header)]

    (if (and header
             (> level 6))
      {:header (list {:level 6
                      :text text})}
      ast-node)))

(defn take-paragraph
  [ast]
  (loop [remaining ast
         node (first remaining)
         carry '()]
    (if (or (:newline node)
            (empty? remaining))
      {:car {:paragraph carry}
       :rem (rest remaining)}
      (recur (rest remaining)
             (second remaining)
             (concat carry (list node))))))

(defn build-paragraphs
  "Builds paragraphs out of word tags."
  [ast]
  (loop [remaining ast
         carry '()]
    (if (empty? remaining)
      carry
      (let [built (take-paragraph remaining)]
        (recur (:rem built)
               (concat carry (list (:car built))))))))


(defn organise-ast
  "Organise the AST."
  [ast]
  {:head (->> ast
              (map :head)
              (filter #(not= nil %))
              (ensure-head-exists))
   
   :body  (->> ast
               (filter #(= (:head %) nil))
               (map fix-header)
               (build-paragraphs))})
