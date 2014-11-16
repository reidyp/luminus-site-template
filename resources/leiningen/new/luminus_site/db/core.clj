(ns {{name}}.db.core
  (:require [{{name}}.db.schema :refer [db-spec]]
            [clojure.java.jdbc :as sql]))

(defn create-user [id-pass]
  (sql/insert! db-spec :user_tbl id-pass))

(defn update-user [id first-name last-name email]
  (sql/update! db-spec :user_tbl
               {:first_name first-name
                :last_name last-name
                :email email}
               ["id = ?" id]))

(defn get-user [id]
  (first (sql/query db-spec ["SELECT * FROM user_tbl WHERE id = ? LIMIT 1" id])))
