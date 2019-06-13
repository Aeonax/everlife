(ns everlife.cell
  (:require [everlife.helpers :as helpers]
            [clojure.set :as set]))

(println "here")

(defn count-cells [indexes-range rows]
  (reduce (fn [result row]
            (+ (count (set/select #(contains? indexes-range %) row))
               result))
          0
          rows))

(defn survives? [related-rows cell-indx]
  (let [all-cells-count (count-cells (helpers/build-surrounding-indexes cell-indx)
                                     related-rows)
        cell-alive? (contains? (related-rows 1) cell-indx)
        cells-count (if cell-alive?
                      (- all-cells-count 1)
                      all-cells-count)]
    (when (or (and cell-alive?
                   (= cells-count 2))
              (= cells-count 3))
      true)))
