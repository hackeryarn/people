(ns person.core
  (:require [clojure.string :as str]
            [java-time :as time]))

;; Helpers
(def gender-map
  "Possible user provided genders to consistent gender."
  {:m "m"
   :f "f"
   :male "m"
   :female "f"})

(def valid-genders
  "List of valid genders"
  (map name (keys gender-map)))

(defn gender?
  "Checks if string is a valid gender"
  [s]
  (some #{(str/lower-case s)} valid-genders))

(defn date-str?
  "Checks for valid date string"
  [date]
  (boolean (re-matches #"\d\d\/\d\d\/\d\d\d\d" date)))

;; Parser
(defrecord Person [last-name first-name gender favorite-color date-of-birth])

(defn split-str
  "Splits a string by '|', ',', or ' '."
  [s]
  (cond
    (str/includes? s "|") (str/split s #"\|")
    (str/includes? s ",") (str/split s #"\,")
    (str/includes? s " ") (str/split s #"\ ")
    :else []))

(defn split-str->person
  "Creates a person from a split string"
  [[last-name first-name gender favorite-color date-of-birth]]
  {:pre [(string? last-name)
         (string? first-name)
         (gender? gender)
         (string? favorite-color)
         (date-str? date-of-birth)]}
  (->Person
   last-name
   first-name
   ((keyword (str/lower-case gender)) gender-map)
   favorite-color
   (time/local-date "MM/dd/yyyy" date-of-birth)))

(defn str->person
  "Parses a string separated by '|', ',', or ' ' into a person record"
  [s]
  (-> s
      split-str
      split-str->person))
