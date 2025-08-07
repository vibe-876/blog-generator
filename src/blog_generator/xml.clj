(ns blog-generator.xml
  (:gen-class))


(defn tag-pair
  "Put a thing into some xml tags."
  ([tag notes data]
   (apply str ["<" tag " " notes ">" data "</" tag ">"]))
  ([tag data]
   (apply str ["<" tag ">" data "</" tag ">"])))


(defn trans-node-xml
  "Translate the internal representation into
  xml.

  Note, this assumes that there's only one thing
  inside the map."
  [node]
  (let [type (keys node)
        data (vals node)]
    
    (cond (and (coll? data)
               (not (string? (first data))))
          (tag-pair "super-node" (trans-node-xml data))

          :else (tag-pair "node"
                          (first data)))))
