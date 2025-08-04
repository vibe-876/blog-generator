(ns blog-generator.core
  (:gen-class)
  (:require [blog-generator.parser :as parser]
            [blog-generator.tree :as tree]
            [blog-generator.html :as html]
            [blog-generator.rss :as rss]))


(defn -main
  "Program entry point."
  [file-name output-type]
  (->> (slurp file-name)
       (parser/trans-camarkup-ir)
       (tree/organise-ast)
       (#(cond (= output-type "html") (html/trans-ir-html %)
               (= output-type "rss") (rss/trans-ir-rss %)
               (= output-type "ir") ["raw.ir" (apply str %)]
               :else ["error-log" (str "Unknown output type: " output-type)]))
       (apply spit)))
