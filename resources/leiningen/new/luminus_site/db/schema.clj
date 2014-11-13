(ns {{name}}.db.schema)

(def db-spec
  {:subprotocol "postgresql"
   :subname "//localhost/{{sanitized}}"
   :user "user_name"
   :password "user_password"})
