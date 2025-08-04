(ns blog-generator.tree
  (:gen-class))


(defn fix-header
  "If a node is a header with an invalid
  level, then set it to a valid level."
  [ast-node]
  (let [header (:header ast-node)
        text (:text header)
        level (:level header)]

    (if (and header
             (> level 6))
      {:header {:level 6
                :text text}}
      ast-node)))


(defn organise-ast
  "Organise the AST."
  [ast]
  [{:head (->> ast
               (map :head)
               (filter #(not= nil %)))
    
    :body (->> ast
               (filter #(= (:head %) nil))
               (map fix-header))}])
