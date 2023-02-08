

(defproject template "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [io.pedestal/pedestal.service "0.5.11-beta-1"]
                 [io.pedestal/pedestal.jetty "0.5.11-beta-1"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [mysql/mysql-connector-java "8.0.31"]
                 [clj-time "0.15.2"]
                 [clojure.java-time "1.2.0"]
                 [environ "0.5.0"]
                 [lynxeyes/dotenv "1.1.0"]
                 [cheshire "5.11.0"]
                 [org.clojure/data.json "0.2.6"]
                 [ch.qos.logback/logback-classic "1.2.10" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.35"]
                 [org.slf4j/jcl-over-slf4j "1.7.35"]
                 [org.slf4j/log4j-over-slf4j "1.7.35"]]
  :min-lein-version "2.0.0"
  :resource-paths ["config", "resources"]
  :profiles {:dev {:aliases {"run-dev" ["trampoline" "run" "-m" "manager.server/run-dev"]}
                   :dependencies [[io.pedestal/pedestal.service-tools "0.5.11-beta-1"]]}
             :uberjar {:aot [manager.server]}}
  :main ^{:skip-aot true} manager.server)
