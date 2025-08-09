(ns blog-generator.html
  (:gen-class)
  (:require [blog-generator.xml :as xml]))


(defn trans-ir-html
  "Translate from the internal representation into
  HTML. Just a special case of trans-ir-xml."
  [ast]
  (let [head (apply hash-map (first ast))
        body (apply hash-map (second ast))
        html-table {:body "body"
                    :paragraph "p"
                    :link "a"
                    :head "head"
                    :title "title"
                    :header "h1"
                    :word "p"}]
    
    (str "<html>"
         (xml/trans-ir-xml html-table head)
         (xml/trans-ir-xml html-table body)
         "</html>")))
