(ns manager.http.transact-http
  (:require [manager.business.transact-business :as transact-business]))

(defn new-transaction
  [{:keys [json-params]}]
   (let [cpf (:cpf json-params)
         receive (:destinatary json-params)
         value_send (:value json-params)
         reason (:reason json-params)
         status (transact-business/check-exists-account cpf receive value_send reason)]
     {:status (if (not (= status "account not found"))
                (if (= status "successful transaction!") 200 401)
                404)
      :headers {"Content-Type" "application/json"}
      :body (if (= status "successful transaction!")
              {:message "successfully transaction!"}
              {:message "Error!"})}))

(defn reverse-transaction-account
  [{:keys [json-params]}]
   (let [cpf (:cpf json-params)
         transact-id (:transaction json-params)
         reason (:reason_reversal json-params)
         status (transact-business/check-owner-transaction cpf transact-id reason)]
     {:status (if (not (= status "non-existent user!")) 200 404)
      :headers {"Content-Type" "application/json"}
      :body (if (nil? status)
              {:message "error"}
              {:message "reversal performed successfully!"})}))




