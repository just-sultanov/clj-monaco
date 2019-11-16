(ns clj-monaco.core
  (:require
    [applied-science.js-interop :as j]))

(defn editor []
  (j/get js/monaco "editor"))


(defn square [x]
  (* x x))
