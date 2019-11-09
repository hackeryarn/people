(ns person.core-test
  (:require [clojure.test :refer :all]
            [java-time :as time]
            [person.core :as p]))

(deftest str->person
  (def valid-psv "Chernyak|Artem|Male|Green|05/03/1990")
  (def valid-csv "Chernyak,Artem,M,Green,05/03/1990")
  (def valid-ssv "Chernyak Artem M Green 05/03/1990")

  (def valid-person
    (p/map->Person {:first-name "Artem"
                    :last-name "Chernyak"
                    :gender "m"
                    :favorite-color "Green"
                    :date-of-birth (time/local-date
                                    "MM/dd/yyyy"
                                    "05/03/1990")}))

  (testing "pipe separated values"
    (is (= valid-person (p/str->person valid-psv))))
  (testing "comma separated values"
    (is (= valid-person (p/str->person valid-csv))))
  (testing "space separated values"
    (is (= valid-person (p/str->person valid-ssv))))

  (def no-separator "ChernyakArtem")

  (testing "missing separators"
    (try
      (is (thrown? AssertionError (p/str->person no-separator))))))

(run-tests)
