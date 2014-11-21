(defproject {{name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.danielsz/system "0.1.3"]
                 [org.clojure/java.jdbc "0.3.6"]
                 [org.postgresql/postgresql "9.3-1102-jdbc41"]
                 [http-kit "2.1.18"]
                 [lib-noir "0.9.4"]
                 [com.taoensso/timbre "3.3.1"]
                 [log4j "1.2.17" :exclusions [javax.mail/mail
                                              javax.jms/jms
                                              com.sun.jdmk/jmxtools
                                              com.sun.jmx/jmxri]]
                 [com.taoensso/tower "3.0.2"]
                 [noir-exception "0.2.2"]
                 [prone "0.6.0"]
                 [selmer "0.7.2"]
                 [markdown-clj "0.9.55" :exclusions [com.keminglabs/cljx]]
                 [im.chit/cronj "1.4.2"]
                 [mvxcvi/puget "0.6.4"]
                 [environ "1.0.0"]]
  :jvm-opts ["-server"]
  :plugins [[lein-environ "1.0.0"]
            [lein-ancient "0.5.5"]]
  :profiles {:uberjar {:omit-source true, :env {:prod true}, :aot :all},
             :prod {:env {:http-port "8080"
                          :repl-port "8000"}
                    :dependencies  [[org.clojure/tools.nrepl "0.2.5"]]},
             :dev {:env {:dev true
                         :http-port "3000"
                         :repl-port "8000"}
                   :source-paths ["dev"]
                   :dependencies [[ring-mock "0.1.5"]
                                  [ring/ring-devel "1.3.1"]
                                  [pjstadig/humane-test-output "0.6.0"]],
                   :injections [(require 'pjstadig.humane-test-output)
                                (pjstadig.humane-test-output/activate!)]}}
  :main {{name}}.core
  :min-lein-version "2.0.0")
