(ns everlife.game-ui.board
  (:require [rum.core :as rum]))

(defn activate-cell [row-state index]
  (if-let [row @row-state]
    (if (get row index)
      (swap! row-state dissoc index)
      (swap! row-state assoc index true))
    (reset! row-state {index true})))

(defn board-unlocked? [options]
  (or (not (:playing? options))
      (:interfere? options)))

(defn cell [row-state alive? cell-index options]
  (let [unlocked? (board-unlocked? options)
        classes [(when unlocked?
                   :pickable)
                 (when alive?
                   :background-main)]]
    [:.cell {:class classes
             :key (str "cell-" cell-index)
             :onClick (when unlocked?
                        #(activate-cell row-state cell-index))}]))

(rum/defc Row < rum/reactive
  [row-state options]
  (let [row (rum/react row-state)]
    [:.row.flex.flex-row
     (map #(cell row-state (get row %) % options)
          (range (+ (:cells-count options) 1)))]))

(defn gaming-space [state options]
  [:.gaming-space.flex.flex-column
   (map #(Row (rum/cursor-in state [:game :current-state %])
              options)
        (range (+ (:cells-count options) 1)))])
