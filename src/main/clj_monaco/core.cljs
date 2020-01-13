(ns clj-monaco.core
  (:require
    [cljs-bean.core :as b]
    [applied-science.js-interop :as j]))

(def monaco js/monaco)
(def editor (j/get js/monaco "editor"))

(defn create-editor [el opts]
  (.create editor el (b/->js opts)))

(defn register-language [])

(defn square [x]
  (* x x))
