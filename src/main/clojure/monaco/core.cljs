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
    [monaco.api.refs :refer [Monaco]]
    [monaco.api.editor :as me]
    [monaco.api.editor.text-model :as mtm]))

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
     :editor-did-mount       (fn [editor monaco]
                               (js/console.log :editor-did-mount)
                               (monaco.api.editor/focus editor))

     :editor-will-mount      (fn [monaco]
                               (js/console.log :editor-will-mount))

     :on-change              (fn [new-value event]
                               (js/console.log :on-change)
                               (rf/dispatch [::set-value new-value]))}

  A full list of available properties:
    * [link](https://microsoft.github.io/monaco-editor/api/interfaces/monaco.editor.istandaloneeditorconstructionoptions.html)"
  {:added "0.0.4"}
  [config]
  (let [*ref                   (atom nil)

        assign-ref             (fn [component]
                                 (reset! *ref component))

        editor-did-mount       (fn [this editor]
                                 (let [props (r/props this)]
                                   (when-some [f (:editor-did-mount props)]
                                     (f editor Monaco))
                                   (j/set this :__subscription
                                     (me/on-did-change-model-content editor
                                       (fn [event]
                                         (when-not (j/get this :__prevent-trigger-on-change-event)
                                           (when-some [f (:on-change props)]
                                             (f (me/get-value editor) event))))))))

        editor-will-mount      (fn [this _]
                                 (let [props (r/props this)]
                                   (when-some [f (:editor-will-mount props)]
                                     (or (f Monaco) (j/with-camel-case {})))))

        component-did-mount    (fn [this]
                                 (when-some [ref @*ref]
                                   (let [props  (r/props this)
                                         opts   (-> config
                                                  (merge props)
                                                  (assoc :editor-will-mount (partial editor-will-mount this)))
                                         editor (me/create ref opts {})]
                                     (j/set this :editor editor)
                                     (editor-did-mount this editor))))

        component-did-update   (fn [this old-argv]
                                 (let [editor      (j/get this :editor)
                                       old-props   (second old-argv)
                                       props       (r/props this)
                                       model       (me/get-model editor)
                                       model-value (mtm/get-value model)
                                       {:keys [value theme language options width height]} props]

                                   (when (and value (not= value model-value))
                                     (j/set this :__prevent-trigger-on-change-event true)
                                     (me/push-undo-stop editor)
                                     (mtm/push-edit-operations model [] [{:text value, :range (mtm/get-full-model-range model)}] nil)
                                     (me/push-undo-stop editor)
                                     (j/set this :__prevent-trigger-on-change-event false))

                                   (when (not= language (:language old-props))
                                     (me/set-model-language model language))

                                   (when (not= theme (:theme old-props))
                                     (me/set-theme theme))

                                   (when (not= options (:options old-props))
                                     (me/update-options editor options))

                                   (when (or (not= width (:width old-props))
                                           (not= height (:height old-props)))
                                     (me/layout editor))))

        component-will-unmount (fn [this]
                                 (when-some [editor (j/get this :editor)]
                                   (me/dispose editor)

                                   (when-some [model (me/get-model editor)]
                                     (me/dispose model)))

                                 (when-some [sub (j/get this :__subscription)]
                                   (me/dispose sub)))

        render                 (fn [_]
                                 [:div.monaco-editor-wrapper {:ref assign-ref}])]
    (fn [_]
      (r/create-class
        {:display-name           "monaco-editor"
         :component-did-mount    component-did-mount
         :component-did-update   component-did-update
         :component-will-unmount component-will-unmount
         :render                 render}))))
