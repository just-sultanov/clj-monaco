(ns monaco.core
  (:require
    [reagent.core :as r]
    [cljs-bean.core :as b]
    [applied-science.js-interop :as j]))

(def monaco js/monaco)
(def monaco-editor (j/get monaco :editor))
(def monaco-languages (j/get monaco :languages))



;;
;; Monaco Editor
;;

(defn create [dom-element options override]
  (j/call monaco-editor :create dom-element (b/->js options) (b/->js override)))

(defn define-theme [theme-name theme-data]
  (j/call monaco-editor :defineTheme theme-name (b/->js theme-data)))

(defn focus [editor]
  (j/call editor :focus))

(defn set-model-language [model language-id]
  (j/call monaco-editor :setModelLanguage model language-id))

(defn set-theme [theme-name]
  (j/call monaco-editor :setTheme theme-name))



;;
;; IDisposable
;;

(defn dispose [disposable]
  (j/call disposable :dispose))



;;
;; IStandaloneCodeEditor
;;

(defn get-value [editor]
  (j/call editor :getValue))

(defn get-model [editor]
  (j/call editor :getModel))

(defn on-did-change-model [editor listener]
  (j/call editor :onDidChangeModel listener))

(defn on-did-change-model-content [editor listener]
  (j/call editor :onDidChangeModelContent listener))

(defn layout [editor]
  (j/call editor :layout))

(defn push-undo-stop [editor]
  (j/call editor :pushUndoStop))

(defn update-options [editor options]
  (j/call editor :updateOptions options))



;;
;; ITextModel
;;

(defn get-model-value [model]
  (j/call model :getValue))

(defn get-full-model-range [model]
  (j/call model :getFullModelRange))

(defn push-edit-operations [model before-cursor-state edit-operations]
  (j/call model :pushEditOperations (b/->js before-cursor-state) (b/->js edit-operations)))



;;
;; Components
;;

(defn editor [config]
  (let [*ref                   (atom nil)

        assign-ref             (fn [component]
                                 (reset! *ref component))

        editor-did-mount       (fn [this editor]
                                 (let [props (r/props this)]
                                   (when-some [f (:editorDidMount props)]
                                     (f editor monaco))
                                   (j/assoc! this :__subscription
                                     (on-did-change-model-content editor
                                       (fn [event]
                                         (when-not (j/get this :__preventTriggerChangeEvent)
                                           (when-some [f (:onChange props)]
                                             (f (get-value editor) event))))))))

        editor-will-mount      (fn [this _]
                                 (let [props (r/props this)]
                                   (when-some [f (:editorWillMount props)]
                                     (or (f monaco) (b/->js {})))))

        component-did-mount    (fn [this]
                                 (when-some [ref @*ref]
                                   (let [props  (r/props this)
                                         opts   (-> config (merge props) (assoc :editorWillMount (partial editor-will-mount this)))
                                         editor (create ref opts {})]
                                     (j/assoc! this :editor editor)
                                     (editor-did-mount this editor))))

        component-did-update   (fn [this old-argv]
                                 (let [editor      (j/get this :editor)
                                       old-props   (second old-argv)
                                       props       (r/props this)
                                       model       (get-model editor)
                                       model-value (get-model-value model)
                                       {:keys [value theme language options width height]} props]

                                   (when (and value (not= value model-value))
                                     (j/assoc! this :__preventTriggerChangeEvent true)
                                     (push-undo-stop editor)
                                     (push-edit-operations model [] [{:text value, :range (get-full-model-range model)}])
                                     (push-undo-stop editor)
                                     (j/assoc! this :__preventTriggerChangeEvent false))

                                   (when (not= language (:language old-props))
                                     (set-model-language model language))

                                   (when (not= theme (:theme old-props))
                                     (set-theme theme))

                                   (when (not= options (:options old-props))
                                     (update-options editor options))

                                   (when (or (not= width (:width old-props))
                                           (not= height (:height old-props)))
                                     (layout editor))))

        component-will-unmount (fn [this]
                                 (when-some [editor (j/get this :editor)]
                                   (dispose editor)

                                   (when-some [model (get-model editor)]
                                     (dispose model)))

                                 (when-some [sub (j/get this :__subscription)]
                                   (dispose sub)))

        render                 (fn [_]
                                 [:div.monaco-editor-wrapper {:ref assign-ref}])]
    (fn [_]
      (r/create-class
        {:display-name           "monaco-editor"
         :component-did-mount    component-did-mount
         :component-did-update   component-did-update
         :component-will-unmount component-will-unmount
         :render                 render}))))
