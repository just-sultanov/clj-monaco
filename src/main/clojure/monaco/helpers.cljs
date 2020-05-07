(ns monaco.helpers
  (:refer-clojure :exclude [get get-in set])
  (:require
    [goog.object :as gobj]))

(defn get
  ([obj key]
   (get obj key nil))

  ([obj key default]
   (gobj/get obj key default)))


(defn get-in
  ([obj path]
   (get-in obj path nil))

  ([obj path default]
   (or (apply gobj/getValueByKeys obj path)
     default)))


(defn set [obj key value]
  (gobj/set obj key value))


(defn call
  ([obj fn-name]
   (js-invoke obj fn-name))

  ([obj fn-name & args]
   (apply js-invoke obj fn-name (clj->js args))))
