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

(deftest process-file
  (def expected-output "D|Artem|m|green|05/03/1991
G|Artem|m|green|05/03/1992
H|Artem|f|green|05/03/1993
A|Artem|f|green|05/03/1994
F|Artem|f|green|05/03/1995
C|Artem|m|green|05/03/1996
B|Artem|m|green|05/03/1997
E|Artem|f|green|05/03/1998")
  (testing "formats the output according to birth year"
    (is (= expected-output
           (doall (cli/process-file "resources/test.psv" "by-birth-date")))))
  
  (testing "gracefully exits on parsing error"
    (with-redefs [cli/exit (fn [status msg] {:status status :msg msg})]
      (let [result (cli/parse-person-line 4 "E|Artem|f|green|05-03-1998")]
        (is (= 1 (:status result)))
        (is (="Parsing error on line 5: Date of birth should have the format MM/dd/yyyy"
               (:msg result)))))))
