(ns {{name}}.handler
  (:require [compojure.core :refer [defroutes]]
            [{{name}}.routes.home :refer [home-routes]]
            [{{name}}.middleware :refer [load-middleware]]
            [{{name}}.session-manager :as session-manager]
            [noir.response :refer [redirect]]
            [noir.util.middleware :refer [app-handler]]
            [ring.middleware.defaults :refer [site-defaults]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [compojure.route :as route]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.rotor :as rotor]
            [selmer.parser :as parser]
            [environ.core :refer [env]]
            [cronj.core :as cronj]
            [{{name}}.routes.auth :refer [auth-routes]]
            [{{name}}.db.schema :as schema]
            [com.stuartsierra.component :as component]
            [org.httpkit.server :refer [run-server]]))

(defroutes
  base-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(defn init
  []
  (timbre/set-config!
   [:appenders :rotor]
   {:min-level :info,
    :enabled? true,
    :async? false,
    :max-message-per-msecs nil,
    :fn rotor/appender-fn})
  (timbre/set-config!
   [:shared-appender-config :rotor]
   {:path "logs/{{sanitized}}.log", :max-size (* 512 1024), :backlog 10})
  (if (env :dev) (parser/cache-off!))
  (cronj/start! session-manager/cleanup-job)
  (timbre/info
   "
-=[ {{name}} started successfully at port"
   (env :http-port)
   (if (env :dev) "using the development profile" "")
   "]=-"))

(defn destroy
  "destroy will be called when your application
  shuts down, put any clean up code here"
  []
  (timbre/info "{{name}} is shutting down...")
  (cronj/shutdown! session-manager/cleanup-job)
  (timbre/info "shutdown complete!"))

(def session-defaults
  {:timeout (* 60 30), :timeout-response (redirect "/")})

(defn- mk-defaults
  "set to true to enable XSS protection"
  [xss-protection?]
  (-> site-defaults
      (update-in [:session] merge session-defaults)
      (assoc-in [:security :anti-forgery] xss-protection?)))

(def app
  (app-handler
   [auth-routes home-routes base-routes]
   :middleware
   (load-middleware)
   :ring-defaults
   (mk-defaults false)
   :access-rules
   []
   :formats
   [:json-kw :edn :transit-json]))

(defn get-handler []
  ;; #'app expands to (var app) so that when we reload our code,
  ;; the server is forced to re-resolve the symbol in the var
  ;; rather than having its own copy. When the root binding
  ;; changes, the server picks it up without having to restart.
  (-> #'app
      ; Makes static assets in $PROJECT_DIR/resources/public/ available.
      (wrap-file "resources")
      ; Content-Type, Content-Length, and Last Modified headers for files in body
      (wrap-file-info)))

(defrecord WebServer [port handler stop-fn init-fn destroy-fn]
  component/Lifecycle
  (start [component]
    (let [stop-fn (run-server handler {:port port})]
      (when init-fn
        (init-fn))
      (assoc component :stop-fn stop-fn)))
  (stop [component]
    (when destroy-fn
      (destroy-fn))
    (when stop-fn
      (stop-fn)
      component)))

(defn new-web-server
  ([port handler]
   (map->WebServer {:port port :handler handler
                    :init-fn nil :destroy-fn nil}))
  ([port handler init-fn destroy-fn]
   (map->WebServer {:port port :handler handler
                    :init-fn init-fn
                    :destroy-fn destroy-fn})))
