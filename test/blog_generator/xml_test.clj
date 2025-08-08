(ns blog-generator.test_xml
  (:require [clojure.test :refer :all]
            [blog-generator.xml :refer :all]))


(deftest tags-correctly?
  (testing "Are things tagged properly?"
    (is (= "<rss version=\"2.0\"><title>First Blog Entry!</title></rss>"
           (tag-pair "rss" "version=\"2.0\"" (tag-pair "title" "First Blog Entry!"))))))

(deftest recursively-tags-properly?
  (testing "Is trans-ir-xml doing its thing properly?"
    (is (= "<body><h1>:3</h1><p>Hello</p></body>"
           (trans-ir-xml {:body '({:header {:level 1
                                           :text ":3"}}
                                  {:paragraph "Hello"})}
                         {:body "body"
                          :header "h"
                          :paragraph "p"})))))
