(ns manager.routes
  (:require [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http :as http]
            [manager.http.client-http :as client-user]))

;; (defn home-page
;;   [{:keys [json-params]}]
;;   (let [nm (:name json-params)]
;;     {:status 200
;;      :headers {"Content-type" "text/plain"}
;;      :body nm}))



(def routes
  `[[["/users/account/new" {:post client-user/post-new-account}
      ^:interceptors [(body-params/body-params) http/html-body]]]])


