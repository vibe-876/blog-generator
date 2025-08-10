(ns blog-generator.xml
  (:gen-class))


(defn tag-pair
  "Put a thing into some xml tags."
  ([tag notes data]
   (apply str ["<" tag " " notes ">" data "</" tag ">"]))
  ([tag data]
   (apply str ["<" tag ">" data "</" tag ">"])))

(defn build-tag
  "Serves the same purpose as (:symbol map), but
  with a little bit of error handling, and then calls
  tag-pair."
  [s-type type-table data]
  (tag-pair (let [d-type (s-type type-table)]
              (if d-type
                d-type
                "error"))
            data))

(defn trans-link-xml
  [data]
  (let [pair (first data)]
    (str "<a href=\"" (:uri pair) "\">"
         (:text pair)
         "</a>")))

(defn trans-header-xml
  [data]
  (let [pair (first data)
        header-tag (str \h (:level pair))]
    (str "<" header-tag ">"
         (:text pair)
         "</" header-tag ">")))


(defn trans-ir-xml
  "Translate the internal representation into
  xml.

  Note, this assumes that there's only one thing
  inside the map.

  The node table is a vector of keys, and tag names.
  For example, setting [{:word \"p\"}] will translate
  {:word \"hello :3\"} into <p>hello :3</p>."
  [type-table ast]
  (let [symbol-type (->> (keys ast)
                         (first))
        data (->> (vals ast)
                  (first))]
    
    (cond (= :link symbol-type) (trans-link-xml data)
          (= :header symbol-type) (trans-header-xml data)
          (seq? data) (build-tag symbol-type type-table
                                 (loop [remaining data
                                        node (first remaining)
                                        carry ""]
                                   (if (empty? remaining)
                                     carry
                                     (recur (rest remaining)
                                            (second remaining)
                                            (str carry (trans-ir-xml type-table node))))))
          
          :else (build-tag symbol-type type-table
                           data))))
