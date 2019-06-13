(ns everlife.styles
  (:require [garden.def :refer [defstyles]]))

(defstyles main
  [[:.board {:width :1250px}
     [:.gaming-space {:width :1000px}
      [:.row {:display :flex
              :height :50px
              :flex-direction :row}
       [:.cell {:border "1px solid #b0e08f"
                :width :50px}]
       [:.pickable {:cursor :pointer}
        [:&:hover {:border-color :yellow}]]]]]
   [:.background-main {:background-color :yellow}]
   ])
