(ns everlife.game-ui.actions
  (:require [everlife.game.core :as game]))

(defn run-cycle [state]
  (let [game (:game @state)
        current-state (:current-state game)]
    (swap! state assoc-in [:game :current-state]
           (game/run-cycle current-state))))

(defn loop-game [state]
  (let [options (-> @state :game :options)]
    (when (and (:movie? options)
               (:playing? options))
      (run-cycle state)
      (js/setTimeout #(loop-game state)
                     (or (:delay options)
                         1000)))))

(defn toogle-game [state mode]
  (swap! state assoc-in [:game :options :playing?] mode))

(defn start-game [state]
  (swap! state assoc-in [:game :initial-state] (-> @state :game :current-state))
  (toogle-game state true)
  (loop-game state))

(defn pause-game [state]
  (toogle-game state nil))

(defn reset-game
  ([state] (reset-game state (-> @state :game :initial-state)))
  ([state field]
   (toogle-game state nil)
   (swap! state assoc-in [:game :current-state] field)))

(defn random-board [state]
  (swap! state assoc-in [:game :current-state] (game/build-random-state)))
