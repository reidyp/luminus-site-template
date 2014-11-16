(ns {{name}}.db.schema
  (:require [clojure.java.jdbc :as sql]))

(def db-spec
  {:subprotocol "postgresql"
   :subname "//localhost/{{sanitized}}"
   :user "user_name"
   :password "user_password"})

(defn- create-table-user []
  (sql/create-table-ddl :user_tbl
                        [:id "VARCHAR(20) PRIMARY KEY"]
                        [:first_name "VARCHAR(30)"]
                        [:last_name "VARCHAR(30)"]
                        [:email "VARCHAR(30)"]
                        [:admin :boolean]
                        [:last_login "TIME"]
                        [:is_active :boolean]
                        [:pass "VARCHAR(100)"]))

(defn create-all-tables []
  (create-table-user))
