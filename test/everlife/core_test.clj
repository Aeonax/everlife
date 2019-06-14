(ns everlife.core-test
  (:require [clojure.test :refer :all]
            [everlife.game.core :as core]))

(def infinite-state {7 {2 true} 9 {2 true} 8 {2 true}})

(deftest infinite-test
  (testing "Cycle Iterates correct"
    (is (= (core/run-cycle {8 {1 true 2 true 3 true}})
           infinite-state)))
  (testing "Several cycles goes ok"
    (-> infinite-state
        core/run-cycle
        core/run-cycle
        core/run-cycle
        core/run-cycle
        (= infinite-state))))

(def glider {1 {2 true} 2 {3 true} 3 {1 true 2 true 3 true}})

(deftest glider-test
  (testing "Glider correctly flying"
    (is (= (-> glider
               core/run-cycle
               core/run-cycle
               core/run-cycle
               core/run-cycle)
           ;; oh my.... it increases row and cell indexes by 1...
           (zipmap (map inc (keys glider))
                   (map (fn [row] (zipmap (map inc (keys row))
                                          (vals row)))
                        (vals glider)))))))
