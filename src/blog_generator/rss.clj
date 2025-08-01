(ns blog-generator.rss
  (:gen-class))


;; TODO:
;; Decide on an I.R. for the RSS document.
;; (defn create-rss-document
;;   "Creates a new rss document.
;;   This is intended to only be called once per blog."
;;   [document-name]
;;   (generate-doc []))


;; (defn trans-ast-xml
;;   "Translate the abstract syntax tree into xml."
;;   [full-ast]
;;   (loop [ast full-ast]
;;     (let [current-index (first ast)]
;;       )))


(defn trans-ast-xml
  [& etc]
  0)

(defn traverse-ast
  "Traverses the AST."
  [ast]
  (loop [remaining-ast ast]
    (let [current-point (first remaining-ast)]
      (println current-point)
      (cond (list? current-point) (traverse-ast current-point)
            (empty? remaining-ast) (println "eol")
            :else (recur (rest remaining-ast))))))
