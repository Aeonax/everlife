(ns everlife.core-test
  (:require [clojure.test :refer :all]
            [everlife.game.core :as core]))

(def infinite-state {7 #{2} 9 #{2} 8 #{2}})

(deftest infinite-test
  (testing "Cycle Iterates correct"
    (is (= (core/run-cycle {8 #{1 2 3}})
           infinite-state)))
  (testing "Several cycles goes ok"
    (-> infinite-state
        core/run-cycle
        core/run-cycle
        core/run-cycle
        core/run-cycle
        (= infinite-state))))

(def glider {1 #{2} 2 #{3} 3 #{1 2 3}})

(deftest glider-test
  (testing "Glider correctly flying"
    (is (= (-> glider
               core/run-cycle
               core/run-cycle
               core/run-cycle
               core/run-cycle)
           (zipmap (map inc (keys glider))
                   (map (fn [row] (set (map #(inc %) row))) (vals glider)))))))
