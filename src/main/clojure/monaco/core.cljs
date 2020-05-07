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
    ["monaco" :as monaco]))

;;;;
;; Constants and references
;;;;

;; NOTE: https://microsoft.github.io/monaco-editor/api/index.html

(def ^{:added "0.0.4"}
  MonacoEditor
  "A Monaco Editor reference."
  (j/get monaco :editor))


(def ^{:added "0.0.4"}
  MonacoLanguages
  "A Monaco Languages reference."
  (j/get monaco :languages))



;;;;
;; Monaco Editor
;;;;

;; NOTE: https://microsoft.github.io/monaco-editor/api/modules/monaco.editor.html

(defn create
  "Create a new editor under `dom-element`. `dom-element` should be empty (not contain other dom nodes).
  The editor will read the size of `dom-element`.

  Params:
    * `dom-element` - `HTMLElement`
    * `options`     - `IStandaloneEditorConstructionOptions` (optional)
    * `override`    - `IEditorOverrideServices` (optional)

  Returns:
    * `IStandaloneCodeEditor`"
  {:added "0.0.4"}
  [dom-element options override]
  (j/invoke MonacoEditor :create dom-element options override))


(defn create-diff-editor
  "Create a new diff editor under `dom-element`. `dom-element` should be empty (not contain other dom nodes).
  The editor will read the size of `dom-element`.

  Params:
    * `dom-element` - `HTMLElement`
    * `options`     - `IDiffEditorConstructionOptions` (optional)
    * `override`    - `IEditorOverrideServices` (optional)

  Returns:
    * `IStandaloneDiffEditor`"
  {:added "0.0.4"}
  [dom-element options override]
  (j/invoke MonacoEditor :create-diff-editor dom-element options override))


(defn define-theme
  "Define a new theme or update an existing theme.

  Params:
    * `theme-name` - `String`
    * `theme-data` - `IStandaloneThemeData`

  Returns:
    * `nil`"
  {:added "0.0.4"}
  [theme-name theme-data]
  (j/invoke MonacoEditor :define-theme theme-name theme-data))


(defn set-model-language
  "Change the language for a model.

  Params:
    * `model`       - `ITextModel`
    * `language-id` - `String`

  Returns:
    * `nil`"
  {:added "0.0.4"}
  [model language-id]
  (j/invoke MonacoEditor :set-model-language model language-id))


(defn set-theme
  "Switches to a theme.

  Parameters
    * `theme-name` - `String`

  Returns:
    * `nil`"
  {:added "0.0.4"}
  [theme-name]
  (j/invoke MonacoEditor :set-theme theme-name))


