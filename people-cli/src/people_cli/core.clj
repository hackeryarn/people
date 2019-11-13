(ns people-cli.core
  (:gen-class)
  (:require [clojure.tools.cli :as cli]
            [person.parser :as pp]
            [person.core :as p]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def cli-options
  [["-s" "--sort SORT" "Sort by type"
    :default "by-last-name"]
   ["-h" "--help"]])

(defn usage [options-summary]
  (->> ["Produces a sorted list from the provided file of person data."
        ""
        "Usage: people_cli [options] file"
        ""
        "The provided file must be separated by either:"
        "  - Pipe"
        "  - Comma"
        "  - Space"
        ""
        "Options:"
        options-summary
        ""
        "The possible sort options are:"
        "- by-gender-last-name"
        "- by-birth-date"
        "- by-last-name (Default)"]
       (str/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (str/join \newline errors)))

(def sort-options-map
  {:by-gender-last-name p/by-gender-last-name
   :by-birth-date p/by-birth-date
   :by-last-name p/by-last-name})

(def valid-sort-options
  (set (map name (keys sort-options-map))))

(defn validate-args
  "Validates cli options and arguments."
  [args]
  (let [{:keys [options arguments errors summary]} (cli/parse-opts args cli-options)]
    (cond
      (:help options)
      {:exit-message (usage summary) :ok? true}
      errors
      {:exit-message (error-msg errors)}
      (and (= 1 (count arguments))
           (valid-sort-options (:sort options)))
      {:file (first arguments) :options options}
      :else
      {:exit-message (usage summary)})))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn process-file
  "Processes the supplied file and provides the output in the specified format"
  [file sort-option]
  (with-open [r (io/reader file)]
     (->> (line-seq r)
          (map pp/str->person)
          (sort ((keyword sort-option) sort-options-map))
          (map p/format-person)
          (str/join "\n"))))

(defn -main [& args]
  (let [{:keys [file options exit-message ok?]} (validate-args args)
        sort (:sort options)]
    (if exit-message
      (exit (if ok? 0 1) exit-message)
      (print (process-file file sort)))))
