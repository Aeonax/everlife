(ns everlife.ui
  (:require [rum.core :as rum]))

(defn checkbox [{:keys [on-click value label container-class]}]
  [:.input-group.flex.flex-row.flex-center {:class container-class}
   [:input {:on-click on-click
            :type :checkbox
            :checked value}]
   (when label
     [:.label label])])

(defn button [{:keys [on-click label class]}]
  [:.button {:on-click on-click
             :class class}
   label])

(rum/defcs LocalInput < (rum/local nil :local)
  {:will-mount (fn [state]
                 (reset! (:local state) (-> state :rum/args last :default-value))
                 state)}
  [component-state {:keys [on-blur label container-class]}]
  (let [local (:local component-state)]
    ;; wtf :class have another behavior then first functions element
    [:.input-group.flex.flex-row {:class container-class}
     [:.label label]
     [:input.ml-1 {:onChange #(reset! local (.. % -target -value))
                   :onBlur #(on-blur (.. % -target -value))
                   :value @local}]]))
