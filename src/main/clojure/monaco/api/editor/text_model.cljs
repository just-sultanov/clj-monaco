;;;;
;; Copyright © 2019-2020 anywhere.ninja. All rights reserved.
;; The use and distribution terms for this software are covered by the license
;; which can be found in the file license at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by the
;; terms of this license.
;; You must not remove this notice, or any other, from this software.
;;;;

(ns monaco.api.editor.text-model
  "A Monaco Editor ITextModel API."
  (:require
    [monaco.js-interop :as j]))

;;;;
;; ITextModel
;;;;

;; NOTE: https://microsoft.github.io/monaco-editor/api/interfaces/monaco.editor.itextmodel.html

;;;;
;; ITextModel methods
;;;;

;; - applyEdits
;; - deltaDecorations
;; - detectIndentation
;; - dispose
;; - findMatches
;; - findNextMatch
;; - findPreviousMatch
;; - getAllDecorations
;; - getAlternativeVersionId
;; - getCharacterCountInRange
;; - getDecorationOptions
;; - getDecorationRange
;; - getDecorationsInRange
;; - getEOL


(defn get-full-model-range
  "Get a range covering the entire model.

  Params:
    * `model` - `ITextModel`

  Returns:
    * `Range`"
  {:added "0.0.4"}
  [model]
  (j/invoke model :get-full-model-range))


;; - getLineContent
;; - getLineCount
;; - getLineDecorations
;; - getLineFirstNonWhitespaceColumn
;; - getLineLastNonWhitespaceColumn
;; - getLineLength
;; - getLineMaxColumn
;; - getLineMinColumn
;; - getLinesContent
;; - getLinesDecorations
;; - getModeId
;; - getOffsetAt
;; - getOptions
;; - getOverviewRulerDecorations
;; - getPositionAt


(defn get-value
  "Returns the text stored in this model.

  Params:
    * `model` - `ITextModel`

  Returns:
    * `string`"
  {:added "0.0.4"}
  [model]
  (j/invoke model :get-value))


;; - getValueInRange
;; - getValueLength
;; - getValueLengthInRange
;; - getVersionId
;; - getWordAtPosition
;; - getWordUntilPosition
;; - isDisposed
;; - modifyPosition
;; - normalizeIndentation
;; - pushEOL


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


;; - pushStackElement
;; - setEOL


(defn set-value
  "Replace the entire text buffer value contained in this model.

  Params:
    * `model`     - `ITextModel`
    * `new-value` - `string`

  Returns:
    * `nil`"
  {:added "0.0.9"}
  [model new-value]
  (j/invoke model :set-value new-value))


(defn update-options
  "Change the options of this model.

  Params:
    * `model`       - `ITextModel`
    * `new-options` - `ITextModelUpdateOptions`

  Returns:
    * `nil`"
  {:added "0.0.9s"}
  [model new-options]
  (j/invoke model :update-options new-options))



;; - validatePosition
;; - validateRange
