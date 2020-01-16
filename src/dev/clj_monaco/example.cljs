(ns clj-monaco.example
  (:require
    [clojure.string :as str]
    [reagent.core :as r]
    [re-frame.core :as rf]
    [cljs-bean.core :as b]
    [applied-science.js-interop :as j]))

;;
;; Helper functions
;;

(defn get-value [input]
  (j/get-in input [:target :value]))



;;
;; Core functions
;;

(def monaco js/monaco)
(def monaco-editor (j/get js/monaco "editor"))

(rf/reg-event-db
  ::init
  (fn [_ _]
    {:language  "clojure"
     :languages ["clojure"]
     :theme     "vs"
     :text      "(or 1 2 3)"
     :themes    {"vs"      "Light"
                 "vs-dark" "Dark"
                 "hc-dark" "High Contrast"}}))

(rf/reg-sub
  ::languages
  (fn [db]
    (->> db
      :languages
      (reduce
        (fn [acc k]
          (assoc acc k (str/capitalize k)))
        {})
      (sort-by val))))

(rf/reg-sub
  ::language
  (fn [db]
    (:language db)))

(rf/reg-sub
  ::themes
  (fn [db]
    (sort-by val (:themes db))))

(rf/reg-sub
  ::theme
  (fn [db]
    (:theme db)))

(rf/reg-sub
  ::text
  (fn [db]
    (:text db)))

(rf/reg-event-db
  ::set-theme
  (fn [db [_ theme]]
    (assoc db :theme theme)))

(rf/reg-event-db
  ::set-language
  (fn [db [_ language]]
    (assoc db :language language)))

(defn noop [name & args]
  (js/console.log :name name :args args))

(def default-props
  {:width            "100%"
   :height           "100%"
   :value            "(+ 1 2 3)"
   :defaultValue     ""
   :language         "clojure"
   :theme            "vs"
   :options          {}
   :overrideServices {}
   :editorDidMount   (partial noop :editorDidMount)
   :editorWillMount  (partial noop :editorWillMount)
   :onChange         (partial noop :onChange)})


;;
;; Components
;;

(defn editor []
  (let [ref (atom nil)]
    (fn [props]
      (r/create-class
        {:component-did-mount
         (fn [this]
           (let [el (r/dom-node this)]
             (j/call monaco-editor :create el (b/->js (merge default-props props)))))

         :render
         (fn [_]
           [:div.editor-wrapper {:ref (fn [el]
                                        (reset! ref el))}])}))))

(defn select-theme []
  (let [themes @(rf/subscribe [::themes])]
    [:div.mr-2
     [:span "Theme:"]
     (into [:select.form-select.mt-1.block
            {:on-change #(rf/dispatch [::set-theme (get-value %)])}]
       (for [[k v] themes]
         ^{:key k}
         [:option {:value k} v]))]))

(defn select-language []
  (let [languages @(rf/subscribe [::languages])]
    [:div.mr-2
     [:span "Language:"]
     (into [:select.form-select.mt-1.block
            {:on-change #(rf/dispatch [::set-language (get-value %)])}]
       (for [[k v] languages]
         ^{:key k}
         [:option {:value k} v]))]))

(defn config []
  [:div.mt-6.mb-10.flex.flex-auto.w-full
   [select-theme]
   [select-language]])

(defn header []
  [:h1 "Monaco Editor"])

(defn root []
  [:div.m-6
   [header]
   [config]
   [editor]])



;;
;; Application entry point
;;

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
