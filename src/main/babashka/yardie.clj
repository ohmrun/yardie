(ns yardie)
(require '[babashka.fs :as fs] '[clojure.string :as str] '[babashka.process :as proc])

(defn sanity [_] {:ok true})

(defn instance_name [] (System/getenv "YARDIE_INSTANCE_NAME"))
(defn build [_] (proc/shell "docker" "build" "." "--tag" (yardie/instance_name)))
(defn stop [_] (proc/shell "docker" "stop" (yardie/instance_name)))
(defn connect [_] (proc/shell "docker" "exec" "-it" (yardie/instance_name) "/bin/bash"))
(defn run [_] (let [name (yardie/instance_name)] (
                                              (proc/shell "docker" "run" "--name" name "--rm" "-it" name)
)))