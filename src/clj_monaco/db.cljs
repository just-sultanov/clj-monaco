(ns clj-monaco.db
  (:require
    [re-frame.core :as rf]))

(rf/reg-event-db
  ::init
  (fn [_ _]
    {:text     "(or 1 2)"
     :language "modelizer"}))

(rf/reg-sub
  ::language
  (fn [db]
    (get db :language "")))

(rf/reg-sub
  ::text
  (fn [db]
    (get db :text "")))
