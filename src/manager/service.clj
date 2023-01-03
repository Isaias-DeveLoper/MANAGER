(ns manager.service
  (:require [io.pedestal.http :as http]
            [manager.routes :as routes]))


(def service {:env :prod
              ::http/routes routes/routes
              ::http/resource-path "/public" 
              ::http/type :jetty
              ::http/port 8080
              ::http/container-options {:h2c? true
                                        :h2? false
                                        :ssl? false }})
