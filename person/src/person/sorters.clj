(ns person.sorters
  (:require [person.parser :as p]
            [java-time :as time]
            [clojure.string :as str]))

(def people
  [(p/make-person
    "C"
    "Artem"
    "m"
    "Green"
    "05/03/1990")
   (p/make-person
    "d"
    "Artem"
    "F"
    "Green"
    "05/03/1990")
   (p/make-person
    "B"
    "Artem"
    "female"
    "Green"
    "05/03/1990")
   (p/make-person
    "A"
    "Artem"
    "male"
    "Green"
    "05/03/1990")
   (p/make-person
    "a"
    "Artem"
    "male"
    "Green"
    "05/03/1990")])

(defn sort-by-gender-and-last-name
  "Sorts the list first by gender then by last name ascending."
  [people]
  (->> people
       (sort-by #(str/lower-case (:last-name %)))
       (sort-by #(str/lower-case (:gender %)))))

(map :last-name (sort-by-gender-and-last-name people))
