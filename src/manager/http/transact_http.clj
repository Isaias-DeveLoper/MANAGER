(ns manager.http.transact-http
  (:require [manager.business.transact-business :as transact-business]))

(defn new-transaction
  [{:keys [json-params]}]
   (let [cpf (:cpf json-params)
         receive (:destinatary json-params)
         value_send (:value json-params)
         reason (:reason json-params)
         status (transact-business/check-exists-account cpf receive value_send reason)]
     {:status (if (not (= status "account not found"))
                (if (= status "successful transaction!") 200 401)
                404)
      :headers {"Content-type" "text/plain"}
      :body (str status)}))



