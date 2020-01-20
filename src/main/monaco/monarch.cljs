(ns monaco.monarch
  (:require
    [cljs-bean.core :as b]
    [applied-science.js-interop :as j]
    [monaco.core :as m]))

(defn register-language [language]
  (j/call-in m/monaco [:languages :register] (b/->js language)))

(defn set-monarch-tokens-provider [language-id language]
  (j/call-in m/monaco [:languages :setMonarchTokensProvider] language-id (b/->js language)))

(defn register-completion-item-provider [language-id provider]
  (j/call-in m/monaco [:languages :registerCompletionItemProvider] language-id (b/->js provider)))

(defn define-theme [theme-name theme-data]
  (j/call-in m/monaco [:editor :defineTheme] theme-name (b/->js theme-data)))
