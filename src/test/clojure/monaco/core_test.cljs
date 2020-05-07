;;;;
;; Copyright © 2019-2020 Ilshat Sultanov. All rights reserved.
;; The use and distribution terms for this software are covered by the license which
;; can be found in the file license at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by the terms
;; of this license. You must not remove this notice, or any other, from this software.
;;;;

(ns monaco.core-test
  (:require
    [cljs.test :as t :include-macros true]
    [monaco.core]))

(t/deftest square-test
  (t/is (= 4 (* 2 2))))
