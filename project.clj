(defproject blog-generator "0.1-SNAPSHOT"
  :description "A simple camarkup (a markup language) to HTML translator."
  :url "https://github.com/vibe-876/blog-generator"
  :license {:name "GPLv3"
            :url "https://www.gnu.org/licenses/gpl-3.0.txt"}
  :dependencies [[org.clojure/clojure "1.11.1"]]
  :main ^:skip-aot blog-generator.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
