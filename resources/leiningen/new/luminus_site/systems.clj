(ns {{name}}.systems
  (:require [system.core :refer [defsystem]]
            [environ.core :refer [env]]
            [{{name}}.handler :refer [new-web-server get-handler init destroy]]
            (system.components [repl-server :refer [new-repl-server]])))

(defsystem dev-system
  [:http-server (new-web-server (Integer/parseInt (env :http-port))
                                (get-handler) init destroy)])

(defsystem prod-system
  [:http-server (new-web-server (Integer/parseInt (env :http-port))
                                (get-handler) init destroy)
   :repl-server (new-repl-server (Integer/parseInt (env :repl-port)))])
