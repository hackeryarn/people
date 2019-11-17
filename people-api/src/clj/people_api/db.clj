(ns people-api.db
  (:require [person.parser :as pp]
            [mount.core :as mount]))

(mount/defstate db
  :start (atom []))

(defn add-person [person-str]
  (swap! db conj (pp/str->person person-str)))

(defn list-people []
  @db)


