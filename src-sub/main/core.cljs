(ns main.core
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :as ajax]))

(def color (atom "#ffffff"))

(def bubbles-state (atom []))

(def step (atom -1))

(def channel-name "reagent-colors")

(def pubnub-state (atom nil))

(defn send-message [message]
  (println "sending =>" message)
  (.publish @pubnub-state (clj->js {:channel channel-name,
                                    :message message})))

(defn initialize [publish-key subscribe-key receive-message-fn]
  (println "initializing pubnub")
  (reset! pubnub-state (.init js/PUBNUB (clj->js {:publish_key publish-key
                                                  :subscribe_key subscribe-key})))

  (println "subscribing to reagent-colors channel")
  (.subscribe @pubnub-state (clj->js {:channel channel-name,
                                      :connect #(println "Connected to" channel-name "channel")
                                      :message (fn [m] (receive-message-fn (js->clj m)))})))

;------ bubble manipulation ------------------------------------------------

(defn reverse-step []
  (if (= @step 1)
    (reset! step -1)
    (reset! step 1)))

(defn bubble-move [bubble]
  {:color (get bubble :color)
   :pos (+ (get bubble :pos) @step)})

;------ messaging ----------------------------------------------------------

(defn receive-color [new-color]
  (swap! bubbles-state #(cons {:color new-color :pos 90} %))
  (println "bubbles =>" @bubbles-state)
  (reset! color new-color))

;------ view ---------------------------------------------------------------

(defn bubble-view [bubble]
  (let [color (get bubble :color)
        pos (get bubble :pos)]
    (if (and (> pos 0) (<= pos 90))
      [:div.bubble {:style {:margin-left (str pos "%")
                            :background-color color}}])))

(defn bubbles-view []
  [:div.bubbles (for [bubble @bubbles-state]
                  [bubble-view bubble])])

(defn app-view []
  [:div.main {:style {:background-color @color}} [:div.container [bubbles-view]]
   [:div.container [:div#send-color {:on-click #(reverse-step)} "reverse"]]])

;------ infra --------------------------------------------------------------

(defn tick []
  (swap! bubbles-state (fn [x] (map bubble-move x)))
  (js/setTimeout #(tick) 40))

(defn app-boot []
  (tick)
  (println "booting application")
  (let [publish-key "pub-c-0929351a-e2cf-4d06-8f41-2e4b682e7490"
        subscribe-key "sub-c-493cca24-59fb-11e4-a91d-02ee2ddab7fe"]
    (initialize publish-key subscribe-key receive-color)))

(def app-view-with-callback
  (with-meta app-view
    {:component-did-mount #(app-boot)}))

(reagent/render-component [app-view-with-callback] (.getElementById js/document "app"))


