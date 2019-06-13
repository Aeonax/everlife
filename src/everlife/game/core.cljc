(ns everlife.game.core
  (:require [everlife.game.row :as row]
            [everlife.game.helpers :as helpers]))

(defn run-cycle [state]
  (time
   (reduce #(let [new-row (row/cycle state %2)]
              (if (empty? new-row)
                %1
                (assoc %1 %2 new-row)))
           {}
           (range (+ @helpers/cells 1)))))
