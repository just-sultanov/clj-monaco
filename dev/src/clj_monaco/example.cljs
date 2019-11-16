(ns ^:figwheel-hooks
  clj-monaco.example
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]
    [cljs-bean.core :as b]
    [clj-monaco.db :as db]
    [clj-monaco.core :as m]))

(defn root []
  (r/create-class
    {:name                "root"
     :component-did-mount (fn [this]
                            (let [el (r/dom-node this)]
                              (.create (m/editor) el (b/->js {:value    "(println \"Hello, world!\")"
                                                              :language "clojure"}))))
     :reagent-render      (fn []
                            [:div#editor {:style {:height "200px"}}])}))


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
