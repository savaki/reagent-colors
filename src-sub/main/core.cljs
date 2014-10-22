(ns main.core
  (:require [reagent.core :as reagent :refer [atom]]
            [main.pubnub :as pubnub]
            [ajax.core :as ajax]))

(def color (atom "#ffffff"))

(defn receive-color [new-color]
  (reset! color new-color))

(defn app-view []
  [:div.main {:style {:background-color @color}} [:div.container "hello world"]])

(defn app-boot []
  (println "booting application")
  (let [publish-key "pub-c-0929351a-e2cf-4d06-8f41-2e4b682e7490"
        subscribe-key "sub-c-493cca24-59fb-11e4-a91d-02ee2ddab7fe"]
    (pubnub/initialize publish-key subscribe-key receive-color)))

(def app-view-with-callback
  (with-meta app-view
    {:component-did-mount #(app-boot)}))

(reagent/render-component [app-view-with-callback] (.getElementById js/document "app"))


