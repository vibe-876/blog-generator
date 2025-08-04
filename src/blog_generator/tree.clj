(ns blog-generator.tree
  (:gen-class))


(defn organise-tree
  "Organise the AST."
  [ast]
  [{:head (->> ast
               (map :head)
               (filter #(not= nil %)))
    
    :body (->> ast
               (filter #(= (:head %) nil)))}])
