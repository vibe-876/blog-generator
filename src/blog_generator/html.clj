(ns blog-generator.html
  (:gen-class)
  (:require [blog-generator.xml :as xml]))


(defn trans-ir-html
  "Translate from the internal representation into
  HTML. Just a special case of trans-ir-xml."
  [ast]
  (let [html-table {:body "body"
                    :paragraph "p"
                    :link "a"
                    :head "head"
                    :title "title"
                    :header "h1"
                    :word "p"}]
    
    (str "<html>"
         (xml/trans-ir-xml html-table ast)
         "</html>")))
