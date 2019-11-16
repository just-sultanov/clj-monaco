(ns clj-monaco.core-test
  (:require
    [clojure.test :refer [deftest is]]
    [clj-monaco.core :as sut]))

(deftest ^:unit square-test
  (is (= 4 (sut/square 2))))
