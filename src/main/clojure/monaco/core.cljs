;;;;
;; Copyright © 2019-2020 Ilshat Sultanov. All rights reserved.
;; The use and distribution terms for this software are covered by the license which
;; can be found in the file license at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by the terms
;; of this license. You must not remove this notice, or any other, from this software.
;;;;

(ns monaco.core
  "A ClojureScript library for the Monaco Editor."
  (:require
    [reagent.core :as r]
    [monaco.js-interop :as j]
    [monaco.api.editor :as monaco.editor]
    [monaco.api.editor.text-model :as monaco.editor.text-model]))

;;;;
;; Components
;;;;

(defn editor
  "A Monaco Editor component.

  Params:
    * `config` - `IStandaloneEditorConstructionOptions`

  Example:
    {:width                  \"100%\"
     :height                 \"100%\"
     :value                  \"(+ 1 2 3)\"
     :default-value          \"\"
     :language               \"clojure\"
     :theme                  \"vs-dark\"
     :minimap                {:enabled true}
     :auto-indent            true
     :select-on-line-numbers true
     :rounded-selection      false
     :read-only              false
     :cursor-style           \"line\"
     :automatic-layout       false
     :editor-did-mount       (fn [editor] (monaco.api.editor/focus editor))
     :on-change              (fn [editor new-value event] (re-frame.core/dispatch [::set-value new-value]))}

  Full list of available properties:
    * [link](https://microsoft.github.io/monaco-editor/api/interfaces/monaco.editor.istandaloneeditorconstructionoptions.html)"
  {:added "0.0.4"}
  [config]
  (let [*ref                   (atom nil)

        assign-ref             (fn [component]
                                 (reset! *ref component))

        editor-did-mount       (fn [this]
                                 (let [props  (r/props this)
                                       editor (j/get this :editor)]
                                   (when-let [f (:editor-did-mount props)]
                                     (f editor))
                                   (when-let [f (:on-change props)]
                                     (j/set this :__subscription
                                       (monaco.editor/on-did-change-model-content editor
                                         (fn [event]
                                           (when-not (j/get this :__prevent-trigger-on-change-event)
                                             (f editor (monaco.editor/get-value editor) event))))))))

        component-did-mount    (fn [this]
                                 (when-let [ref @*ref]
                                   (let [props  (r/props this)
                                         opts   (merge config props)
                                         editor (monaco.editor/create ref opts {})]
                                     (j/set this :editor editor)
                                     (editor-did-mount this))))

        component-did-update   (fn [this old-argv]
                                 (let [editor      (j/get this :editor)
                                       old-props   (second old-argv)
                                       props       (r/props this)
                                       model       (monaco.editor/get-model editor)
                                       model-value (monaco.editor.text-model/get-value model)
                                       {:keys [value theme language options width height]} props]

                                   (when (and value (not= value model-value))
                                     (let [range           (monaco.editor.text-model/get-full-model-range model)
                                           edit-operations [{:text value, :range range}]]
                                       (j/set this :__prevent-trigger-on-change-event true)
                                       (monaco.editor/push-undo-stop editor)
                                       (monaco.editor.text-model/push-edit-operations model [] edit-operations nil)
                                       (monaco.editor/push-undo-stop editor)
                                       (j/set this :__prevent-trigger-on-change-event false)))

                                   (when-not (= language (:language old-props))
                                     (monaco.editor/set-model-language model language))

                                   (when-not (= theme (:theme old-props))
                                     (monaco.editor/set-theme theme))

                                   (when-not (= options (:options old-props))
                                     (monaco.editor/update-options editor options))

                                   (when (or (not= width (:width old-props))
                                           (not= height (:height old-props)))
                                     (monaco.editor/layout editor))))

        component-will-unmount (fn [this]
                                 (when-let [editor (j/get this :editor)]
                                   (monaco.editor/dispose editor)

                                   (when-let [model (monaco.editor/get-model editor)]
                                     (monaco.editor/dispose model)))

                                 (when-let [sub (j/get this :__subscription)]
                                   (monaco.editor/dispose sub)))

        render                 (fn [_]
                                 [:div.monaco-editor__wrapper {:ref assign-ref}])]
    (fn [_]
      (r/create-class
        {:display-name           "monaco-editor"
         :component-did-mount    component-did-mount
         :component-did-update   component-did-update
         :component-will-unmount component-will-unmount
         :render                 render}))))
