(ns duct-migrations-auto-cfg.module
  (:refer-clojure :exclude [load-file])
  (:require
    [clojure.string  :as string]
    [clojure.pprint :refer [pprint]]
    [clojure.java.io :as io]
    [duct.core       :as duct]
    [duct.core.merge :as merge]
    [integrant.core  :as ig]
    [ragtime.jdbc    :as ragtime-jdbc]))

(defn- get-project-ns
  "Get project namespace from module configuration or from :duct.core/project-ns"
  [config options]
  (:project-ns options (:duct.core/project-ns config)))

(defn- name-to-path
  "Used to change namespace symbol into path where migrations folder should be inside"
  [sym]
  (-> sym name (string/replace "-" "_") (string/replace "." "/")))

(defn default-migrations-path
  "Default migrations folder path"
  [config options]
  (if-let [project-ns (get-project-ns config options)]
    (str (name-to-path project-ns) "/migrations")
    (str "migrations")))

(defn migrator-config [migrations]
  {:duct.migrator/ragtime {:migrations (vec migrations)}})

(defmethod ig/init-key :duct-migrations-auto-cfg/module [_ options]
  {:req #{}
   :fn (fn [config]
         (let [migrations-folder-path (:migrations-path options
                                                        (default-migrations-path config
                                                                                 options))
               migrations  (ragtime-jdbc/load-resources migrations-folder-path)]
           (duct/merge-configs config (migrator-config migrations))))})
