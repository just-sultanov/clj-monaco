;;;;
;; Copyright © 2019-2020 anywhere.ninja. All rights reserved.
;; The use and distribution terms for this software are covered by the license
;; which can be found in the file license at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by the
;; terms of this license.
;; You must not remove this notice, or any other, from this software.
;;;;

(ns monaco.api.languages
  "A Monaco Languages API."
  (:require
    [monaco.js-interop :as j]
    [monaco.api.refs :refer [MonacoLanguages]]))

;;;;
;; Monaco Languages
;;;;

;; NOTE: https://microsoft.github.io/monaco-editor/api/modules/monaco.languages.html

;;;;
;; Monaco Languages functions
;;;;

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


;; - registerCodeActionProvider
;; - registerCodeLensProvider
;; - registerColorProvider


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


;; - registerDeclarationProvider
;; - registerDefinitionProvider
;; - registerDocumentFormattingEditProvider
;; - registerDocumentHighlightProvider
;; - registerDocumentRangeFormattingEditProvider
;; - registerDocumentRangeSemanticTokensProvider
;; - registerDocumentSemanticTokensProvider
;; - registerDocumentSymbolProvider
;; - registerFoldingRangeProvider
;; - registerHoverProvider
;; - registerImplementationProvider
;; - registerLinkProvider
;; - registerOnTypeFormattingEditProvider
;; - registerReferenceProvider
;; - registerRenameProvider
;; - registerSelectionRangeProvider
;; - registerSignatureHelpProvider
;; - registerTypeDefinitionProvider
;; - setLanguageConfiguration


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


;; - setTokensProvider



;;;;
;; Monaco Languages enumerations
;;;;

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


;; - CompletionItemTag
;; - CompletionTriggerKind
;; - DocumentHighlightKind
;; - IndentAction
;; - SignatureHelpTriggerKind
;; - SymbolKind
;; - SymbolTag
