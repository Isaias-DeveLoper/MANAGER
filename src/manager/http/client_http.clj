(ns manager.http.client-http
  (:require [manager.business.client-business :as client-business]
            [manager.handlers.client-service-handler :as client-handler]
            [clojure.data.json :as json]))

(defn post-new-account 
  [{:keys [json-params]}]
   (let [name (.toString (:name json-params))
         lastname (.toString (:lastname json-params))
         email (.toString (:email json-params))
         cpf  (.toString (:cpf json-params))
         is-valid? (client-business/validate-new-user name lastname email cpf)]
     {:status (if (= is-valid? "Account created successfully!") 201 500)
      :body (str is-valid?)
      :headers {"Content-Type" "application/json"}}))

(defn get-all-users
  [request]
  {:status (if (empty? (client-handler/get-users)) 404 200)
   :body  (client-handler/get-users) 
   :headers {"Content-Type" "application/json"}})

(defn get-users-by-cpf 
  [request]
  (let [cpf (get-in  request [:path-params :cpf])]
    {:status (if (empty? (client-handler/get-users-by-cpf cpf)) 404 200)
     :body  (client-handler/get-users-by-cpf cpf)
     :headers {"Content-Type" "application/json"}}))

(defn put-users 
  [{:keys [json-params]}]
   (let [name (.toString (:name json-params))
         lastname (.toString (:lastname json-params))
         email (.toString (:email json-params))
         cpf (.toString (:cpf json-params))
         status (client-business/validate-update-user name lastname email cpf)]
     {:status (if (= status "No account was found with this CPF!") 404 200)
      :body (str status)
      :headers {"Content-Type" "application/json"}}))



































