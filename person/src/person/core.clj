(ns person.core
  (:require [clojure.string :as str]))

(def psv "Artem|Chernyak|M|Green|05/03/1990")
(def csv "Artem,Chernyak,M,Green,05/03/1990")
(def ssv "Artem Chernyak M Green 05/03/1990")

(defn split-str
  "Splits a string by '|', ',', or ' '."
  [s]
  (cond
    (str/includes? s "|") (str/split s #"\|")
    (str/includes? s ",") (str/split s #"\,")
    (str/includes? s " ") (str/split s #"\ ")
    :else []))

;; Parse date into a map
;; Create specs for all functions
