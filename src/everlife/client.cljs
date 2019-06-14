(ns everlife.client
  (:require [rum.core :as rum]
            #_[everlife.game-board :as game-board]
            [everlife.game-ui.core :as game]))

(defonce state (atom {}))

(set! (.-state js/window) #(clj->js @state))

(rum/defc App []
  [:div
   [:.header "Everlife"]
   (game/Page state)])

(rum/mount (App) (js/document.getElementById "app"))
