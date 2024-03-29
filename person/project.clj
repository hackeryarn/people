(defproject person "0.1.0-SNAPSHOT"
  :description "A library for parsing and interacting with person data."
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [clojure.java-time "0.3.2"]]
  :plugins [[lein-cloverage "1.1.2"]]
  :repl-options {:init-ns person.core})
