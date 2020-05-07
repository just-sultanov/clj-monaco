;;;;
;; Copyright © 2019-2020 Ilshat Sultanov. All rights reserved.
;; The use and distribution terms for this software are covered by the license which
;; can be found in the file license at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by the terms
;; of this license. You must not remove this notice, or any other, from this software.
;;;;

(ns monaco.js-interop-test
  (:require
    [cljs.test :as t :include-macros true]
    [monaco.js-interop :as sut]))

(t/deftest camel-case->kebab-case-test
  (doseq [k [:some-key :someKey :SomeKey]]
    (t/is (= "some-key" (sut/camel-case->kebab-case k))))
  (t/is (= "on-change" (sut/camel-case->kebab-case :onChange)))
  (t/is (= "on-click" (sut/camel-case->kebab-case :onClick))))


(t/deftest kebab-case->camel-case-test
  (doseq [k [:some-key :some-Key :Some-key :Some-Key]]
    (t/is (= "someKey" (sut/kebab-case->camel-case k))))
  (t/is (= "getFullModelRange" (sut/kebab-case->camel-case :get-full-model-range)))
  (t/is (= "pushEditOperations" (sut/kebab-case->camel-case :push-edit-operations))))


(t/deftest kebab-case->pascal-case-test
  (doseq [k [:some-key :some-Key :Some-key :Some-Key]]
    (t/is (= "SomeKey" (sut/kebab-case->pascal-case k))))
  (t/is (= "CompletionItemInsertTextRule" (sut/kebab-case->pascal-case :completion-item-insert-text-rule)))
  (t/is (= "InsertAsSnippet" (sut/kebab-case->pascal-case :insert-as-snippet))))


(t/deftest get-test
  (t/is (= "ok" (sut/get #js {:someKey "ok"} :some-key)))
  (t/is (= "ok" (sut/get #js {:someKey "ok"} :someKey)))
  (t/is (nil? (sut/get #js {:someKey "ok"} :SomeKey))))


(t/deftest get-in-test
  (t/is (= "ok" (sut/get-in #js {:someKey1 #js {:someKey2 "ok"}} [:some-key1 :some-key2])))
  (t/is (= "ok" (sut/get-in #js {:someKey1 #js {:someKey2 "ok"}} [:someKey1 :someKey2])))
  (t/is (nil? (sut/get-in #js {:someKey1 #js {:someKey2 "ok"}} [:SomeKey1 :SomeKey2]))))


(t/deftest get*-test
  (t/is (= "ok" (sut/get* #js {:SomeKey "ok"} :some-key)))
  (t/is (= "ok" (sut/get* #js {:SomeKey "ok"} :someKey)))
  (t/is (= "ok" (sut/get* #js {:SomeKey "ok"} :SomeKey))))


(t/deftest get-in*-test
  (t/is (= "ok" (sut/get-in* #js {:SomeKey1 #js {:SomeKey2 "ok"}} [:some-key1 :some-key2])))
  (t/is (= "ok" (sut/get-in* #js {:SomeKey1 #js {:SomeKey2 "ok"}} [:someKey1 :someKey2])))
  (t/is (= "ok" (sut/get-in* #js {:SomeKey1 #js {:SomeKey2 "ok"}} [:SomeKey1 :SomeKey2]))))


(t/deftest set-test
  (doseq [k [:some-key :someKey]]
    (let [m #js {}]
      (t/is (nil? (sut/get m k)))
      (sut/set m k "ok")
      (t/is (= "ok" (sut/get m k))))))


(t/deftest invoke-test
  (let [m #js {:someKey (fn [] "ok")}]
    (doseq [k [:some-key :someKey]]
      (t/is (= "ok" (sut/invoke m k))))))
