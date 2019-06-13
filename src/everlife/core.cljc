(ns everlife.core
  (:require [everlife.row :as row]
            [everlife.helpers :as helpers]))

(defn run-cycle [state]
  (time
   (reduce #(let [new-row (row/cycle state %2)]
              (if (empty? new-row)
                %1
                (assoc %1 %2 new-row)))
           {}
           (range (+ @helpers/cells 1)))))
