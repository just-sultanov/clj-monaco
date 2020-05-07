(ns monaco.example
  (:require
    [clojure.string :as str]
    [reagent.dom :as dom]
    [re-frame.core :as rf]
    [monaco.core :as monaco]
    [monaco.monarch :as monarch]
    [monaco.helpers :as helpers]))

;;
;; Helper functions
;;

(defn get-value [input]
  (helpers/get-in input ["target" "value"]))



;;
;; Custom language
;;

(defn register! []
  (monarch/register {:id "custom"})

  (monarch/set-monarch-tokens-provider "custom"
    {:tokenizer {:root [[#"\[error.*" "custom-error"]
                        [#"\[notice.*" "custom-notice"]
                        [#"\[info.*" "custom-info"]
                        [#"\[[a-zA-Z 0-9:]+\]" "custom-date"]]}})

  (monarch/register-completion-item-provider "custom"
    {:provideCompletionItems (fn []
                               {:suggestions [{:label      "simpleText"
                                               :insertText "simpleText"
                                               :kind       (helpers/get-in monaco/monaco-editor ["CompletionItemKind" "Text"])}
                                              {:label           "testing"
                                               :insertText      "testing(${1:condition})"
                                               :insertTextRules (helpers/get-in monaco/monaco-editor ["CompletionItemKind" "Keyword"])
                                               :kind            (helpers/get-in monaco/monaco-editor ["CompletionItemInsertTextRule" "InsertAsSnippet"])}]})})

  (monaco/define-theme "custom"
    {:base    "vs"
     :inherit false
     :rules   [{:token "custom-info" :foreground "808080"}
               {:token "custom-error" :foreground "ff0000" :fontStyle "bold"}
               {:token "custom-notice" :foreground "ffa500"}
               {:token "custom-date" :foreground "008800"}]}))



;;
;; Events & subscriptions
;;

(rf/reg-event-db
  ::init
  (fn [_ _]
    {:languages ["clojure" "javascript" "custom"]
     :themes    {"vs"       "Light"
                 "vs-dark"  "Dark"
                 "hc-black" "High Contrast"
                 "custom"   "Custom"}
     :editor    {:width               "100%"
                 :height              "100%"
                 :value               "[Sun Mar 7 16:02:00 2004] [notice] Apache/1.3.29 (Unix) configured -- resuming normal operations\n[Sun Mar 7 16:02:00 2004] [info] Server built: Feb 27 2004 13:56:37\n[Sun Mar 7 16:02:00 2004] [notice] Accept mutex: sysvsem (Default: sysvsem)\n[Sun Mar 7 21:16:17 2004] [error] [client xx.xx.xx.xx] File does not exist: /home/httpd/twiki/view/Main/WebHome"
                 :defaultValue        ""
                 :language            "custom"
                 :theme               "custom"
                 :minimap             {:enabled true}
                 :autoIndent          true
                 :selectOnLineNumbers true
                 :roundedSelection    false
                 :readOnly            false
                 :cursorStyle         "line"
                 :automaticLayout     false
                 :editorDidMount      (fn [editor monaco] (monaco/focus editor))
                 :editorWillMount     (fn [monaco])
                 :onChange            (fn [new-value event] (rf/dispatch [::set-value new-value]))
                 :overrideServices    {}}}))

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
  ::editor
  (fn [db]
    (:editor db)))

(rf/reg-sub
  ::language
  :<- [::editor]
  (fn [editor]
    (:language editor)))

(rf/reg-sub
  ::theme
  :<- [::editor]
  (fn [editor]
    (:theme editor)))

(rf/reg-event-db
  ::set-theme
  (fn [db [_ theme]]
    (assoc-in db [:editor :theme] theme)))

(rf/reg-event-db
  ::set-language
  (fn [db [_ language]]
    (assoc-in db [:editor :language] language)))

(rf/reg-event-db
  ::set-value
  (fn [db [_ value]]
    (assoc-in db [:editor :value] value)))



;;
;; Components
;;

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
   [monaco/editor @(rf/subscribe [::editor])]])



;;
;; Application entry point
;;

(defn mount-root
  "Mount root component."
  {:dev/after-load true}
  []
  (rf/clear-subscription-cache!)
  (dom/render [root]
    (helpers/call js/document "getElementById" "root")))


(defn init
  "Monaco UI initializer."
  {:export true}
  []
  (register!)
  (rf/dispatch-sync [::init])
  (mount-root))
