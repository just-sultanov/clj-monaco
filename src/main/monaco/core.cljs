(ns monaco.core
  (:require
    [reagent.core :as r]
    [cljs-bean.core :as b]
    [applied-science.js-interop :as j]))

(defn square [x]
  (* x x))


(def monaco js/monaco)

(defn editor [config]
  (let [*ref                   (atom nil)

        assign-ref             (fn [component]
                                 (reset! *ref component))

        editor-did-mount       (fn [this editor]
                                 (let [props (r/props this)]
                                   (when-some [f (:editorDidMount props)]
                                     (f editor monaco))
                                   (j/assoc! this :__subscription
                                     (j/call editor :onDidChangeModelContent
                                       (fn [event]
                                         (when-not (j/get this :__preventTriggerChangeEvent)
                                           (when-some [f (:onChange props)]
                                             (f (j/call editor :getValue) event))))))))

        editor-will-mount      (fn [this _]
                                 (let [props (r/props this)]
                                   (when-some [f (:editorWillMount props)]
                                     (or (f monaco) (b/->js {})))))

        component-did-mount    (fn [this]
                                 (when-some [ref @*ref]
                                   (let [props  (r/props this)
                                         opts   (-> config (merge props) (assoc :editorWillMount (partial editor-will-mount this)))
                                         editor (j/call-in monaco [:editor :create] ref (b/->js opts))]
                                     (j/assoc! this :editor editor)
                                     (editor-did-mount this editor))))

        component-did-update   (fn [this old-argv]
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
                                     (j/call-in monaco [:editor :setModelLanguage] model language))

                                   (when (not= theme (:theme old-props))
                                     (j/call-in monaco [:editor :setTheme] theme))

                                   (when (not= options (:options old-props))
                                     (j/call editor :updateOptions options))

                                   (when (or (not= width (:width old-props))
                                           (not= height (:height old-props)))
                                     (j/call editor :layout))))

        component-will-unmount (fn [this]
                                 (when-some [editor (j/get this :editor)]
                                   (j/call editor :dispose)

                                   (when-some [model (j/call editor :getModel)]
                                     (j/call model :dispose)))

                                 (when-some [sub (j/get this :__subscription)]
                                   (j/call sub :dispose)))

        render                 (fn [_]
                                 [:div.editor-wrapper {:ref assign-ref}])]
    (fn [_]
      (r/create-class
        {:display-name           "monaco-editor"
         :component-did-mount    component-did-mount
         :component-did-update   component-did-update
         :component-will-unmount component-will-unmount
         :render                 render}))))
