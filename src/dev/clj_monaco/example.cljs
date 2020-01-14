(ns clj-monaco.example
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]
    [clj-monaco.core :as m]))

(rf/reg-event-db
  ::init
  (fn [_ _]
    {:language "clojure"
     :theme    "vs-dark"
     :text     "(or 1 2 3)"}))

(rf/reg-sub
  ::language
  (fn [db]
    (:language db)))

(rf/reg-sub
  ::theme
  (fn [db]
    (:theme db)))

(rf/reg-sub
  ::text
  (fn [db]
    (:text db)))


(defn root []
  (let [text     (rf/subscribe [::text])
        theme    (rf/subscribe [::theme])
        language (rf/subscribe [::language])]
    (fn []
      (r/create-class
        {:name                "root"
         :component-did-mount (fn [this]
                                (let [el (r/dom-node this)]
                                  (m/create-editor el {:value @text, :language @language, :theme @theme})))
         :reagent-render      (fn []
                                [:div#editor {:style {:height "100vh"}}])}))))


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
  (rf/dispatch-sync [::init])
  (mount-root))
