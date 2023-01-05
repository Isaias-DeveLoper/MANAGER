(ns manager.handlers.transaction-service-handler
  (:require [clojure.java.jdbc :as jdbc]
            [manager.database.connection :as conn]
            [manager.handlers.account-service-handler :as account-handler]
            [manager.handlers.transaction-service-handler :as transact])
  (:import (java.util Date UUID)))

(declare pending-exists check-balance-available authorized-transaction unauthorized-transaction return-type-account check-exists-transact)

(defn transact
  [cpf receive value_send reason]
   (try 
     (let [pending (pending-exists cpf)]
       (if (= pending "unauthorized transaction")
         (unauthorized-transaction cpf receive value_send reason)
         (let [account-balance (check-balance-available cpf value_send )]
           (if (= account-balance "unauthorized transaction")
             (unauthorized-transaction cpf receive value_send reason)
             (authorized-transaction cpf receive value_send reason)))))
     (catch Exception e (str "error in function transact:" (.getMessage e)))))

(defn pending-exists
  [cpf]
  (try 
    (let [account-id (apply str (account-handler/return-account-id cpf)) 
          status (-> conn/connection 
                     (jdbc/query ["SELECT account_status_id FROM account WHERE account_id = ?" account-id] 
                                 {:row-fn :account_status_id}))] 
      (if (= (apply int status) 0) 
        (str "authorized_transaction") 
        (str "unauthorized transaction")))
    (catch Exception e (str "error checking existing pending issues:" (.getMessage e)))))

(defn check-balance-available
  [cpf value]
  (try 
    (let [account-id (apply str (account-handler/return-account-id cpf))
          balance (-> conn/connection 
                      (jdbc/query ["SELECT account_limit FROM account WHERE account_id =?" account-id]
                                  {:row-fn :account_limit}))]
      (if (<= value (apply double balance))
        (str "authorized_transaction")
        (str "unauthorized transaction")))
    (catch Exception e (str "error checking existing balance:" (.getMessage e)))))

(defn return-type-account
  [account-id]
  (try
    (-> conn/connection
        (jdbc/query ["SELECT type_account FROM account WHERE account_id =?" account-id]
                    {:row-fn :type_account}))
    (catch Exception e (str "error returned type account:" (.getMessage e)))))

(defn return-balance
  [account-id]
  (try 
    (-> conn/connection 
        (jdbc/query ["SELECT account_limit FROM account WHERE account_id = ?" account-id]
                    {:row-fn :account_limit}))
    (catch Exception e (str "error function return balance:" (.getMessage e)))))

(defn unauthorized-transaction
  [cpf receive value_send reason]
  (try  
    (let [account-id-original (apply str (account-handler/return-account-id cpf))] 
        (-> conn/connection
            (jdbc/insert! :transactions {:id (.toString (UUID/randomUUID)) 
                                         :account_original_id account-id-original
                                         :account_original_status_id 1
                                         :account_original_type (apply str (return-type-account account-id-original))
                                         :account_receiver_id (.toString receive)
                                         :action "unauthorized"
                                         :value_sent value_send
                                         :transaction_status 1
                                         :expiration_date (new Date)
                                         :responsible_institution "MANAGER_SERVICES"
                                         :reason_for_transaction reason
                                         :completion_date (new Date)}))) 
    (str "unauthorized transaction!")
  (catch Exception e (str "error function unauthorized:" (.getMessage e)))))

(defn subtract-value
  [balance value]
   (- balance value))

(defn add-value
  [balance value]
  (+ balance value))

