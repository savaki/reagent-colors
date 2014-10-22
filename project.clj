(defproject CHANGE-ME-ME "0.1.0-SNAPSHOT"
  :description "CHANGE-ME"
  :url "https://CHANGE-ME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2280"]
                 [reagent "0.4.2"]
                 [cljs-ajax "0.2.6"]]

  :plugins [[lein-environ "0.5.0"]
            [lein-cljsbuild "1.0.3"]]

  :cljsbuild {:builds [{:id "dev-pub"
                        :source-paths ["src-pub"]
                        :compiler {:optimizations :none
                                   :output-to "public/dev-pub/app.js"
                                   :output-dir "public/dev-pub/"
                                   :pretty-print true
                                   :source-map true}}
                       {:id "prod-pub"
                        :source-paths ["src-pub"]
                        :compiler {:optimizations :advanced
                                   :output-to "public/js-pub/app.js"
                                   :output-dir "public/js-pub/"
                                   :pretty-print true}}
                       {:id "dev-sub"
                        :source-paths ["src-sub"]
                        :compiler {:optimizations :none
                                   :output-to "public/dev-sub/app.js"
                                   :output-dir "public/dev-sub/"
                                   :pretty-print true
                                   :source-map true}}
                       {:id "prod-sub"
                        :source-paths ["src-sub"]
                        :compiler {:optimizations :advanced
                                   :output-to "public/js-sub/app.js"
                                   :output-dir "public/js-sub/"
                                   :pretty-print true}}
                       ]}

  :min-lein-version "2.0.0")
