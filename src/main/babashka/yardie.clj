(ns yardie 
  (:require [babashka.fs :as fs]))
(require '[babashka.fs :as fs] '[clojure.string :as str] '[babashka.process :as proc])

(defn sanity [] {:ok true})

(defn instance_name [] (System/getenv "YARDIE_INSTANCE_NAME"))
(if (nil? (instance_name)) (throw (Exception. "set YARDIE_INSTANCE_NAME")) ())

(defn host_folder [] (System/getenv "YARDIE_HOST_FOLDER_PATH"))
;; (if (nil? (host_folder)) (throw (Exception. "set YARDIE_HOST_FOLDER_PATH")) ())

(defn build [] (proc/shell "docker" "build" "." "--tag" (yardie/instance_name)))
(defn stop [] (proc/shell "docker" "stop" (yardie/instance_name)))
(defn connect [] (proc/shell "docker" "exec" "-it" (yardie/instance_name) "/bin/bash"))

(defn command [name server]
  (if
   (nil? (host_folder))
    [
     "docker" "run"
     "--name" name
     "--rm" "-it" name
     ]
    [
     "docker" "run"
     "--mount" (str/join "," ["type=bind" (str/join "=" ["source"  (str/join "/" [(fs/cwd) server])]) "target=/srv" ])
     "--name" name
     "--rm" "-it" name
     ]
    ) 
  )
(defn command_local [] 
  (
   let
   [name (yardie/instance_name)
    server (yardie/host_folder)]
    (command name server))
  )
(defn run []
  (let [command (command_local)]
    (prn command)
      (apply proc/shell command)
    ))
