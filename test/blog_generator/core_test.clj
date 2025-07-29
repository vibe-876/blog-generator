(ns blog-generator.core-test
  (:require [clojure.test :refer :all]
            [blog-generator.core :refer :all]))

(deftest trans-p
  (testing "Are paragraphs translated properly"
    (is (= (blog-generator.core/taggify "p" "class=\"paragraph\"" "Hello, World :3 !")
           "<p class=\"paragraph\">Hello, World :3 !</p>"))))
