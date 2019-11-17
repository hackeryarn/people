(ns people-api.test.handler
  (:require
    [clojure.test :refer :all]
    [ring.mock.request :refer :all]
    [people-api.handler :refer :all]
    [people-api.middleware.formats :as formats]
    [muuntaja.core :as m]
    [mount.core :as mount]))

(defn parse-json [body]
  (m/decode formats/instance "application/json" body))

(use-fixtures
  :once
  (fn [f]
    (mount/start #'people-api.config/env
                 #'people-api.db/db
                 #'people-api.handler/app-routes)
    (f)))

(deftest test-app
  (testing "main route"
    (let [response ((app) (request :get "/"))]
      (is (= 301 (:status response)))))

  (testing "not-found route"
    (let [response ((app) (request :get "/invalid"))]
      (is (= 404 (:status response)))))
  
  (testing "services"
    (testing "adds person"
      (let [response ((app) (-> (request :post "/api/records")
                                (content-type "application/json")
                                (json-body {:person "chernyak,artem,m,green,05/03/1990"})))]
        (is (= 201 (:status response)))))

    (testing "invalid post"
      (let [response ((app) (-> (request :post "/api/records")
                                (content-type "application/json")
                                (json-body {})))]
        (is (= 201 (:status response)))))

    (testing "invalid add person"
      (let [response ((app) (-> (request :post "/api/records")
                                (json-body {:person "chernyak,artem,alien,green,05/03/1990"})
                                (header "accept" "application/transit+json")))]
        (is (= 400 (:status response)))
        (is (= {:error "Gender should be one of: m, f, male, female"} (m/decode-response-body response)))))

    (testing "get records by gender"
      (let [response ((app) (-> (request :get "/api/records/gender")
                                (header "accept" "application/transit+json")))]
        (is (= 200 (:status response)))
        (is (= "artem" (-> (m/decode-response-body response) :people first :first-name)))))

    (testing "get records by birthdate"
      (let [response ((app) (-> (request :get "/api/records/birthdate")
                                (header "accept" "application/transit+json")))]
        (is (= 200 (:status response)))
        (is (= "artem" (-> (m/decode-response-body response) :people first :first-name)))))

    (testing "get records by name"
      (let [response ((app) (-> (request :get "/api/records/name")
                                (header "accept" "application/transit+json")))]
        (is (= 200 (:status response)))
        (is (= "artem" (-> (m/decode-response-body response) :people first :first-name)))))))
