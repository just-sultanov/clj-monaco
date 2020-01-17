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
(def monaco-editor (j/get js/monaco :editor))

(rf/reg-event-db
  ::init
  (fn [_ _]
    {:languages ["clojure" "javascript"]
     :themes    {"vs"       "Light"
                 "vs-dark"  "Dark"
                 "hc-black" "High Contrast"}
     :settings  {:width            "100%"
                 :height           "100%"
                 :value            ""
                 :defaultValue     ""
                 :language         "clojure"
                 :theme            "vs"
                 :minimap          {:enabled false}
                 :autoIndent       true
                 :options          {}
                 :editorDidMount   (fn [editor monaco] (j/call editor :focus))
                 :editorWillMount  (fn [monaco])
                 :onChange         (fn [new-value event] (rf/dispatch [::set-value new-value]))
                 :overrideServices {}}}))

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
  ::themes
  (fn [db]
    (sort-by val (:themes db))))

(rf/reg-sub
  ::settings
  (fn [db]
    (:settings db)))

(rf/reg-sub
  ::language
  :<- [::settings]
  (fn [settings]
    (:language settings)))

(rf/reg-sub
  ::theme
  :<- [::settings]
  (fn [settings]
    (:theme settings)))

(rf/reg-event-db
  ::set-theme
  (fn [db [_ theme]]
    (assoc-in db [:settings :theme] theme)))

(rf/reg-event-db
  ::set-language
  (fn [db [_ language]]
    (assoc-in db [:settings :language] language)))

(rf/reg-event-db
  ::set-value
  (fn [db [_ value]]
    (assoc-in db [:settings :value] value)))



;;
;; Components
;;

(defn editor-wrapper [settings]
  (let [*ref              (atom nil)

        assign-ref        (fn [component]
                            (reset! *ref component))

        editor-did-mount  (fn [this editor]
                            (let [props (r/props this)]
                              (when-some [f (:editorDidMount props)]
                                (f editor monaco))
                              (j/assoc! this :__subscription
                                (j/call editor :onDidChangeModelContent
                                  (fn [event]
                                    (when-not (j/get this :__preventTriggerChangeEvent)
                                      (when-some [f (:onChange props)]
                                        (f (j/call editor :getValue) event))))))))

        editor-will-mount (fn [this editor]
                            (let [props (r/props this)]
                              (when-some [f (:editorWillMount props)]
                                (or (f monaco) (b/->js {})))))

        did-mount         (fn [this]
                            (when-some [ref @*ref]
                              (let [props  (r/props this)
                                    opts   (-> settings (merge props) (assoc :editorWillMount (partial editor-will-mount this)))
                                    editor (j/call monaco-editor :create ref (b/->js opts))]
                                (j/assoc! this :editor editor)
                                (editor-did-mount this editor))))

        did-update        (fn [this old-argv]
                            (let [editor      (j/get this :editor)
                                  old-props   (second old-argv)
                                  props       (r/props this)
                                  model       (j/call editor :getModel)
                                  model-value (j/call model :getValue)
                                  {:keys [value theme language options width height]} props]

                              (when (and value (not= value model-value))
                                (j/assoc! this :__preventTriggerChangeEvent true)
                                (j/call editor :pushUndoStop)
                                (j/call model :pushEditOperations
                                  (b/->js [])
                                  (b/->js [{:range (j/call model :getFullModelRange)
                                            :text  value}]))
                                (j/call editor :pushUndoStop)
                                (j/assoc! this :__preventTriggerChangeEvent false))

                              (when (not= language (:language old-props))
                                (j/call monaco-editor :setModelLanguage model language))

                              (when (not= theme (:theme old-props))
                                (j/call monaco-editor :setTheme theme))

                              (when (not= options (:options old-props))
                                (j/call editor :updateOptions options))

                              (when (or (not= width (:width old-props))
                                      (not= height (:height old-props)))
                                (j/call editor :layout))))

        will-unmount      (fn [this]
                            (when-some [editor (j/get this :editor)]
                              (j/call editor :dispose)

                              (when-some [model (j/call editor :getModel)]
                                (j/call model :dispose)))

                            (when-some [sub (j/get this :__subscription)]
                              (j/call sub :dispose)))

        render            (fn [_]
                            [:div.editor-wrapper {:ref assign-ref}])]
    (fn [_]
      (r/create-class
        {:display-name           "monaco-editor"
         :component-did-mount    did-mount
         :component-did-update   did-update
         :component-will-unmount will-unmount
         :render                 render}))))

(defn select-theme []
  (let [theme  @(rf/subscribe [::theme])
        themes @(rf/subscribe [::themes])]
    [:div.mr-2
     [:span "Theme:"]
     (into [:select.form-select.mt-1.block
            {:value     theme
             :on-change #(rf/dispatch [::set-theme (get-value %)])}]
       (for [[k v] themes]
         ^{:key k}
         [:option {:value k} v]))]))

(defn select-language []
  (let [language  @(rf/subscribe [::language])
        languages @(rf/subscribe [::languages])]
    [:div.mr-2
     [:span "Language:"]
     (into [:select.form-select.mt-1.block
            {:value     language
             :on-change #(rf/dispatch [::set-language (get-value %)])}]
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
   [editor-wrapper @(rf/subscribe [::settings])]])



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
