(ns everlife.game-ui.board
  (:require [rum.core :as rum]))

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

(rum/defc Row < rum/reactive
  {:did-update
   (fn [state]
     (console.log "Rerendering row"
                  (clj->js (-> state :rum/args
                               first deref
                               clj->js)))
     state)}
  [row-state options]
  [:.row.flex.flex-row
   (let [row (rum/react row-state)]
     (map #(cell row-state % options)
          (range (+ (:cells-count options) 1))))])

(defn gaming-space [state options]
  [:.gaming-space.flex.flex-column
   (map #(Row (rum/cursor-in state [:game :current-state %])
                 options)
           (range (+ (:cells-count options) 1)))])
