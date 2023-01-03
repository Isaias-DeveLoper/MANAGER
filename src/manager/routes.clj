(ns manager.routes
  (:require [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http :as http]
            [manager.http.client-http :as client-user]))

(def routes
  `[[:app-name :manager
     ["/client/users/account" 
      {:post client-user/post-new-account
       :get client-user/get-all-users}
      ^:interceptors [(body-params/body-params) http/html-body]] 
     ["/client/users/account/:cpf" 
      ^:interceptors [(body-params/body-params) http/html-body] 
      ^:constraints  {:cpf #"[0-9]+"} 
      {:get client-user/get-users-by-cpf}]]])






