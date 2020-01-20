(ns monaco.monarch
  (:require
    [cljs-bean.core :as b]
    [applied-science.js-interop :as j]
    [monaco.core :as m]))

(defn get-encoded-language-id [language-id]
  (j/call m/monaco-languages :getEncodedLanguageId language-id))

(defn get-languages []
  (j/call m/monaco-languages :getLanguages))

(defn register [language]
  (j/call m/monaco-languages :register (b/->js language)))

(defn set-monarch-tokens-provider [language-id language]
  (j/call m/monaco-languages :setMonarchTokensProvider language-id (b/->js language)))

(defn register-completion-item-provider [language-id provider]
  (j/call m/monaco-languages :registerCompletionItemProvider language-id (b/->js provider)))
