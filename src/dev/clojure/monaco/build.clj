;;;;
;; Copyright © 2019-2020 Ilshat Sultanov. All rights reserved.
;; The use and distribution terms for this software are covered by the license which
;; can be found in the file license at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by the terms
;; of this license. You must not remove this notice, or any other, from this software.
;;;;

(ns monaco.build
  (:require
    [clojure.java.io :as io]
    [clojure.edn :as edn]))

(set! *warn-on-reflection* true)


(defn safe-slurp [path]
  (let [f (io/file (io/resource path))]
    (try
      (edn/read-string (slurp f))
      (catch Exception _))))


(defmacro read-info []
  (or (safe-slurp "clj-monaco/build.edn")
    {}))
