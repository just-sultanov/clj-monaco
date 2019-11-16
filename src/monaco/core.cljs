(ns ^:figwheel-hooks
  monaco.core
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]
    [monaco.db :as db]))

(defn square [x]
  (* x x))


(defn root []
  [:div
   [:h1 "Hello world!"]])


(defn mount-root
  "Mount root component."
  {:after-load true}
  []
  (rf/clear-subscription-cache!)
  (r/render [root]
            (.getElementById js/document "root")))


(defn init
  "Monaco UI initializer."
  {:export true}
  []
  (rf/dispatch-sync [::db/init])
  (mount-root))
