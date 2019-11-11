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

(deftest by-gender-last-name
  (def male
    (p/make-person
     "C"
     "Artem"
     "m"
     "Green"
     "05/03/1990"))
  (def female
    (p/make-person
     "C"
     "Artem"
     "f"
     "Green"
     "05/03/1990"))
  (testing "male vs female order"
    (is (< 0 (p/by-gender-last-name male female))))

  (def male-last-name-z
    (p/make-person
     "Z"
     "Artem"
     "m"
     "Green"
     "05/03/1990"))
  (testing "ascending last name after gender check"
    (is (> 0 (p/by-gender-last-name male male-last-name-z))))

  (def male-last-name-lower-c
    (p/make-person
     "c"
     "Artem"
     "m"
     "Green"
     "05/03/1990"))
  (testing "ignores case in last name"
    (is (> 0 (p/by-gender-last-name male-last-name-lower-c male-last-name-z))))

  (def female-last-name-z
    (p/make-person
     "z"
     "Artem"
     "f"
     "Green"
     "05/03/1990"))
  (testing "ignores last name in favor of gender"
    (is (< 0 (p/by-gender-last-name male female-last-name-z)))))

(deftest by-birth-date
  (def young
    (p/make-person
     "C"
     "Artem"
     "m"
     "Green"
     "05/03/1990"))
  (def old
    (p/make-person
     "C"
     "Artem"
     "f"
     "Green"
     "05/03/1900"))
  (testing "compares birth dates in ascending order"
    (is (< 0 (p/by-birth-date young old)))))

(deftest by-last-name
  (def a-last-name
    (p/make-person
     "a"
     "Artem"
     "m"
     "Green"
     "05/03/1990"))
  (def z-last-name
    (p/make-person
     "z"
     "Artem"
     "f"
     "Green"
     "05/03/1900"))
  (testing "compares last names"
    (is (< 0 (p/by-last-name a-last-name z-last-name))))

  (def capital-z-last-name
    (p/make-person
     "Z"
     "Artem"
     "f"
     "Green"
     "05/03/1900"))
  (testing "ignores last name case"
    (is (< 0 (p/by-last-name a-last-name capital-z-last-name)))))
