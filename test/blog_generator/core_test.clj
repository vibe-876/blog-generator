(ns blog-generator.core-test
  (:require [clojure.test :refer :all]
            [blog-generator.core :refer :all]))

(deftest trans-p
  (testing "Are paragraphs translated properly?"
    (is (= (taggify "p" "class=\"paragraph\"" "Hello, World :3 !")
           "<p class=\"paragraph\">Hello, World :3 !</p>"))))

(deftest div
  (testing "Are divs (containing things) translated correctly?"
    (is (= (map trans-markup-to-html ["~code"
                                                     "(+ 1 2"
                                                     "     3)"
                                                     ";"])
           ["<div class=\"code\">"
            "<p >(+ 1 2</p>"
            "<p >     3)</p>"
            "</div>"]))))

(deftest link-detecting
  (testing "Are links being detected properly?"
    (is (is-link? "!test@wow"))))
