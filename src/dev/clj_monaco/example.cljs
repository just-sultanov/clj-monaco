(ns clj-monaco.example
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]
    [clj-monaco.db :as db]
    [clj-monaco.core :as m]))

(defn root []
  (let [text     (rf/subscribe [::db/text])
        language (rf/subscribe [::db/language])]
    (fn []
      (r/create-class
        {:name                "root"
         :component-did-mount (fn [this]
                                (let [el (r/dom-node this)]
                                  (m/create-editor el {:value @text, :language @language})))
         :reagent-render      (fn []
                                [:div#editor {:style {:height "200px"}}])}))))


(defn mount-root
  "Mount root component."
  {:dev/after-load true}
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
