(ns blog-generator.core
  (:gen-class)
  (:require [clojure.string :as str]))


(defn is-link?
  "Is the line a link?"
  [raw-line]
  (and (= (first raw-line) \!)
           (boolean (re-find #"@" raw-line))))

(defn is-title?
  "Is this line a title?
  This should probably be a special case of a macro."
  [raw-line]
  (= (first raw-line) \#))

(defn taggify
  "Enter data between two tags.
  (taggify a b c) becomes \"<a b>c</a>\"."
  [tag-name tag-data data]
  (apply str ["<" tag-name " " tag-data ">" data "</" tag-name ">"]))

(defn trans-markup-to-html
  "Maps a line of markup into a line of HTML."
  [raw-line]
  (cond (is-title? raw-line) (->> (rest raw-line)
                                  (apply str)
                                  (taggify "h1" ""))

        (is-link? raw-line) (->> (rest raw-line)
                                 (apply str)
                                 (#(str/split % #"@"))
                                 (#(taggify "a"
                                            (apply str ["href=\"" (second %) "\""])
                                            (first %))))
        
        :else (taggify "p" "" raw-line)))

(defn -main
  "Program entry point."
  [markup-doc & args]
  (let [css-file "styles.css"
        output-file "post.html"]
    (->> (slurp markup-doc)
         (str/split-lines)
         (map trans-markup-to-html)
         (#(vec (concat ["<DOCTYPE html>" "<html>" "<head>"
                         "<title>Blog Entry</title>" "<link rel=\"stylesheet\" href=\"" css-file "\">"
                         "</head>" "<body>"] % ["</body>" "</html>"])))
         (apply str)
         (spit output-file))))
