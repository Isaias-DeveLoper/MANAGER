(ns manager.http.account-http
  (:require [manager.business.account-business :as account-business]
            [manager.handlers.account-service-handler :as account-handler]
            [clojure.data.json :as json]))

(defn get-all-accounts
  [request]
   (let [accounts (account-handler/return-all-accounts)]
     {:status (if (empty? accounts) 204 200)
      :body accounts
      :headers {"Content-Type" "application/json"}}))

(defn get-account-by-cpf
  [request]
  (let [cpf (get-in request [:path-params :cpf])
        account (account-business/check_existence_user cpf)]
    {:status (if (= account "There is no account linked to this cpf!") 404 200) 
     :body account
     :headers {"Content-Type" "application/json"}}))






