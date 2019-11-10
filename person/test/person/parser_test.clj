(ns person.parser-test
  (:require [clojure.test :refer :all]
            [java-time :as time]
            [person.parser :as p]))

(deftest valid-str->person
  (def valid-psv "Chernyak|Artem|Male|Green|05/03/1990")
  (def valid-csv "Chernyak,Artem,M,Green,05/03/1990")
  (def valid-ssv "Chernyak Artem M Green 05/03/1990")

  (def valid-person
    (p/make-person
     "Chernyak"
     "Artem"
     "m"
     "Green"
     "05/03/1990"))

  (testing "pipe separated values"
    (is (= valid-person (p/str->person valid-psv))))
  (testing "comma separated values"
    (is (= valid-person (p/str->person valid-csv))))
  (testing "space separated values"
    (is (= valid-person (p/str->person valid-ssv))))

  (def valid-psv-with-spaces "Chernyak |Artem | Male| Green|05/03/1990")
  (def valid-csv-with-spaces "Chernyak ,Artem , M, Green,05/03/1990")

  (testing "pipe separated values with spaces"
    (is (= valid-person (p/str->person valid-psv-with-spaces))))
  (testing "comma separated values with spaces"
    (is (= valid-person (p/str->person valid-csv-with-spaces)))))



(deftest invalid-str->person
  (def no-separator "ChernyakArtem")
  (testing "missing separators"
    (try
      (is (thrown? AssertionError (p/str->person no-separator)))))

  (def invalid-gender "Chernyak|Artem|Alien|Green|05/03/1990")
  (testing "invalid gender"
    (try
      (is (thrown-with-msg? AssertionError
                            #"Gender"
                            (p/str->person invalid-gender)))))

  (def invalid-date-of-birth "Chernyak|Artem|M|Green|05-03-1990")
  (testing "invalid date of birth"
    (try
      (is (thrown-with-msg? AssertionError
                            #"MM/dd/yyyy"
                            (p/str->person invalid-date-of-birth))))))

(run-tests)
