(ns monaco.core-test
  (:require
    [cljs.test :as t :include-macros true]
    [monaco.core]))

(t/deftest square-test
  (t/is (= 4 (* 2 2))))
