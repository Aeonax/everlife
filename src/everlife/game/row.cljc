(ns everlife.game.row
  (:require [clojure.set :as set]
            [everlife.game.cell :as cell]
            [everlife.game.helpers :as helpers]))

(defn build-related-rows [state row-indx]
  [(or (state (helpers/endless-index (- row-indx 1))) {})
   (state row-indx)
   (or (state (helpers/endless-index (+ row-indx 1))) {})])

(defn changeable-cells [related-rows]
  (let [related-indexes (reduce #(set/union %1 (set (keys %2)))
                                  #{}
                                  related-rows)]
      (reduce helpers/build-surrounding-indexes
              #{}
              related-indexes)))

(defn survive-cycle [state row-index]
  (let [related-rows (build-related-rows state row-index)]
    (reduce #(if (cell/survives? related-rows %2)
                 (assoc %1 %2 true)
                 (dissoc %1 %2))
              (get state row-index {})
              (changeable-cells related-rows))))
