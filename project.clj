(defproject org.clojars.sulami/prelude "0.2.0"
  :license {:name "MIT License"}
  :description "Personal Toolbox"
  :url "https://github.com/sulami/prelude"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [potemkin "0.4.5"]]
  :java-source-paths ["src"]
  :jvm-opts ^:replace ["-server" "-Xmx4g"]
  :javac-options ["-source" "1.6" "-target" "1.6"])
