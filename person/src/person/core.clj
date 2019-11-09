(ns person.core
  (:require [clojure.string :as str]))

(def psv "Artem|Chernyak|M|Green|05/03/1990")
(def csv "Artem,Chernyak,M,Green,05/03/1990")
(def ssv "Artem Chernyak M Green 05/03/1990")

;; Helpers

(def gender-map
  "Possible user provided genders to consistent gender."
  {:m "m"
   :f "f"
   :male "m"
   :female "f"})

(def valid-genders
  ["m"
   "f"
   "male"
   "female"])

(defn gender?
  "Checks if string is a valid gender"
  [s]
  (some #{(str/lower-case s)} valid-genders))

(defn split-str
  "Splits a string by '|', ',', or ' '."
  [s]
  (cond
    (str/includes? s "|") (str/split s #"\|")
    (str/includes? s ",") (str/split s #"\,")
    (str/includes? s " ") (str/split s #"\ ")
    :else []))

;; Person functions

(defrecord Person [last-name first-name gender favorite-color date-of-birth])

(defn split-str->person
  "Creates a person from a split string"
  [[last-name first-name gender favorite-color date-of-birth]]
  {:pre [(string? last-name)
         (string? first-name)
         (gender? gender)
         (string? favorite-color)
         (string? date-of-birth)]}
  (->Person last-name first-name gender favorite-color date-of-birth))

(defn string->person
  "Parses a string separated by '|', ',', or ' ' into a person record"
  [s]
  (-> s
      split-str
      split-str->person))

(string->person psv)
