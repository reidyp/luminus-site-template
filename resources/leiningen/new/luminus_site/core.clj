(ns {{name}}.core
  (:require [reloaded.repl :refer [system init start stop go reset]]
            [{{name}}.systems :refer [prod-system]])
  (:gen-class))

(defn -main []
  (reloaded.repl/set-init! prod-system)
  (go))
