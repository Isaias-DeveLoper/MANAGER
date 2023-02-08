(ns manager.interceptors.parser
  (:require [cheshire.core :refer :all]))


(def to->json
  {:name ::to->json
   :leave
   (fn [context]
     (let [accepted         (get-in context [:request :accept :field] "text/plain")
           response         (get context :response)
           body             (get response :body)
           coerced-body     (generate-string body)
           updated-response (assoc response
                                   :headers {"Content-Type" accepted}
                                   :body    coerced-body)]
       (assoc context :response updated-response)))})

