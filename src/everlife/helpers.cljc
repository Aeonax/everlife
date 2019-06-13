(ns everlife.helpers)

;; (def current-state (atom []))

(def cells (atom 9))

(defn- real-index [indx]
  (println (str "executing real-index: " indx))
  (let [d-cells @cells]
    (cond
      (> indx d-cells) 0
      (< indx 0) d-cells
      :else indx)))

(def momoized-real-index (memoize real-index))

(defn build-surrounding-indexes
  ([indx] (build-surrounding-indexes #{} indx))
  ([set-storage indx]
   (conj set-storage
         (momoized-real-index (- indx 1))
         indx
         (momoized-real-index (+ indx 1)))))