(defn completion-item-kind
  "Returns an enum by the given type - `monaco.languages.CompletionItemKind.{ type }`

  Types:
    * `:method`
    * `:function`
    * `:constructor`
    * `...`
    * `:snippet`

  A full list of available types:
    * [link](https://github.com/microsoft/monaco-editor/blob/v0.20.0/monaco.d.ts#L5174)"
  {:added "0.0.4"}
  [type]
  (j/get-in* MonacoLanguages [:completion-item-kind type]))


(defn completion-item-insert-text-rule
  "Returns an enum by the given type - `monaco.languages.CompletionItemInsertTextRule.{ type }`

  Types:
    * `:keep-whitespace`
    * `:insert-as-snippet`

  A full list of available types:
    * [link](https://github.com/microsoft/monaco-editor/blob/v0.20.0/monaco.d.ts#L5226)"
  {:added "0.0.4"}
  [type]
  (j/get-in* MonacoLanguages [:completion-item-insert-text-rule type]))


(defn focus
  "Brings browser focus to the editor text.

  Params:
    * `editor` - `IStandaloneCodeEditor`

  Returns:
    * `nil`"
  {:added "0.0.4"}
  [editor]
  (j/invoke editor :focus))



;;;;
;; Monaco Languages
;;;;

;; NOTE: https://microsoft.github.io/monaco-editor/api/modules/monaco.languages.html

(defn get-encoded-language-id
  "Returns the encoded language id.

  Returns:
    * `number`"
  {:added "0.0.4"}
  [language-id]
  (j/invoke MonacoLanguages :get-encoded-language-id language-id))


(defn get-languages
  "Returns the information of all the registered languages.

  Returns:
    * `ILanguageExtensionPoint[]`"
  {:added "0.0.4"}
  []
  (j/invoke MonacoLanguages :get-languages))


(defn register
  "Registers information about a new language.

  Params:
    * `language` - `ILanguageExtensionPoint`

  Returns:
    * `nil`

  A full list of available properties:
    * [link](https://microsoft.github.io/monaco-editor/api/interfaces/monaco.languages.ilanguageextensionpoint.html)"
  {:added "0.0.4"}
  [language]
  (j/invoke MonacoLanguages :register language))


(defn set-monarch-tokens-provider
  "Sets the tokens provider for a language (monarch implementation).

  Params:
    * `language-id`  - `string`
    * `language-def` - `IMonarchLanguage` or `Thenable<IMonarchLanguage>`

  Returns:
    * `IDisposable`"
  {:added "0.0.4"}
  [language-id language-def]
  (j/invoke MonacoLanguages :set-monarch-tokens-provider language-id language-def))


(defn register-completion-item-provider
  "Registers a completion item provider (use by e.g. suggestions).

  Params:
    * `language-id`  - `string`
    * `provider`     - `CompletionItemProvider`

  Returns:
    * `IDisposable`"
  {:added "0.0.4"}
  [language-id provider]
  (j/invoke MonacoLanguages :register-completion-item-provider language-id provider))



;;;;
;; IDisposable
;;;;

;; NOTE: https://microsoft.github.io/monaco-editor/api/interfaces/monaco.idisposable.html

(defn dispose
  "Dispose the editor.

  Params:
    * `disposable` - `IDisposable`

  Returns:
    * `nil`"
  {:added "0.0.4"}
  [disposable]
  (j/invoke disposable :dispose))



;;;;
;; IStandaloneCodeEditor
;;;;

;; NOTE: https://microsoft.github.io/monaco-editor/api/interfaces/monaco.editor.istandalonecodeeditor.html

(defn get-value
  "Returns the value of the current model attached to this editor.

  Params:
    * `editor` - `IStandaloneCodeEditor`

  Returns:
    * `string`"
  {:added "0.0.4"}
  [editor]
  (j/invoke editor :get-value))


(defn get-model
  "Returns the current model attached to this editor.

  Params:
    * `editor` - `IStandaloneCodeEditor`

  Returns:
    * `ITextModel` or `nil`"
  {:added "0.0.4"}
  [editor]
  (j/invoke editor :get-model))


(defn on-did-change-model
  "An event emitted when the model of this editor has changed (e.g. editor.setModel()).

  Params:
    * `editor`   - `IStandaloneCodeEditor`
    * `listener` - `IModelChangedEvent`

  Returns:
    * `IDisposable`"
  {:added "0.0.4"}
  [editor listener]
  (j/invoke editor :on-did-change-model listener))


(defn on-did-change-model-content
  "An event emitted when the content of the current model has changed.

  Params:
    * `editor`   - `IStandaloneCodeEditor`
    * `listener` - `IModelContentChangedEvent`

  Returns:
    * `IDisposable`"
  {:added "0.0.4"}
  [editor listener]
  (j/invoke editor :on-did-change-model-content listener))


(defn layout
  "Instructs the editor to remeasure its container. This method should be called when the container
  of the editor gets resized. If a dimension is passed in, the passed in value will be used.

  Params:
    * `editor` - `IStandaloneCodeEditor`

  Returns:
    * `nil`"
  {:added "0.0.4"}
  [editor]
  (j/invoke editor :layout))


(defn push-undo-stop
  "Push an `undo stop` in the undo-redo stack.

  Params:
    * `editor` - `IStandaloneCodeEditor`

  Returns:
    * `boolean`"
  {:added "0.0.4"}
  [editor]
  (j/invoke editor :push-undo-stop))


(defn update-options
  "Update the editor's options after the editor has been created.

  Params:
    * `editor`  - `IStandaloneCodeEditor`
    * `options` - `IEditorOptions` or `IGlobalEditorOptions`

  Returns:
    * `nil`"
  {:added "0.0.4"}
  [editor options]
  (j/invoke editor :update-options options))



;;;;
;; ITextModel
;;;;

;; NOTE: https://microsoft.github.io/monaco-editor/api/interfaces/monaco.editor.itextmodel.html

(defn get-model-value
  "Returns the text stored in this model.

  Params:
    * `model` - `ITextModel`

  Returns:
    * `string`"
  {:added "0.0.4"}
  [model]
  (j/invoke model :get-value))


(defn get-full-model-range
  "Get a range covering the entire model.

  Params:
    * `model` - `ITextModel`

  Returns:
    * `Range`"
  {:added "0.0.4"}
  [model]
  (j/invoke model :get-full-model-range))


(defn push-edit-operations
  "Push edit operations, basically editing the model. This is the preferred way of editing the model.
  The edit operations will land on the undo stack.

  Params:
    * `model`                 - `ITextModel`
    * `before-cursor-state`   - `Selection[]`. The cursor state before the edit operations. This cursor state will be returned when undo or redo are invoked.
    * `edit-operations`       - `IIdentifiedSingleEditOperation[]`. The edit operations.
    * `cursor-state-computer` - `ICursorStateComputer`. A callback that can compute the resulting cursors state after the edit operations have been executed.

  Returns:
    * `Selection[]` or `nil`"
  {:added "0.0.4"}
  [model before-cursor-state edit-operations cursor-state-computer]
  (j/invoke model :push-edit-operations before-cursor-state edit-operations cursor-state-computer))



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
                               (m/focus editor))

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
                                     (f editor monaco))
                                   (j/set this :__subscription
                                     (on-did-change-model-content editor
                                       (fn [event]
                                         (when-not (j/get this :__prevent-trigger-on-change-event)
                                           (when-some [f (:on-change props)]
                                             (f (get-value editor) event))))))))

        editor-will-mount      (fn [this _]
                                 (let [props (r/props this)]
                                   (when-some [f (:editor-will-mount props)]
                                     (or (f monaco) (j/with-camel-case {})))))

        component-did-mount    (fn [this]
                                 (when-some [ref @*ref]
                                   (let [props  (r/props this)
                                         opts   (-> config
                                                  (merge props)
                                                  (assoc :editor-will-mount (partial editor-will-mount this)))
                                         editor (create ref opts {})]
                                     (j/set this :editor editor)
                                     (editor-did-mount this editor))))

        component-did-update   (fn [this old-argv]
                                 (let [editor      (j/get this :editor)
                                       old-props   (second old-argv)
                                       props       (r/props this)
                                       model       (get-model editor)
                                       model-value (get-model-value model)
                                       {:keys [value theme language options width height]} props]

                                   (when (and value (not= value model-value))
                                     (j/set this :__prevent-trigger-on-change-event true)
                                     (push-undo-stop editor)
                                     (push-edit-operations model [] [{:text value, :range (get-full-model-range model)}] nil)
                                     (push-undo-stop editor)
                                     (j/set this :__prevent-trigger-on-change-event false))

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
