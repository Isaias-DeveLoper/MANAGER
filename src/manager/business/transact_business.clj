(ns manager.business.transact-business 
  (:require [manager.handlers.account-service-handler :as account-handler]
            [manager.handlers.transaction-service-handler :as transact-handler]
            [manager.handlers.client-service-handler :as user-handler]))

(defn check-exists-account 
  [cpf receive value_send reason]
  (let [exists? (account-handler/return-account-by-code receive)]
    (if (empty? exists?)
      (str "account not found")
      (transact-handler/transact cpf receive value_send reason))))

(defn check-owner-transaction
  [cpf transact-id reason]
  (let [exists? (user-handler/get-users-by-cpf cpf)]
    (if (empty? exists?)
      (str "non-existent user!")
      (transact-handler/reverse-transaction transact-id reason))))


