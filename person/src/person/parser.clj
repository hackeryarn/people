(ns person.parser
  (:require [clojure.string :as str]
            [person.core :as p]))

;; Validators
(defn- validate-split-str
  [split-str]
  (or (= 5 (count split-str))
      (throw (AssertionError.
              "Not enough fields provided"))))

;; Parser
(def psv-regex #"\ *\|\ *")
(def csv-regex #"\ *\,\ *")
(def ssv-regex #"\ ")

(defn- split-str
  "Splits a string by '|', ',', or ' '."
  [s]
  {:post [(validate-split-str %)]}
  (cond
    (str/includes? s "|") (str/split s psv-regex)
    (str/includes? s ",") (str/split s csv-regex)
    (str/includes? s " ") (str/split s ssv-regex)
    :else []))

(defn str->person
  "Parses a string separated by '|', ',', or ' ' into a person record"
  [s]
  (->> s
      split-str
      (apply p/make-person)))
