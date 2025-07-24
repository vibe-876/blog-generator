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

(defn to-title
  "Translate a line into HTML."
  [raw-line]
  (->> (rest raw-line)
       (apply str)
       (taggify "h1" "")))

(defn to-link
  "Translate a line into a hyperlink."
  [raw-line]
  (->> (rest raw-line)
       (apply str)
       (#(str/split % #"@"))
       (#(taggify "a"
                  (apply str ["href=\"" (second %) "\""])
                  (first %)))))


(defn trans-markup-to-html
  "Maps a line of markup into a line of HTML."
  [raw-line]
  (cond (is-title? raw-line) (to-title raw-line)
        (is-link? raw-line) (to-link raw-line)
        :else (taggify "p" "" raw-line)))

(defn -main
  "Program entry point."
  [markup-doc output-file header-file & body-file]
  (let [head (slurp header-file)
        html-preamble ["<!DOCTYPE html>" "<html>" "<head>" head "</head>" "<body>"
                       (if (not= '() body-file)
                         (slurp (first body-file))
                         "")]
        html-postamble ["</body>" "</html>"]]
    
    (->> (slurp markup-doc)
         (str/split-lines)
         (map trans-markup-to-html)
         (#(vec (concat html-preamble % html-postamble)))
         (apply str)
         (spit output-file))))
