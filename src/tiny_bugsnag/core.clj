(ns tiny-bugsnag.core
  (:import [com.bugsnag Client]))

(def ^:dynamic *bugsnag* nil)

(defonce api-key (atom nil))
(defonce stage (atom "production"))
(defonce notify-on-stages (atom ["production" "staging"]))

(defn setup! [key cur-stage]
  (reset! api-key key)
  (reset! stage cur-stage))

(defn client
  ^com.bugsnag.Client [& {:keys [context user]}]
  (let [cli (Client. @api-key)]
    (.setReleaseStage cli @stage)
    (.setNotifyReleaseStages cli (into-array String @notify-on-stages))
    (when context
      (.setContext cli context))
    (when user
      (.setUser cli (:id (str user)) (:email user) (:name user)))
    cli))

(defn notify
  [& {:keys [context user severity ^Throwable exception data]}]
  (when @api-key
    (let [cli (client :context context, :user user)
          metadata (com.bugsnag.MetaData.)]
      (doseq [[tab-name fields] data
              [key val] fields]
        (.addToTab metadata tab-name key val))
      (.addToTab metadata "Server" "Hostname" (.getHostName (java.net.InetAddress/getLocalHost)))
      (.addToTab metadata "JVM" "Heap - total" (.totalMemory (Runtime/getRuntime)))
      (.addToTab metadata "JVM" "Heap - max" (.maxMemory (Runtime/getRuntime)))
      (.addToTab metadata "JVM" "Heap - free" (.freeMemory (Runtime/getRuntime)))
      (if (#{"error" "warning" "info"} severity)
        (.notify cli exception severity metadata)
        (.notify cli exception "error" metadata)))))
