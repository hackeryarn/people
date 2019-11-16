(ns people-api.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[people-api started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[people-api has shut down successfully]=-"))
   :middleware identity})
