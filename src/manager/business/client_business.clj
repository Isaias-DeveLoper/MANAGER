(ns manager.business.client-business
  (:require [manager.handlers.client-service-handler :as services]))

(declare validate-email)

(defn validate-new-user 
  [name lastname email cpf]
  (let [cpf_user (services/get-users-by-cpf cpf)]
    (if (empty? cpf_user)
      (let [is-valid? (validate-email email)]
        (if (nil? is-valid?)
          (str "invalid email")
          (services/create-account! name lastname email cpf)))
      (str "there is already an account with this cpf"))))

(defn validate-update-user
  [name lastname email cpf]
   (let [cpf_found? (services/get-users-by-cpf cpf)]
     (if (empty? cpf_found?) 
       (str "No account was found with this CPF!")
         (let [is-valid? (validate-email email)]
           (if (nil? is-valid?)
            (str "invalid email")
             (services/update-user! name lastname email cpf))))))

(defn validate-email [email]
  (re-matches #".+\@.+\..+" email))






