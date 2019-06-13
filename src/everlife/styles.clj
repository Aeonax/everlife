(ns everlife.styles
  (:require [garden.def :refer [defstyles]]
            [everlife.styles-data :as data :refer [colors]]))

(defn int-to-px [integer]
  (str integer "px"))

(defstyles main
  [[:* {:box-sizing :border-box}]
   [:.board {:width :1250px}
    [:.gaming-space {:width (int-to-px data/desk-width)
                     :height (int-to-px data/desk-height)}
     [:.row {:flex-grow 1}
      [:.cell {:border (str "1px solid " (:main-black colors))
               :flex-grow 1}]
      [:.pickable {:cursor :pointer}
       [:&:hover {:border-color :yellow}]]]]]
   [:.background-main {:background-color :yellow}]
   [:.button {:border-radius :0.1875rem
              :transition "opacity 0.2s, box-shadow 0.2s"
              :padding "0.375rem 1rem"
              :text-decoration :none
              :border-bottom "0.125rem solid rgba(0,0,0,0.1)"
              :color :white
              :text-align :center
              :background-color (:main-green colors)
              :display :inline-block
              :cursor :pointer}]
   [:.ml-1 {:margin-left :1em}]
   [:.mt-2 {:margin-top :2em}]
   [:.mx-1 {:margin-left :1em
            :margin-right :1em}]
   [:.mx-3 {:margin-left :3em
            :margin-right :3em}]
   [:.my-2 {:margin-top :2em
            :margin-bottom :2em}]
   [:.flex {:display :flex}]
   [:.flex-row {:flex-direction :row}]
   [:.flex-column {:flex-direction :column}]
   [:.flex-center {:align-items :center}]])
