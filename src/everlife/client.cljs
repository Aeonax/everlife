(ns everlife.client
  (:require [rum.core :as rum]
            [everlife.game-board :as game-board]))

(defonce state (atom {}))

(set! (.-state js/window) #(clj->js @state))

(rum/defc App []
  [:div
   [:.header "Everlife"]
   (game-board/Board state)])

(rum/mount (App) (js/document.getElementById "app"))
