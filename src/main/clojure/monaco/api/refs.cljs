;;;;
;; Copyright © 2019-2020 anywhere.ninja. All rights reserved.
;; The use and distribution terms for this software are covered by the license
;; which can be found in the file license at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by the
;; terms of this license.
;; You must not remove this notice, or any other, from this software.
;;;;

(ns monaco.api.refs
  "Constants and references."
  (:require
    [monaco.js-interop :as j]
    ["monaco" :as monaco]))

(def ^{:added "0.0.8"}
  Monaco
  "A Monaco reference."
  monaco)


(def ^{:added "0.0.4"}
  MonacoEditor
  "A Monaco Editor reference."
  (j/get Monaco :editor))


(def ^{:added "0.0.4"}
  MonacoLanguages
  "A Monaco Languages reference."
  (j/get Monaco :languages))
