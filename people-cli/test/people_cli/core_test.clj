(ns people-cli.core-test
  (:require [clojure.test :refer :all]
            [people-cli.core :as cli]))

(deftest validate-args
  (def help-args ["-h"])
  (testing "help exit-message"
    (let [result (cli/validate-args help-args)]
      (is (re-find #"Produces a sorted list from the provided file of person data"
                   (:exit-message result)))
      (is (= true (:ok? result)))))

  (def error-args ["--unknown"])
  (testing "error exit message"
    (is (re-find #"Unknown option: \"--unknown\""
                 (:exit-message (cli/validate-args error-args)))))

  (def valid-args ["test.csv"])
  (testing "valid args"
    (let [result (cli/validate-args valid-args)]
      (is (= "test.csv" (:file result)))
      (is (= "by-last-name" (-> result :options :sort)))))

  (def valid-sort-option ["test.csv" "-s" "by-gender-last-name"])
  (testing "sets sort option when provided"
    (let [result (cli/validate-args valid-sort-option)]
      (is (= "test.csv" (:file result)))
      (is (= "by-gender-last-name" (-> result :options :sort)))))

  (def invalid-sort-option ["test.csv" "-s" "unknown"])
  (testing "provides usage information if invalid sort options is provided"
    (is (re-find #"Produces a sorted list from the provided file of person data"
                (:exit-message (cli/validate-args invalid-sort-option))))))

(run-tests)
