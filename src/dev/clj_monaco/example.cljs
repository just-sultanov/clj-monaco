(ns clj-monaco.example
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]
    [clj-monaco.core :as m]))

(rf/reg-event-db
  ::init
  (fn [_ _]
    {:language "clojure"
     :theme    "vs"
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

(def default-props
  {:width            "100%",
   :height           "100%",
   :value            "(+ 1 2 3)",
   :defaultValue     "",
   :language         "clojure",
   :theme            "vs",
   :options          {},
   :overrideServices {},
   :editorDidMount   identity,
   :editorWillMount  identity,
   :onChange         identity})


(defn editor []
  (let [ref (atom nil)]
    (fn []
      (r/create-class
        {:component-did-mount
         (fn [this]
           (m/create-editor (r/dom-node this) default-props))

         :render
         (fn []
           [:div.editor-wrapper {:ref (fn [el]
                                        (reset! ref el))}])}))))


(defn root []
  [:div.m-6
   [:h1.text-gray-700 "Monaco Editor"]
   [:div.mt-6.mb-10.flex.justify-start.w-full
    [:div.mr-2
     [:span.text-gray-700 "Theme:"]
     [:select.form-select.mt-1.block
      [:option "Light"]
      [:option "Dark"]]]
    [:div.mr-2
     [:span.text-gray-700 "Language:"]
     [:select.form-select.mt-1.block
      [:option "Clojure"]]]]
   [editor]])


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
