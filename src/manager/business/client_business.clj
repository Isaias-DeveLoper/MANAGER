(ns manager.business.client-business
  (:require [clojure.java.jdbc :as jdbc]
            [manager.database.connection :as conn]
            [manager.handlers.client-service-handler :as services]))

(declare validate-email)

(defn validate-new-user 
  [name lastname email cpf]
  (let [cpf_user (-> conn/connection
                (jdbc/query ["SELECT * FROM users WHERE client_cpf = ?" cpf]))]
    (if (empty? cpf_user)
      (let [is-valid? (validate-email email)]
        (if (nil? is-valid?)
          (str "invalid email")
          (services/create-account! name lastname email cpf)))
      (str "there is already an account with this cpf"))))

(defn validate-email [email]
  (re-matches #".+\@.+\..+" email))






