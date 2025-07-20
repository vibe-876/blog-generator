(ns blog-generator.core
  (:gen-class)
  (:require [clojure.string :as str]))


(defn trans-markup-to-html
  "Maps a line of markup into a line of HTML."
  [raw-line]
  (cond (is-title raw-line) (->> (rest raw-line)
                                 (apply str)
                                 (#(taggify "h1" %)))
        
        :else (taggify "p" raw-line)))

(defn is-title
  "Is this line a title?
  This should probably be a macro."
  [raw-line]
  (= (first raw-line) \#))

(defn taggify
  "Enter data between two tags.
  (taggify a b c) becomes \"<a c>b</a>\"."
  [tag-name data & extra-data]
  (apply str ["<" tag-name ">" data "</" tag-name ">"]))


(defn -main
  "Program entry point."
  [markup-doc & args]
  (->> (slurp markup-doc)
       (str/split-lines)
       (map trans-markup-to-html)
       (apply str)))
