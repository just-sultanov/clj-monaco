;;;;
;; Copyright © 2019-2020 anywhere.ninja. All rights reserved.
;; The use and distribution terms for this software are covered by the license
;; which can be found in the file license at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by the
;; terms of this license.
;; You must not remove this notice, or any other, from this software.
;;;;

(ns monaco.api.editor
  "A Monaco Editor API."
  (:require
    [monaco.js-interop :as j]
    [monaco.api.refs :refer [MonacoEditor]]))

;;;;
;; Monaco Editor
;;;;

;; NOTE: https://microsoft.github.io/monaco-editor/api/modules/monaco.editor.html

;;;;
;; Monaco Editor functions
;;;;

;; - colorize
;; - colorizeElement
;; - colorizeModelLine


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


;; - createDiffNavigator
;; - createModel
;; - createWebWorker


(defn define-theme
  "Define a new theme or update an existing theme.

  Params:
    * `theme-name` - `string`
    * `theme-data` - `IStandaloneThemeData`

  Returns:
    * `nil`"
  {:added "0.0.4"}
  [theme-name theme-data]
  (j/invoke MonacoEditor :define-theme theme-name theme-data))


;; - getModel
;; - getModelMarkers
;; - getModels
;; - remeasureFonts
;; - setModelLanguage
;; - setModelMarkers
;; - setTheme
;; - tokenize


(defn set-model-language
  "Change the language for a model.

  Params:
    * `model`       - `ITextModel`
    * `language-id` - `string`

  Returns:
    * `nil`"
  {:added "0.0.4"}
  [model language-id]
  (j/invoke MonacoEditor :set-model-language model language-id))


(defn set-theme
  "Switches to a theme.

  Parameters
    * `theme-name` - `string`

  Returns:
    * `nil`"
  {:added "0.0.4"}
  [theme-name]
  (j/invoke MonacoEditor :set-theme theme-name))



;;;;
;; IStandaloneCodeEditor
;;;;

;; NOTE: https://microsoft.github.io/monaco-editor/api/interfaces/monaco.editor.istandalonecodeeditor.html

;;;;
;; IStandaloneCodeEditor events
;;;;

;; - onContextMenu
;; - onDidBlurEditorText
;; - onDidBlurEditorWidget
;; - onDidChangeConfiguration
;; - onDidChangeCursorPosition
;; - onDidChangeCursorSelection


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


;; - onDidChangeModelDecorations
;; - onDidChangeModelLanguage
;; - onDidChangeModelLanguageConfiguration
;; - onDidChangeModelOptions
;; - onDidContentSizeChange
;; - onDidDispose
;; - onDidFocusEditorText
;; - onDidFocusEditorWidget
;; - onDidLayoutChange
;; - onDidPaste
;; - onDidScrollChange
;; - onKeyDown
;; - onKeyUp
;; - onMouseDown
;; - onMouseLeave
;; - onMouseMove
;; - onMouseUp



;;;;
;; IStandaloneCodeEditor methods
;;;;

(defn add-action
  "Add action to the editor.

  Params:
    * `editor`     - `IStandaloneCodeEditor`
    * `descriptor` - `IActionDescriptor`

  Returns:
    * `IDisposable`

  Full information about the descriptor structure:
  * [link](https://microsoft.github.io/monaco-editor/api/interfaces/monaco.editor.iactiondescriptor.html)"
  {:added "0.0.8"}
  [editor descriptor]
  (j/invoke editor :add-action descriptor))


;; - addCommand
;; - addContentWidget
;; - addOverlayWidget
;; - applyFontInfo
;; - changeViewZones
;; - createContextKey
;; - deltaDecorations


(defn dispose
  "Dispose the editor.

  Params:
    * `editor` - `IStandaloneCodeEditor`

  Returns:
    * `nil`"
  {:added "0.0.4"}
  [editor]
  (j/invoke editor :dispose))


;; - executeCommand
;; - executeCommands
;; - executeEdits


(defn focus
  "Brings browser focus to the editor text.

  Params:
    * `editor` - `IStandaloneCodeEditor`

  Returns:
    * `nil`"
  {:added "0.0.4"}
  [editor]
  (j/invoke editor :focus))


;; - getAction
;; - getContainerDomNode
;; - getContentHeight
;; - getContentWidth
;; - getContribution
;; - getDomNode
;; - getEditorType
;; - getId
;; - getLayoutInfo
;; - getLineDecorations


(defn get-model
  "Returns the current model attached to this editor.

  Params:
    * `editor` - `IStandaloneCodeEditor`

  Returns:
    * `ITextModel` or `nil`"
  {:added "0.0.4"}
  [editor]
  (j/invoke editor :get-model))


;; - getOffsetForColumn
;; - getOption
;; - getOptions
;; - getPosition
;; - getRawOptions
;; - getScrollHeight
;; - getScrollLeft
;; - getScrollTop
;; - getScrollWidth
;; - getScrolledVisiblePosition
;; - getSelection
;; - getSelections
;; - getSupportedActions
;; - getTargetAtClientPoint
;; - getTopForLineNumber
;; - getTopForPosition


(defn get-value
  "Returns the value of the current model attached to this editor.

  Params:
    * `editor` - `IStandaloneCodeEditor`

  Returns:
    * `string`"
  {:added "0.0.4"}
  [editor]
  (j/invoke editor :get-value))


;; - getVisibleColumnFromPosition
;; - getVisibleRanges
;; - hasTextFocus
;; - hasWidgetFocus


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


;; - layoutContentWidget
;; - layoutOverlayWidget
;; - onDidCompositionEnd
;; - onDidCompositionStart


(defn push-undo-stop
  "Push an `undo stop` in the undo-redo stack.

  Params:
    * `editor` - `IStandaloneCodeEditor`

  Returns:
    * `boolean`"
  {:added "0.0.4"}
  [editor]
  (j/invoke editor :push-undo-stop))


;; - removeContentWidget
;; - removeOverlayWidget
;; - render
;; - restoreViewState
;; - revealLine
;; - revealLineInCenter
;; - revealLineInCenterIfOutsideViewport
;; - revealLines
;; - revealLinesInCenter
;; - revealLinesInCenterIfOutsideViewport
;; - revealPosition
;; - revealPositionInCenter
;; - revealPositionInCenterIfOutsideViewport
;; - revealRange
;; - revealRangeAtTop
;; - revealRangeInCenter
;; - revealRangeInCenterIfOutsideViewport
;; - saveViewState
;; - setModel
;; - setPosition
;; - setScrollLeft
;; - setScrollPosition
;; - setScrollTop
;; - setSelection
;; - setSelections
;; - setValue
;; - trigger


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
