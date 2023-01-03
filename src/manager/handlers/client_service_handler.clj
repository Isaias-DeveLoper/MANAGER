(ns manager.handlers.client-service-handler
  (:require [clojure.java.jdbc :as jdbc]
            [manager.database.connection :as conn])
  (:import (java.util UUID Date)))

(defn create-account!
  [name lastname email cpf] 
  (let [id (.toString (UUID/randomUUID))
        create (new Date)]
     (try 
           (jdbc/with-db-transaction [t-con conn/connection ]
                        (jdbc/insert! t-con :users {:id (.toString (UUID/randomUUID))
                                              :client_name name
                                              :client_lastname lastname
                                              :client_email email
                                              :client_cpf cpf
                                              :client_account_id id
                                              :created_at create})
                               (jdbc/insert! t-con :account {:id (.toString (UUID/randomUUID))
                                                       :account_id id
                                                       :type_account "debit"
                                                       :account_limit 0.00
                                                       :account_status_id 0
                                                       :created_at create}))
          (str "Account created successfully!")
       (catch Exception e (str "error creating new user:" (.getMessage e))))))










