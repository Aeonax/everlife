(ns everlife.game.core
  (:require [everlife.game.row :as row]
            [everlife.game.helpers :as helpers]))

(defn run-cycle [state]
  (time
   (reduce #(let [new-row (row/survive-cycle state %2)]
              (if (empty? new-row)
                (dissoc %1 %2)
                (assoc %1 %2 new-row)))
           state
           (range (+ @helpers/cells 1)))))

(defn build-random-state []
  (let [cells-range (range (+ @helpers/cells 1))]
    (reduce
     (fn [state row-index]
       (assoc state row-index
              (reduce
               #(if (pos? (rand-int 2))
                  (assoc %1 %2 true)
                  %1)
               {}
               cells-range)))
     {}
     cells-range)))
