(ns monaco.monarch
  (:require
    [monaco.core :as m]
    [monaco.helpers :as helpers]))

(defn get-encoded-language-id [language-id]
  (helpers/call m/monaco-languages "getEncodedLanguageId" language-id))

(defn get-languages []
  (helpers/call m/monaco-languages "getLanguages"))

(defn register [language]
  (helpers/call m/monaco-languages "register" language))

(defn set-monarch-tokens-provider [language-id language]
  (helpers/call m/monaco-languages "setMonarchTokensProvider" language-id language))

(defn register-completion-item-provider [language-id provider]
  (helpers/call m/monaco-languages "registerCompletionItemProvider" language-id provider))
