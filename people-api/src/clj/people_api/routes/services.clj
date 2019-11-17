(ns people-api.routes.services
  (:require
    [reitit.swagger :as swagger]
    [reitit.swagger-ui :as swagger-ui]
    [reitit.ring.coercion :as coercion]
    [reitit.coercion.spec :as spec-coercion]
    [reitit.ring.middleware.muuntaja :as muuntaja]
    [reitit.ring.middleware.multipart :as multipart]
    [reitit.ring.middleware.parameters :as parameters]
    [people-api.db :as db]
    [people-api.middleware.formats :as formats]
    [people-api.middleware.exception :as exception]
    [person.core :as p]
    [ring.util.http-response :refer :all]
    [clojure.java.io :as io]))

(defn add-person-handler [{{{:keys [person]} :body} :parameters}]
  (try
    (db/add-person person)
    {:status 201}
    (catch AssertionError e
      {:status 400
       :body {:error (.getMessage e)}})))

(defn by-gender-handler [_]
  {:status 200
   :body {:people (sort p/by-gender-last-name (db/list-people))}})

(defn by-birthdate-handler [_]
  {:status 200
   :body {:people (sort p/by-birth-date (db/list-people))}})

(defn by-name-handler [_]
  {:status 200
   :body {:people (sort p/by-last-name (db/list-people))}})

(defn service-routes []
  ["/api"
   {:coercion spec-coercion/coercion
    :muuntaja formats/instance
    :swagger {:id ::api}
    :middleware [;; query-params & form-params
                 parameters/parameters-middleware
                 ;; content-negotiation
                 muuntaja/format-negotiate-middleware
                 ;; encoding response body
                 muuntaja/format-response-middleware
                 ;; exception handling
                 exception/exception-middleware
                 ;; decoding request body
                 muuntaja/format-request-middleware
                 ;; coercing response bodys
                 coercion/coerce-response-middleware
                 ;; coercing request parameters
                 coercion/coerce-request-middleware
                 ;; multipart
                 multipart/multipart-middleware]}

   ;; swagger documentation
   ["" {:no-doc true
        :swagger {:info {:title "my-api"
                         :description "https://cljdoc.org/d/metosin/reitit"}}}

    ["/swagger.json"
     {:get (swagger/create-swagger-handler)}]

    ["/api-docs/*"
     {:get (swagger-ui/create-swagger-ui-handler
             {:url "/api/swagger.json"
              :config {:validator-url nil}})}]]

   ["/ping"
    {:get (constantly (ok {:message "pong"}))}]
   
   ["/records"
    {:swagger {:tags ["people"]}}

    [""
     {:post {:summary "adds a single person data-line separated by '|', ',', or ' '"
             :parameters {:body {:person string?}}
             :responses {201 {}
                         400 {:body {:error string?}}}
             :handler add-person-handler}}]
    
    ["/gender"
     {:get {:summary "returns people sorted by gender then last name"
            :responses {200 {:body {:people seq?}}}
            :handler by-gender-handler}}]
    ["/birthdate"
     {:get {:summary "returns people sorted by date of birth"
            :responses {200 {:body {:people seq?}}}
            :handler by-birthdate-handler}}]
    ["/name"
     {:get {:summary "returns people sorted by last name"
            :responses {200 {:body {:people seq?}}}
            :handler by-name-handler}}]]])
