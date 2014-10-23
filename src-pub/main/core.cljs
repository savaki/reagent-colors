(ns main.core
  (:require [reagent.core :as reagent :refer [atom]]
            [main.pubnub :as pubnub]
            [ajax.core :as ajax]))

(enable-console-print!)

(def color (atom "#ffffff"))

;--- different color -----------------------------------------------------------------------------

(defn random [max]
  (.floor js/Math (+ (* (.random js/Math) max) 1)))

(defn random-hex-value []
  (let [a (+ (random 12) 4)
        b (random 16)]
    (str (.toString a 16) (.toString b 16))))

(defn random-hex-color []
  (str "#" (random-hex-value) (random-hex-value) (random-hex-value)))

(defn different-color []
  (reset! color (random-hex-color)))

;--- messaging -----------------------------------------------------------------------------------

(defn receive-color [message]
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
  [:div.main {:style {:background-color @color}} [:div.container [buttons-view]
                                                  [explain-view]]])

;--- infra ---------------------------------------------------------------------------------------

(defn app-boot []
  (println "booting application")
  (let [publish-key "pub-c-0929351a-e2cf-4d06-8f41-2e4b682e7490"
        subscribe-key "sub-c-493cca24-59fb-11e4-a91d-02ee2ddab7fe"]
    (pubnub/initialize publish-key subscribe-key receive-color)))

(def app-view-with-callback
  (with-meta app-view
    {:component-did-mount #(app-boot)}))

(reagent/render-component [app-view-with-callback] (.getElementById js/document "app"))


