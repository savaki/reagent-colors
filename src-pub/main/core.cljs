(ns main.core
  (:require [reagent.core :as reagent :refer [atom]]
            [main.pubnub :as pubnub]
            [cemerick.pprng :as rng]
            [ajax.core :as ajax]))

(enable-console-print!)

(def color (atom [0 0 0]))

;--- different color -----------------------------------------------------------------------------

(def rng (rng/rng))

(defn random-color-value []
  (rng/int rng 256))

(defn different-color []
  (reset! color [(random-color-value) (random-color-value) (random-color-value)])
  (println "new color =>" @color))

;--- messaging -----------------------------------------------------------------------------------

(defn receive-message [message]
  (println "receiving =>" message))

(defn send-color []
  (pubnub/send-message @color))

;--- view ----------------------------------------------------------------------------------------

(defn buttons-view []
  [:div.buttons [:div#send-color {:on-click #(send-color)} "send color"]
   [:div#send-color {:on-click #(different-color)} "different color"]])

(defn explain-view []
  [:div.pub-explain [:p "You're sending colors to " [:a {:href "sub.html" :target "frp-sub"} "here"]]])

(defn app-view []
  [:div.main [buttons-view]
   [explain-view]])

(defn app-boot []
  (println "booting application")
  (let [publish-key "pub-c-0929351a-e2cf-4d06-8f41-2e4b682e7490"
        subscribe-key "sub-c-493cca24-59fb-11e4-a91d-02ee2ddab7fe"]
    (pubnub/initialize publish-key subscribe-key receive-message)))

(def app-view-with-callback
  (with-meta app-view
    {:component-did-mount #(app-boot)}))

(reagent/render-component [app-view-with-callback] (.getElementById js/document "app"))


