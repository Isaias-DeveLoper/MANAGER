(ns manager.handlers.account-service-handler
  (:require [clojure.java.jdbc :as jdbc]
            [manager.database.connection :as conn]))
(declare return-account-id)

(defn return-all-accounts
  []
  (try
      (-> conn/connection
          (jdbc/query ["SELECT * FROM account"]))
    (catch Exception e (str "error listing all existing accounts:" (.getMessage e)))))

(defn return-accounts-by-cpf
  [cpf]
   (try 
      (let [account-id (return-account-id cpf)
            account-id-string (apply str account-id)]
        (-> conn/connection
            (jdbc/query ["SELECT * FROM account WHERE account_id = ?" account-id-string])))
     (catch Exception e (str "error find account by cpf:" (.getMessage e)))))

(defn return-account-id
  [cpf]
  (try 
    (-> conn/connection 
        (jdbc/query ["SELECT * FROM users WHERE client_cpf = ?" cpf]
                    {:row-fn :client_account_id}))
    (catch Exception e (str "error find account_id:" (.getMessage e)))))










