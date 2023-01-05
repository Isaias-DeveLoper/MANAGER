(ns manager.routes
  (:require [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http :as http]
            [manager.http.client-http :as user-http]
            [manager.http.account-http :as account-http]
            [manager.http.transact-http :as transact-http]))

(def routes 
  `[[:app-name :manager
     ["/api/v1/services" 
      ["/users" 
       {:post user-http/post-new-account 
        :get user-http/get-all-users 
        :put user-http/put-users} 
       ^:interceptors [(body-params/body-params) http/html-body] 
       ["/:cpf" 
        ^:interceptors [(body-params/body-params) http/html-body] 
        ^:constraints  {:cpf #"[0-9]+"} 
        {:get user-http/get-users-by-cpf}]]
      ["/accounts"
       {:get account-http/get-all-accounts}
       ^:interceptors [(body-params/body-params) http/html-body]
       ["/:cpf" 
         {:get account-http/get-account-by-cpf}
        ^:interceptors [(body-params/body-params) http/html-body]
        ^:constraints  {:cpf #"[0-9]+"}]]
      ["/transact/new"
       {:post transact-http/new-transaction}
       ^:interceptors [(body-params/body-params) http/html-body]]
      ["/transact/reversal"
       {:post transact-http/reverse-transaction-account}
       ^:interceptors [(body-params/body-params) http/html-body]]]]])



