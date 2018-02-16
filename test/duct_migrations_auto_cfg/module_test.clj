(ns duct-migrations-auto-cfg.module-test
  (:require
    [clojure.java.jdbc :as jdbc]
    [clojure.test :refer :all]
    [duct.core      :as duct]
    [integrant.core :as ig]
    [duct.logger    :as logger]
    [duct.module.sql]
    [duct-migrations-auto-cfg.main]))

(duct/load-hierarchy)

(def base-config
  {:duct-migrations-auto-cfg/module {}})

(deftest config-test
  (testing "fill in migrations from migrations folder"
    (is (= 6
           (-> base-config
               duct/prep
               :duct.migrator/ragtime
               :migrations
               keys
               count)))))

(defrecord TestLogger [logs]
  logger/Logger
  (-log [_ level ns-str file line id event data]
    (swap! logs conj [event data])))

(def logs (atom []))

;; fake logger initialization
;; we don't need whole logger subsystem
(defmethod ig/init-key :duct/logger [_ config] (->TestLogger logs))

(def system-config
  {::duct/environment :development
   :duct/logger (->TestLogger logs)
   :duct-migrations-auto-cfg/module {}
   :duct.module/sql {:database-url "jdbc:sqlite:"}})

(defn- find-tables [db-spec]
  (jdbc/query db-spec ["SELECT name FROM sqlite_master WHERE type='table'"]))

(deftest initialization-test
  (testing "default migrations path"
    (let [system (-> system-config
                    duct/prep
                    ig/init)]
      (testing "applied migraions count"
        (is (= 6
              (-> system
                  :duct.migrator/ragtime
                  keys
                  count))))

      (testing "applied migraions result (created tables)"
        (is (= (find-tables (-> system :duct.database.sql/hikaricp :spec))
              [{:name "ragtime_migrations"}
                {:name "foo"}
                {:name "bar"}
                {:name "baz"}
                {:name "quza"}
                {:name "quzb"}
                {:name "quxa"}
                {:name "quxb"}
                {:name "last_table"}])))

      (reset! logs [])
      (ig/halt! system)))

  (testing "namespaced migrations path"
    (let [system (-> (merge {:duct.core/project-ns 'duct-migrations-auto-cfg} system-config)
                     duct/prep
                     ig/init)]
      (testing "applied migraions count"
        (is (= 2
               (-> system
                   :duct.migrator/ragtime
                   keys
                   count))))

      (testing "applied migraions result (created tables)"
        (is (= (find-tables (-> system :duct.database.sql/hikaricp :spec))
               [{:name "ragtime_migrations"}
                {:name "foo"}
                {:name "bar"}])))

      (reset! logs [])
      (ig/halt! system)))

  (testing "custom migrations path"
    (let [system (-> (assoc-in system-config
                               [:duct-migrations-auto-cfg/module :migrations-path]
                               "my-migrations")
                     duct/prep
                     ig/init)]
      (testing "applied migraions count"
        (is (= 2
               (-> system
                   :duct.migrator/ragtime
                   keys
                   count))))

      (testing "applied migraions result (created tables)"
        (is (= (find-tables (-> system :duct.database.sql/hikaricp :spec))
               [{:name "ragtime_migrations"}
                {:name "foo"}
                {:name "bar"}])))

      (reset! logs [])
      (ig/halt! system))))
