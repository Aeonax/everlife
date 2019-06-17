(ns everlife.game-ui.form
  (:require [rum.core :as rum]
            [everlife.ui :as ui]
            [everlife.game-ui.actions :as actions]
            [everlife.game.helpers :as game-helpers]))

(defn initial-inputs [state options]
  [:.flex.flex-column
   [:.flex.flex-column.mt-1
    (ui/LocalInput {:on-blur #(game-helpers/swap-cells %)
                    :default-value (+ (:cells-count options) 1)
                    :container-class ["mb-1"]
                    :label "Cells Amount"})
    (ui/LocalInput {:on-blur #(swap! state assoc-in [:game :options :delay]
                                     (* (js/parseFloat %) 1000))
                    :default-value (/ (or (:delay options) 1000)
                                      1000)
                    :label "Frame delay(s)"})]
       
   [:.flex.flex-column.my-1
    (ui/checkbox {:on-click #(swap! state update-in [:game :options :movie?] not)
                  :label "Switch on Video mode"
                  :container-class :mb-1
                  :value (:movie? options)})
    (ui/checkbox {:on-click #(swap! state update-in [:game :options :interfere?] not)
                  :label "Allow interactions during game"
                  :value (:interfere? options)})]
   [:.flex.flex-column
    (ui/button {:on-click #(actions/random-board state)
                :label "Randomize field"
                :class :mb-1})
    (ui/button {:on-click #(actions/start-game state)
                :label "Start Game"})]])

(defn inputs-during-the-game [state options]
  [:.flex.flex-column
   (ui/button {:on-click #(actions/pause-game state)
               :label "Pause"
               :class :my-1})
   (ui/button {:on-click #(actions/reset-game state)
               :label "Reset Game"})])

(defn static-inputs [state options]
  [:.flex.flex-column
   (ui/button {:on-click #(actions/run-cycle state)
               :class :my-1
               :label "Run Cycle"})
   (when (-> @state :game :initial-state)
     (ui/button {:on-click #(actions/reset-game state)
                 :label "Reset Desk"}))
   (ui/button {:on-click #(actions/reset-game state {})
               :class :mt-1
               :label "Clean Desk"})])

(rum/defc Inputs [state options]
  [:.inputs-column.ml-1
   (static-inputs state options)
   [:.flex.flex-column
    (if (:playing? options)
      (inputs-during-the-game state options)
      (initial-inputs state options))]])
