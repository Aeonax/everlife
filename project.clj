(defproject everlife "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure         "1.8.0"]
                 [org.clojure/clojurescript   "1.10.520"]
                 [rum                         "0.11.3"]
                 [garden                      "1.3.2"]]

  :plugins [[lein-figwheel "0.5.18"]
            [lein-cljsbuild "1.1.7" :exclusions [[org.clojure/clojure]]]
            [lein-garden "0.2.6"]]

  :clean-targets ^{:protect false} ["resources/public/assets/" "target"]
  :main everlife.core

  :figwheel {:server-ip "0.0.0.0"
             :css-dirs ["resources/public/assets/"]
             :repl false}

  :cljsbuild {:builds
              {:main {:source-paths ["src"]
                      :figwheel {:on-jsload "everlife.client/on-js-reload"}
                      :compiler {:main everlife.client
                                 :asset-path "/assets/js"
                                 :output-to "resources/public/assets/js/everlife.js"
                                 :output-dir "resources/public/assets/js"
                                 :source-map-timestamp true}}
               :min {:source-paths ["src"]
                     :compiler {:output-to "resources/public/assets/js/everlife.js"
                                :main everlife.client
                                ;; :externs ["resources/externs.js"]
                                :optimizations :advanced
                                :pretty-print false}}}}

  :garden {:builds [{:id "screen"
                     :stylesheet everlife.styles/main
                     :source-paths ["src"]
                     :compiler {:output-to "resources/public/assets/css/everlife.css"
                                ;; Compress the output?
                                :optimizations :advanced
                                :pretty-print? false}}]}

  :aliases {"prod"        ["do"
                           ["clean"]
                           ["garden" "once"]
                           ["cljsbuild" "once" "min"]]
            "dev"         ["do"
                           ["garden" "once"]
                           ["figwheel"]]})
