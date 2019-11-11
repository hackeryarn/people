(ns person.parser-test
  (:require [clojure.test :refer :all]
            [person.core :as p]
            [person.parser :as pp]))

(deftest str->person
  (def person
    (p/make-person
     "Chernyak"
     "Artem"
     "m"
     "Green"
     "05/03/1990"))

  (def psv "Chernyak|Artem|Male|Green|05/03/1990")
  (def csv "Chernyak,Artem,M,Green,05/03/1990")
  (def ssv "Chernyak Artem M Green 05/03/1990")
  (testing "pipe separated values"
    (is (= person (pp/str->person psv))))
  (testing "comma separated values"
    (is (= person (pp/str->person csv))))
  (testing "space separated values"
    (is (= person (pp/str->person ssv))))

  (def psv-with-spaces "Chernyak |Artem | Male| Green|05/03/1990")
  (def csv-with-spaces "Chernyak ,Artem , M, Green,05/03/1990")
  (testing "pipe separated values with spaces"
    (is (= person (pp/str->person psv-with-spaces))))
  (testing "comma separated values with spaces"
    (is (= person (pp/str->person csv-with-spaces))))

  (def no-separator "ChernyakArtem")
  (testing "missing separators"
    (try
      (is (thrown? AssertionError (pp/str->person no-separator))))))
