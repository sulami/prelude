(defproject schlachtschiff "0.0.1"
  :license {:name "MIT License"}
  :description "Personal Prelude."
  :dependencies [[org.clojure/clojure "1.9.0"]]
  :java-source-paths ["src"]
  :jvm-opts ^:replace ["-server" "-Xmx4g"]
  :javac-options ["-source" "1.6" "-target" "1.6"]
  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"})