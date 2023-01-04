(ns manager.business.account-business
  (:require [manager.handlers.account-service-handler :as account-handler]
            [manager.handlers.client-service-handler :as user-handler]))

(defn check_existence_user 
  [cpf]
   (let [found-user? (user-handler/get-users-by-cpf cpf)]
     (if (empty? found-user?)
       (str "There is no account linked to this cpf!")
       (account-handler/return-accounts-by-cpf cpf))))

