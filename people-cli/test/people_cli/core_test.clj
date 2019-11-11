(ns people-cli.core-test
  (:require [clojure.test :refer :all]
            [people-cli.core :refer :all]))

(deftest main
  (def help-args ["-h"])
  (testing "help exit-message"
    (let [result (validate-args help-args)]
      (is (re-find #"Produces a sorted list from the provided file of person data"
                   (:exit-message result)))
      (is (= true (:ok? result)))))

  (def error-args ["--unknown"])
  (testing "error exit message"
    (is (re-find #"Unknown option: \"--unknown\""
           (:exit-message (validate-args error-args))))))

(run-tests)
