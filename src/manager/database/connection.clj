(ns manager.database.connection
  (:require [dotenv :as e]))

(def connection 
  {:dbtype (e/env :TYPE_BANK)
   :dbname (e/env :NAME_BANK)
   :user (e/env :USER_BANK)
   :password (e/env :PASS_BANK)})







