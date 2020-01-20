(ns monaco.core-test
  (:require
    [cljs.test :refer-macros [deftest is]]
    [monaco.core]))

(deftest ^:unit square-test
  (is (= 4 (* 2 2))))
