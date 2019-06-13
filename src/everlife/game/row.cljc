(ns everlife.game.row
  (:require [clojure.set :as set]
            [everlife.game.cell :as cell]
            [everlife.game.helpers :as helpers]))

(defn build-related-rows [state row-indx]
  [(or (state (helpers/endless-index (- row-indx 1))) #{})
   (state row-indx)
   (or (state (helpers/endless-index (+ row-indx 1))) #{})])

(defn changeable-cells [related-rows]
  (let [related-indexes (reduce #(set/union %1 %2)
                                #{}
                                related-rows)]
    (reduce helpers/build-surrounding-indexes
            #{}
            related-indexes)))

(defn survive-cycle [state row-indx]
  (let [related-rows (build-related-rows state row-indx)]
    (reduce #(if (cell/survives? related-rows %2)
               (conj %1 %2)
               %1)
            #{}
            (changeable-cells related-rows))))
