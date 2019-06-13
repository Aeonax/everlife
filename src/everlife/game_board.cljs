(ns everlife.game-board
  (:require [rum.core :as rum]
            [everlife.game.core :as game]
            [everlife.game.helpers :as game-helpers]))

(defn run-cycle [state]
  (let [game (:game @state)
        current-state (or (:current-state game)
                          (:initial-state game))]
    (swap! state assoc-in [:game :current-state]
           (game/run-cycle current-state))))

(defn activate-cell [row-state index]
  (console.log (clj->js @row-state))
  (if-let [row @row-state]
    (swap! row-state conj index)
    (reset! row-state #{index}))
  (console.log (clj->js @row-state)))

(defn cell [row-state index playing?]
  (let [on-click (when-not playing?
                   #(activate-cell row-state index))
        active? (contains? (or @row-state #{}) index)
        classes [(when-not playing?
                   :pickable)
                 (when active?
                   :background-main)]]
    (console.log (clj->js @row-state) index active?)
    [:.cell {:class classes
             :key (str "cell-" index)
             :onClick on-click}]))

(rum/defc Row < rum/reactive [row-state playing? cells-count]
  [:.row
   ;; (console.log "Rerendering")
   (let [row (rum/react row-state)]
     (reduce #(conj %1 (cell row-state %2 playing?))
             []
             (range (+ cells-count 1))))])

(defn gaming-space [state playing? cells-count]
  [:.gaming-space
   (reduce #(conj %1
                  (Row (rum/cursor-in state [:game :current-state %2])
                       playing? cells-count))
           []
           (range (+ cells-count 1)))])

(rum/defc Inputs [state playing? cells-count]
  [:div
   (if playing?
     [:div
      [:.button {:on-click #() #_(pause-game state)} "Pause"]
      [:.button {:on-click #() #_(reset-game state)} "Reset Game"]]
     [:div
      [:.button {:on-click #(run-cycle state)} "Run Cycle"]
      [:.button {:on-click #() #_(start-game)} "Start Game"]])])

(rum/defc Board < rum/reactive [state]
  (let [playing? (rum/react (rum/cursor-in state [:game :playing]))
        cells-count (rum/react game-helpers/cells)]
    [:.board
     (gaming-space state playing? cells-count)
     (Inputs state playing? cells-count)]))

;;; OR with canvas?=))
