(ns manager.http.client-http
  (:require [manager.business.client-business :as client-business]
            [manager.handlers.client-service-handler :as client-handler]))

(defn post-new-account 
  [{:keys [json-params]}]
   (let [name (.toString (:name json-params))
         lastname (.toString (:lastname json-params))
         email (.toString (:email json-params))
         cpf  (:cpf json-params)
         is-valid? (client-business/validate-new-user name lastname email cpf)]
     {:status (if (= is-valid? "Account created successfully!") 201 500)
      :headers {"Content-type" "text/plain"}
      :body (str is-valid?)}
     ))

(defn get-all-users
  [request]
  {:status (if (empty? (client-handler/get-users)) 404 200)
   :headers {"Content-type" "text/json"}
   :body(client-handler/get-users)})


(defn get-users-by-cpf 
  [request]
  (let [cpf (get-in  request [:path-params :cpf])]
    {:status (if (empty? (client-handler/get-users-by-cpf cpf)) 404 200)
     :headers {"Content-type" "text/json"}
     :body  (client-handler/get-users-by-cpf cpf)}))



































