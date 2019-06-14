(ns everlife.game-ui.core
  (:require [rum.core :as rum]
            [everlife.game-ui.form :as form]
            [everlife.game-ui.board :as board]
            [everlife.game.helpers :as game-helpers]))

(rum/defc Page < rum/reactive [state]
  (let [cells-count (rum/react game-helpers/cells)
        options (assoc (or (rum/react (rum/cursor-in state [:game :options]))
                           {})
                       :cells-count cells-count)]
    (console.log "Rerendreing board " (clj->js options) (clj->js cells-count))
    [:.board
     (when (and cells-count
                (pos? cells-count))
       (board/gaming-space state options))
     (form/Inputs state options)]))
