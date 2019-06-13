(ns everlife.game.helpers)

;; (def current-state (atom []))

(defonce cells (atom 9))

(defn swap-cells [input-value]
  (let [value (- (int input-value) 1)]
    (when-not (= @cells value)
      (reset! cells value))))

(defn real-index [indx cells-count]
  (println (str "executing real-index: " indx))
  (let [d-cells @cells]
    (cond
      (> indx d-cells) 0
      (< indx 0) d-cells
      :else indx)))

(def memoized-real-index (memoize real-index))

(defn endless-index [index]
  (memoized-real-index index @cells))

(defn build-surrounding-indexes
  ([indx] (build-surrounding-indexes #{} indx))
  ([set-storage indx]
   (conj set-storage
         (endless-index (- indx 1))
         indx
         (endless-index (+ indx 1)))))
