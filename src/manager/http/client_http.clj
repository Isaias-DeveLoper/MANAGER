(ns manager.http.client-http
  (:require [manager.business.client-business :as client-business]))

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



















