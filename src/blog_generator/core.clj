(ns blog-generator.core
  (:gen-class)
  (:require [blog-generator.parser :as parser]
            [blog-generator.rss :as rss]))


;; (defn is-link?
;;   "Is the line a link?"
;;   [raw-line]
;;   (and (= (first raw-line) \!)
;;        (boolean (re-find #"@" raw-line))))

;; (defn is-title?
;;   "Is this line a title?
;;   This should probably be a special case of a macro."
;;   [raw-line]
;;   (= (first raw-line) \#))

;; (defn is-div?
;;   "Is this line a div?"
;;   [raw-line]
;;   (= (first raw-line) \~))

;; (defn is-undiv?
;;   "Is the line the end of a div?"
;;   [raw-line]
;;   (= (first raw-line) \;))

;; (defn taggify
;;   "Enter data between two tags.
;;   (taggify a b c) becomes \"<a b>c</a>\"."
;;   [tag-name tag-data data]
;;   (apply str ["<" tag-name " " tag-data ">" data "</" tag-name ">"]))

;; (defn single-taggify
;;   "Enter data into a single tag"
;;   [tag-name tag-data]
;;   (apply str ["<" tag-name " " tag-data ">"]))

;; (defn to-title
;;   "Translate a line into HTML."
;;   [raw-line]
;;   (->> (rest raw-line)
;;        (apply str)
;;        (taggify "h1" "")))

;; (defn to-link
;;   "Translate a line into a hyperlink."
;;   [raw-line]
;;   (->> (rest raw-line)
;;        (apply str)
;;        (#(string/split % #"@"))
;;        (#(taggify "a"
;;                   (apply str ["href=\"" (second %) "\""])
;;                   (first %)))))

;; (defn to-div
;;   "Translate a line to a div."
;;   [raw-line]
;;   (->> (rest raw-line)
;;        (apply str)
;;        (#(single-taggify "div"
;;                          (apply str ["class=" \" % \"])))))

;; (defn trans-markup-to-html
;;   "Maps a line of markup into a line of HTML."
;;   [raw-line]
;;   (cond (is-title? raw-line) (to-title raw-line)
;;         (is-link? raw-line) (to-link raw-line)
;;         (is-div? raw-line) (to-div raw-line)
;;         (is-undiv? raw-line) "</div>"
;;         :else (taggify "p" "" raw-line)))

;; (defn -main
;;   "Program entry point."
;;   [markup-doc output-file header-file & body-file]
;;   (let [head (slurp header-file)
;;         html-preamble ["<!DOCTYPE html>" "<html>" "<head>" head "</head>" "<body>"
;;                        (if (not= '() body-file)
;;                          (slurp (first body-file))
;;                          "")]
;;         html-postamble ["</body>" "</html>"]]
    
;;     (->> (slurp markup-doc)
;;          (string/split-lines)
;;          (map trans-markup-to-html)
;;          (#(vec (concat html-preamble % html-postamble)))
;;          (apply str)
;;          (spit output-file))))
(defn -main
  "Program entry point."
  [& args]
  0)
