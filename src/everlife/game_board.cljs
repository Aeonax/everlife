(ns everlife.game-board
  (:require [rum.core :as rum]
            [everlife.game.core :as game]
            [everlife.styles-data :as styles]
            [everlife.game.helpers :as game-helpers]))

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

(defn activate-cell [row-state index]
  (if-let [row @row-state]
    (swap! row-state (if (contains? row index)
                       disj conj)
           index)
    (reset! row-state #{index})))

(defn board-unlocked? [options]
  (or (not (:playing? options))
      (:interfere? options)))

(defn cell [row-state index options]
  (let [unlocked? (board-unlocked? options)
        alive? (contains? (or @row-state #{}) index)
        classes [(when unlocked?
                   :pickable)
                 (when alive?
                   :background-main)]]
    [:.cell {:class classes
             :key (str "cell-" index)
             :onClick (when unlocked?
                        #(activate-cell row-state index))}]))

(rum/defc Row < rum/reactive [row-state options]
  [:.row.flex.flex-row
   (let [row (rum/react row-state)]
     (console.log "Rerendering row" (clj->js row))
     (map #(cell row-state % options)
          (range (+ (:cells-count options) 1))))])

(defn gaming-space [state options]
  [:.gaming-space.flex.flex-column
   (map #(Row (rum/cursor-in state [:game :current-state %])
                 options)
           (range (+ (:cells-count options) 1)))])

(rum/defcs CellsInput <
  ;; Costyl' need to fix it-_- will-mount with reseting local is scary-_-
  (rum/local (+ @game-helpers/cells 1) :cells-count)
  [c-state state]
  (let [local (:cells-count c-state)]
    [:.input-group.mx-1
     [:.label "Cells Amount"]
     [:input {:onChange #(reset! local (.. % -target -value))
              :onBlur #(game-helpers/swap-cells (.. % -target -value))
              :value @local}]]))

(rum/defc Inputs [state options]
  [:.mt-2.flex.flex-row.flex-center
   [:.button {:on-click #(run-cycle state)} "Run Cycle"]
   (if (:playing? options)
     [:div
      [:.button.mx-1 {:on-click #(pause-game state)} "Pause"]
      #_[:.button {:on-click #() #_(reset-game state)} "Reset Game"]]
     [:.flex.flex-row.flex-center
      (CellsInput state)
      [:.input-group.flex.flex-row.flex-center
       [:.label "Switch on Video mode"]
       [:input {:on-click #(swap! state update-in [:game :options :movie?] not)
                :type :checkbox
                :checked (:movie? options)}]]
      [:.input-group
       [:.label "Allow interactions during game"]
       [:input {:on-click #(swap! state update-in [:game :options :interfere?] not)
                :type :checkbox
                :checked (:interfere? options)}]]
      [:.button.ml-1 {:on-click #(start-game state)} "Start Game"]])])

(rum/defc Board < rum/reactive [state]
  (let [cells-count (rum/react game-helpers/cells)
        options (assoc (or (rum/react (rum/cursor-in state [:game :options]))
                           {})
                       :cells-count cells-count)]
    [:.board
     (when (and cells-count
                (pos? cells-count))
       (gaming-space state options))
     (Inputs state options)]))
