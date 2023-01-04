(ns manager.business.transact-business 
  (:require [manager.handlers.account-service-handler :as account-handler]
            [manager.handlers.transaction-service-handler :as transact-handler])) 

(defn check-exists-account 
  [cpf receive value_send reason]
  (let [exists? (account-handler/return-account-by-code receive)]
    (if (empty? exists?)
      (str "account not found")
      (transact-handler/transact cpf receive value_send reason))))

