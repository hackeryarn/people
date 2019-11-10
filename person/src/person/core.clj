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

;; Validators
(defn- validate-last-name
  [last-name]
  (or (string? last-name)
      (throw (AssertionError. "Last name should be a string."))))

(defn- validate-first-name
  [first-name]
  (or (string? first-name)
      (throw (AssertionError. "First name should be a string."))))

(defn- validate-gender
  [gender]
  (or (gender? gender)
      (throw (AssertionError. (str "Gender should be one of: "
                                    (str/join valid-genders ", "))))))

(defn- validate-favorite-color
  [favorite-color]
  (or (string? favorite-color)
      (throw (AssertionError. "Favorite color should be a string."))))

(defn- validate-date-of-birth
  [date-of-birth]
  (or (date-str? date-of-birth)
      (throw (AssertionError.
              "Date of birth should have the format MM/dd/yyyy"))))

(defn- validate-split-str
  [split-str]
  (or (= 5 (count split-str))
      (throw (AssertionError.
              "Not enough fields provided"))))

;; Parser
(defrecord Person [last-name first-name gender favorite-color date-of-birth])

(defn split-str
  "Splits a string by '|', ',', or ' '."
  [s]
  {:post [(validate-split-str %)]}
  (cond
    (str/includes? s "|") (str/split s #"\|")
    (str/includes? s ",") (str/split s #"\,")
    (str/includes? s " ") (str/split s #"\ ")
    :else []))

(defn make-person
  "Creates a person from a split string"
  [last-name first-name gender favorite-color date-of-birth]
  {:pre [(validate-last-name last-name)
         (validate-first-name first-name)
         (validate-gender gender)
         (validate-favorite-color favorite-color)
         (validate-date-of-birth date-of-birth)]}
  (->Person
   last-name
   first-name
   ((keyword (str/lower-case gender)) gender-map)
   favorite-color
   (time/local-date "MM/dd/yyyy" date-of-birth)))

(defn str->person
  "Parses a string separated by '|', ',', or ' ' into a person record"
  [s]
  (->> s
      split-str
      (apply make-person)))
