(ns blog-generator.parser-test
  (:require [clojure.test :refer :all]
            [blog-generator.parser :refer :all]))


(deftest lex-chunk-end-words?
  (testing "Does lex-chunk-end do its thing?"
    (is (= [{:word ">:3"} '(\s \e \x \p \y \.)]
           (lex-chunk-end [">:3 sexpy."] [\ ])))))

(deftest chunk-chomping-1?
  (testing "Munching down on some tokens :3 ."
    (is (= [{:word "yummy"} {:word "tokens,"}
            '(\m \y \space \f \a \v \o \u \r \i \t \e \space \f \o \o \d \.)]
           (next-chunk [{:word "yummy"} "tokens, my favourite food."])))))

(deftest taking-and-dropping-properly?
  (testing "Does it take and drop properly?"
    (is (= ["hello" "world}"]
           [(apply str (rtake-until #(= \@ %) "{hello@world}"))
            (apply str (rdrop-until #(= \@ %) "{hello@world}"))]))))

(deftest lexing-links?
  (testing "Does the link lexer work?"
    (is (= (parse-link "{Camilla's Website@vibe-876.github.io} is her website.")
           [{:link {:text "Camilla's Website"
                    :uri "vibe-876.github.io"}}
            " is her website."]))))
