(ns leiningen.new.luminus-site
  (:use leiningen.new.template-parser)
  (:require [leiningen.core.main :as main]
            [leiningen.new.templates :refer [renderer name-to-path ->files year]])
  (:import java.io.File
           java.util.regex.Matcher))

(defn sanitized-resource-path [proj-name path]
  (.replaceAll
    (str proj-name "/resources/" (apply str path))
    "/"
    (Matcher/quoteReplacement File/separator)))

(def render (renderer "luminus-site"))

(defn luminus-site
  "FIXME: write documentation"
  [proj-name]
  (let [data {:name      proj-name
              :sanitized (name-to-path proj-name)
              :year      (year)}]
    (main/info "Generating fresh 'lein new' luminus-site project.")
    (->files data
             [".gitignore" (render "gitignore")]
             ["project.clj" (render "project.clj" data)]
             ["README.md" (render "README.md" data)]
             "logs"
             ["dev/user.clj" (render "user.clj" data)]
             ["src/{{sanitized}}/core.clj" (render "core.clj" data)]
             ["src/{{sanitized}}/db/core.clj" (render "db/core.clj" data)]
             ["src/{{sanitized}}/db/schema.clj" (render "db/schema.clj" data)]
             ["src/{{sanitized}}/handler.clj" (render "handler.clj" data)]
             ["src/{{sanitized}}/layout.clj" (render "layout.clj" data)]
             ["src/{{sanitized}}/middleware.clj" (render "middleware.clj" data)]
             ["src/{{sanitized}}/routes/auth.clj" (render "routes/auth.clj" data)]
             ["src/{{sanitized}}/routes/home.clj" (render "routes/home.clj" data)]
             ["src/{{sanitized}}/systems.clj" (render "systems.clj" data)]
             ["src/{{sanitized}}/util.clj" (render "util.clj" data)]
             ["resources/log4j.properties" (render "log4j.properties")]
             ["resources/public/css/screen.css" (render "css/screen.css")]
             ["resources/public/fonts/glyphicons-halflings-regular.eot" (render "fonts/glyphicons-halflings-regular.eot")]
             ["resources/public/fonts/glyphicons-halflings-regular.svg" (render "fonts/glyphicons-halflings-regular.svg")]
             ["resources/public/fonts/glyphicons-halflings-regular.ttf" (render "fonts/glyphicons-halflings-regular.ttf")]
             ["resources/public/fonts/glyphicons-halflings-regular.woff" (render "fonts/glyphicons-halflings-regular.woff")]
             "resources/public/js"
             "resources/public/img"
             ["resources/public/md/docs.md" (render "md/docs.md" data)]
             ["resources/templates/about.html" (render "templates/about.html" data)]
             ["resources/templates/base.html" (render "templates/base.html" data)]
             ["resources/templates/home.html" (render "templates/home.html" data)]
             ["resources/templates/menu.html" (render "templates/menu.html")]
             ["resources/templates/profile.html" (render "templates/profile.html")]
             ["resources/templates/registration.html" (render "templates/registration.html")])
    (rewrite-template-tags (sanitized-resource-path proj-name "/templates/"))))
