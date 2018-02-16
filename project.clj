(defproject com.mjachimowicz/duct-migrations-auto-cfg "0.1.0-SNAPSHOT"
  :description "Very simple sql db module for Duct framework"
  :url         "https://github.com/mariusz-jachimowicz-83/duct-migrations-auto-cfg"
  :license     {:name "Eclipse Public License"
                :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.9.0-beta4"]
                 [integrant "0.6.1"]
                 [medley    "1.0.0"]
                 [duct/core "0.6.1"]
                 [ragtime/jdbc "0.7.2"]]

  :deploy-repositories [["clojars" {:sign-releases false}]]

  ;; lein cloverage --fail-threshold 95
  ;; lein kibit
  ;; lein eastwood
  :profiles {:dev {:dependencies [[duct/logger     "0.2.1"]
                                  [duct/module.sql "0.4.2"]
                                  [org.clojure/java.jdbc  "0.7.3"]
                                  [org.xerial/sqlite-jdbc "3.20.1"]
                                  [org.slf4j/slf4j-nop    "1.7.25"]
                                  [org.clojure/java.jdbc  "0.7.3"]]
                   :plugins [[lein-cloverage  "1.0.10"]
                             [lein-kibit      "0.1.6"]
                             [jonase/eastwood "0.2.5"]]}})
