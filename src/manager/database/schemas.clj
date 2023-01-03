(ns manager.database.schemas
  (:require [clojure.java.jdbc :as jdbc]))

(def users-schema
  (jdbc/create-table-ddl :users
                         [[:id "varchar(36)"]
                          [:client_name "varchar(20) NOT NULL"]
                          [:client_lastname "varchar(30) NOT NULL"]
                          [:client_email "varchar(100) NOT NULL"]
                          [:client_cpf "varchar(11) NOT NULL"]
                          [:client_account_id "varchar(36)"]
                          [:created_at "timestamp"]]))

(def account-schema
  (jdbc/create-table-ddl :account
                         [[:id "varchar(36)"]
                          [:account_id "varchar(36)"]
                          [:type_account "varchar(20)"]
                          [:account_limit "double"]
                          [:account_status_id :int]
                          [:created_at "timestamp"]]))

(def debts-schema
  (jdbc/create-table-ddl :debts
                         [[:id "varchar(36)"]
                          [:label "varchar(30)"]
                          [:price "double"]
                          [:status_id :int]
                          [:deadline "timestamp"]
                          [:created_at "timestamp"]]))

