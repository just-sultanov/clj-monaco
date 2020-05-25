;;;;
;; Copyright © 2019-2020 Ilshat Sultanov. All rights reserved.
;; The use and distribution terms for this software are covered by the license which
;; can be found in the file license at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by the terms
;; of this license. You must not remove this notice, or any other, from this software.
;;;;

(ns monaco.js-interop
  (:refer-clojure :exclude [get get-in set])
  (:require
    [goog.object :as gobj]
    [clojure.string :as str]))

;; TODO: add map with normalized attributes?

;;;;
;; Helper functions
;;;;

(defn camel-case->kebab-case
  "Converts from `camelCase` to `kebab-case`.

  Examples:
    * `:someKey` -> `some-key`
    * `:SomeKey` -> `some-key`"
  {:added "0.0.6"}
  [k]
  (->> (name k)
    (re-seq #"\w[a-z0-9]*")
    (map str/lower-case)
    (str/join "-")))

(defn kebab-case->camel-case
  "Converts from `kebab-case` to `camelCase`.

  Examples:
    * `:some-key` -> `someKey`
    * `:Some-Key` -> `someKey`"
  {:added "0.0.6"}
  [k]
  (let [[k & ks] (str/split (name k) #"-")]
    (if-not (seq ks)
      k
      (->> ks
        (map str/capitalize)
        (apply str (str/lower-case k))))))


(defn kebab-case->pascal-case
  "Converts from `kebab-case` to `PascalCase`.

  Example:
    * `:some-key` -> `SomeKey`
    * `:Some-Key` -> `SomeKey`"
  {:added "0.0.6"}
  [k]
  (let [[k & ks :as words] (str/split (name k) #"-")]
    (if-not (seq ks)
      (str (str/capitalize (subs k 0 1)) (subs k 1))
      (->> words
        (map str/capitalize)
        (apply str)))))


(defn with-pascal-case
  "Recursively transforms Clojure value to JavaScript.

  Params:
    * `x`                  - `any value`
    * `<:keyword-fn (fn)>` -  a single-argument `function` to be called on keywords

  By default `:keyword-fn` is `kebab-case->pascal-case`."
  {:added "0.0.6"}
  [x & {:keys [keyword-fn]
        :or   {keyword-fn kebab-case->pascal-case}}]
  (clj->js x :keyword-fn keyword-fn))


(defn with-camel-case
  "Recursively transforms Clojure value to JavaScript.

  Params:
    * `x`                  - `any value`
    * `<:keyword-fn (fn)>` -  a single-argument `function` to be called on keywords

  By default `:keyword-fn` is `kebab-case->camel-case`."
  {:added "0.0.6"}
  [x & {:keys [keyword-fn]
        :or   {keyword-fn kebab-case->camel-case}}]
  (clj->js x :keyword-fn keyword-fn))



;;;;
;; Pubic API
;;;;

;; Common

(defn get
  "Same as `clojure.core/get` function, but uses `camelCase` for the key."
  {:added "0.0.4"}
  ([obj key]
   (get obj key nil))

  ([obj key not-found]
   (gobj/get obj (with-camel-case key) not-found)))


(defn get-in
  "Same as `clojure.core/get-in` function, but uses `camelCase` for all keys."
  {:added "0.0.4"}
  ([obj keys]
   (get-in obj keys nil))

  ([obj keys not-found]
   (or (apply gobj/getValueByKeys obj (with-camel-case keys))
     not-found)))


(defn set
  "Same as `goog.object/set` function, but uses `camelCase` for the key."
  {:added "s0.0.4"}
  [obj key value]
  (gobj/set obj (with-camel-case key) value))


(defn invoke
  "Invokes the JavaScript object method via string.
  Uses `camelCase` for the function name and for all arguments."
  {:added "0.0.4"}
  ([obj fn-name]
   (js-invoke obj (with-camel-case fn-name)))

  ([obj fn-name & args]
   (apply js-invoke obj (with-camel-case fn-name) (with-camel-case args))))



;; TODO: If exists other use cases then should be renamed `get*` and `get-in*` functions

;; Example use case - for the enums:

;;`(get* MonacoLanguages :completion-item-kind)`               => `monaco.languages.CompletionItemKind`
;;`(get-in* MonacoLanguages [:completion-item-kind :keyword])` => `monaco.languages.CompletionItemKind.Keyword`


(defn get*
  "Same as `clojure.core/get` function, but uses `PascalCase` for the key."
  {:added "0.0.6"}
  ([obj key]
   (get* obj key nil))

  ([obj key not-found]
   (gobj/get obj (with-pascal-case key) not-found)))


(defn get-in*
  "Same as `clojure.core/get-in` function, but uses `PascalCase` for all keys."
  {:added "0.0.6"}
  ([obj keys]
   (get-in* obj keys nil))

  ([obj keys not-found]
   (or (apply gobj/getValueByKeys obj (with-pascal-case keys))
     not-found)))