(defn authorized-transaction
  [cpf receive value_send reason]
  (try 
    (let [account-original (apply str (account-handler/return-account-id cpf))
          type-account (apply str (return-type-account account-original))
          receive-transact (.toString receive)
          balance-original (apply double (return-balance account-original))
          balance-receive  (apply double (return-balance receive-transact))
          new-balance-original (subtract-value balance-original value_send)
          new-balance-receive (add-value balance-receive value_send)
          reason-transact (.toString reason)]
      (jdbc/with-db-transaction [t-con conn/connection]
            (jdbc/insert! t-con :transactions {:id (.toString (UUID/randomUUID))
                                         :account_original_id account-original
                                         :account_original_status_id 0
                                         :account_original_type type-account
                                         :account_receiver_id receive-transact
                                         :action "authorized"
                                         :value_sent value_send
                                         :transaction_status 0 
                                         :expiration_date (new Date)
                                         :responsible_institution "MANAGER_SERVICES"
                                         :reason_for_transaction reason-transact
                                         :completion_date (new Date)})
            (jdbc/update! t-con :account {:account_limit new-balance-original}
                          ["account_id = ?" account-original])
            (jdbc/update! t-con :account {:account_limit new-balance-receive}
                          ["account_id = ?" receive-transact])
            (jdbc/insert-multi! t-con :historic_account
                                [{:id (.toString (UUID/randomUUID))
                                  :account_id account-original
                                  :balance new-balance-original
                                  :status_account 0
                                  :last_date (new Date)}
                                 {:id (.toString (UUID/randomUUID))
                                  :account_id receive-transact
                                  :balance new-balance-receive
                                  :status_account 0
                                  :last_date (new Date)}])))
    (str "successful transaction!")
    (catch Exception e (str "error function authorized:" (.getMessage e)))))

(defn reverse-transaction
  [id-transaction reason]
   (try 
     (if (empty? (check-exists-transact id-transaction))
       (str "non-existent transaction, please enter an existing identifier")
       (let [account-original (apply str (-> conn/connection
                                             (jdbc/query ["SELECT account_original_id FROM transactions WHERE id = ?" id-transaction]
                                                         {:row-fn :account_original_id})))
             account-receive (apply str (-> conn/connection
                                            (jdbc/query ["SELECT account_receiver_id FROM transactions WHERE id = ?" id-transaction]
                                                        {:row-fn :account_receiver_id})))
             transacted-value (apply double (-> conn/connection
                                                (jdbc/query ["SELECT value_sent FROM transactions WHERE id = ?" id-transaction]
                                                            {:row-fn :value_sent})))
             account-original-balance-update (add-value (apply double (return-balance account-original)) transacted-value)
             account-receive-balance-update (subtract-value (apply double (return-balance account-receive)) transacted-value)
             type-account-original (apply str (return-type-account account-receive))]
         (jdbc/with-db-transaction [t-con conn/connection]
           (jdbc/insert! t-con :transactions {:id (.toString (UUID/randomUUID))
                                              :account_original_id account-receive
                                              :account_original_status_id 0
                                              :account_original_type type-account-original
                                              :account_receiver_id account-original
                                              :action "reversal"
                                              :value_sent transacted-value
                                              :transaction_status 0
                                              :expiration_date (new Date)
                                              :responsible_institution "MANAGER_SERVICES"
                                              :reason_for_transaction reason
                                              :completion_date (new Date)})
           (jdbc/update! t-con :account {:account_limit account-original-balance-update}
                         ["account_id = ?" account-original])
           (jdbc/update! t-con :account {:account_limit account-receive-balance-update}
                         ["account_id = ?" account-receive])
           (jdbc/insert-multi! t-con :historic_account
                               [{:id (.toString (UUID/randomUUID))
                                 :account_id account-original
                                 :balance account-original-balance-update
                                 :status_account 0
                                 :last_date (new Date)}
                                {:id (.toString (UUID/randomUUID))
                                 :account_id account-receive
                                 :balance account-receive-balance-update
                                 :status_account 0
                                 :last_date (new Date)}])))) 
     (catch Exception e (str "error in function reverse-transaction:" (.getMessage e)))))

(defn check-exists-transact 
  [id-transaction]
   (try 
    (-> conn/connection
        (jdbc/query ["SELECT * FROM transactions WHERE id = ?" id-transaction]))
     (catch Exception e (str "error in function check-exists-transact:"(.getMessage e)))))















