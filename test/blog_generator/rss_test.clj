(ns blog-generator.rss-test
  (:require [clojure.test :refer :all]
            [blog-generator.rss :refer :all]))

(deftest trans-ast-chunk-test
  (testing "Does the ast to xml translator sorta work."
    (is (= "<rss version=\"2.0\"></rss>" (trans-ast-xml '(rss version="2.0"))))))
