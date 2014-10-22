(ns main.pubnub
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :as ajax]))

(enable-console-print!)

(def channel-name "reagent-colors")

(def pubnub-state (atom nil))

(defn send-message [message]
  (println "sending =>" message)
  (.publish @pubnub-state (clj->js {:channel channel-name,
                                    :message {"rgb" message}})))

(defn initialize [publish-key subscribe-key receive-message-fn]
  (println "initializing pubnub")
  (reset! pubnub-state (.init js/PUBNUB (clj->js {:publish_key publish-key
                                                  :subscribe_key subscribe-key})))

  (println "subscribing to reagent-colors channel")
  (.subscribe @pubnub-state (clj->js {:channel channel-name,
                                      :connect #(println "Connected to" channel-name "channel")
                                      :message (fn [m] (receive-message-fn (js->clj m)))})))

