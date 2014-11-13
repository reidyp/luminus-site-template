(ns leiningen.new.template-parser
  (:require [clojure.java.io :as io]))

(defn- replace-tags [filename]
  (spit filename
        (-> filename
          (slurp)
          (.replaceAll "#%" "{{")
          (.replaceAll "%#" "}}"))))

(defn rewrite-template-tags [path]
  (doseq [file (->> path
                    io/file
                    file-seq
                    (map #(.getName %))
                    (filter #(.endsWith % ".html")))]
    (replace-tags (str path file))))
