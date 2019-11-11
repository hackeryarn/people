(ns person.core-test
  (:require [person.core :as p]
            [java-time :as time]
            [clojure.test :refer :all]))

(deftest make-person
  (def person
    (p/->Person
     "Chernyak"
     "Artem"
     "m"
     "Green"
     (time/local-date "MM/dd/yyyy" "05/03/1990")))

  (testing "valid parser"
    (is (= person
           (p/make-person
            "Chernyak"
            "Artem"
            "m"
            "Green"
            "05/03/1990"))))

  (testing "maps gender"
    (is (= person
           (p/make-person
            "Chernyak"
            "Artem"
            "Male"
            "Green"
            "05/03/1990"))))

  (testing "invalid last name"
    (try
      (is (thrown-with-msg? AssertionError
                            #"Last name should be a string"
                            (p/make-person
                             1
                             "Artem"
                             "m"
                             "Green"
                             "05/03/1990")))))

  (testing "invalid first name"
    (try
      (is (thrown-with-msg? AssertionError
                            #"First name should be a string"
                            (p/make-person
                             "Chernyak"
                             1
                             "m"
                             "Green"
                             "05/03/1990")))))

  (testing "invalid gender"
    (try
      (is (thrown-with-msg? AssertionError
                            #"Gender"
                            (p/make-person
                             "Chernyak"
                             "Artem"
                             "Alient"
                             "Green"
                             "05/03/1990")))))

  (testing "invalid favorite color name"
    (try
      (is (thrown-with-msg? AssertionError
                            #"Favorite color should be a string"
                            (p/make-person
                             "Chernyak"
                             "Artem"
                             "m"
                             1
                             "05/03/1990")))))

  (testing "invalid date of birth"
    (try
      (is (thrown-with-msg? AssertionError
                            #"Date of birth should have the format MM/dd/yyyy"
                            (p/make-person
                             "Chernyak"
                             "Artem"
                             "m"
                             "Green"
                             "05-03-1990"))))))

(run-tests)
