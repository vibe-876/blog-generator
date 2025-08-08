(ns blog-generator.xml
  (:gen-class))


(defn tag-pair
  "Put a thing into some xml tags."
  ([tag notes data]
   (apply str ["<" tag " " notes ">" data "</" tag ">"]))
  ([tag data]
   (apply str ["<" tag ">" data "</" tag ">"])))

(defn decipher-type
  "Serves the same purpose as (:symbol map), but
  with a little bit of error handling."
  [s-type type-table]
  (let [d-type (s-type type-table)]
    (if d-type
      d-type
      "error")))


(defn trans-ir-xml
  "Translate the internal representation into
  xml.

  Note, this assumes that there's only one thing
  inside the map.

  The node table is a vector of keys, and tag names.
  For example, setting [{:word \"p\"}] will translate
  {:word \"hello :3\"} into <p>hello :3</p>."
  [ast type-table]
  (let [symbol-type (->> (keys ast)
                         (first))
        data (->> (vals ast)
                  (first))]
    
    (cond (list? data) (tag-pair (decipher-type symbol-type type-table)
                                 (loop [remaining data
                                        node (first remaining)
                                        carry ""]
                                   (if (empty? remaining)
                                     carry
                                     (recur (rest remaining)
                                            (second remaining)
                                            (str carry (trans-ir-xml node type-table))))))
          
          :else (tag-pair (decipher-type symbol-type type-table)
                          data))))
